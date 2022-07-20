package io.quarkus.workshop.superheroes.fight.services;

import io.quarkus.workshop.superheroes.fight.models.Fight;
import io.quarkus.workshop.superheroes.fight.proxies.HeroProxy;
import io.quarkus.workshop.superheroes.fight.proxies.VillainProxy;
import io.quarkus.workshop.superheroes.fight.urls._Fighters;
import io.quarkus.workshop.superheroes.fight.urls._Hero;
import io.quarkus.workshop.superheroes.fight.urls._Villain;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
public class FightService {

    @Channel("fights")
    Emitter<Fight> emitter;

    @Inject
    Logger logger;

    @RestClient HeroProxy heroProxy;
    @RestClient VillainProxy villainProxy;

    private final Random random = new Random();

    @Transactional(SUPPORTS)
    public List<Fight> findAllFights() {
        return Fight.listAll();
    }

    @Transactional(SUPPORTS)
    public Fight findFightById(Long id) {
        return Fight.findById(id);
    }

    @Transactional(SUPPORTS)
    public _Fighters findRandomFighters() {
        return new _Fighters(
            findRandomHero(),
            findRandomVillain()
        );
    }

    @Fallback(fallbackMethod = "fallbackRandomHero")
    _Hero findRandomHero() {
        return heroProxy.findRandomHero();
    }

    @Fallback(fallbackMethod = "fallbackRandomVillain")
    _Villain findRandomVillain() {
        return villainProxy.findRandomVillain();
    }

    public _Hero fallbackRandomHero() {
        logger.warn("Falling back on _Hero");
        _Hero hero = new _Hero();
        hero.name = "Fallback hero";
        hero.picture = "https://dummyimage.com/280x380/1e8fff/ffffff&text=Fallback+_Hero";
        hero.powers = "Fallback hero powers";
        hero.level = 1;
        return hero;
    }

    public _Villain fallbackRandomVillain() {
        logger.warn("Falling back on _Villain");
        _Villain villain = new _Villain();
        villain.name = "Fallback villain";
        villain.picture = "https://dummyimage.com/280x380/b22222/ffffff&text=Fallback+_Villain";
        villain.powers = "Fallback villain powers";
        villain.level = 42;
        return villain;
    }

    @Transactional(REQUIRED)
    public Fight persistFight(@Valid _Fighters fighters) {
        Fight fight;

        int heroAdjust = random.nextInt(20);
        int villainAdjust = random.nextInt(20);

        if ((fighters.hero.level + heroAdjust) > (fighters.villain.level + villainAdjust)) {
            fight = heroWon(fighters);
        } else if (fighters.hero.level < fighters.villain.level) {
            fight = villainWon(fighters);
        } else {
            fight = random.nextBoolean() ? heroWon(fighters) : villainWon(fighters);
        }

        fight.persist();

        emitter.send(fight).toCompletableFuture().join();

        return fight;
    }

    private Fight heroWon(@Valid _Fighters fighters) {
        Fight fight = new Fight();
        fight.fightDate = Instant.now();

        fight.winnerName = fighters.hero.name;
        fight.winnerPicture = fighters.hero.picture;
        fight.winnerLevel = fighters.hero.level;
        fight.winnerTeam = "heroes";

        fight.looserName = fighters.villain.name;
        fight.looserPicture = fighters.villain.picture;
        fight.looserLevel = fighters.villain.level;
        fight.looserTeam = "villains";

        return fight;
    }

    private Fight villainWon(@Valid _Fighters fighters) {
        Fight fight = new Fight();
        fight.fightDate = Instant.now();

        fight.winnerName = fighters.villain.name;
        fight.winnerPicture = fighters.villain.picture;
        fight.winnerLevel = fighters.villain.level;
        fight.winnerTeam = "villains";

        fight.looserName = fighters.hero.name;
        fight.looserPicture = fighters.hero.picture;
        fight.looserLevel = fighters.hero.level;
        fight.looserTeam = "heroes";

        return fight;
    }

}
