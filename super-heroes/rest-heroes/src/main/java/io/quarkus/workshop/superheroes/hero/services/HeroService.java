package io.quarkus.workshop.superheroes.hero.services;

import io.quarkus.workshop.superheroes.hero.models.Hero;
import io.quarkus.workshop.superheroes.hero.util.RandomFunctions;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import java.util.List;

@ApplicationScoped
public class HeroService {

    public Uni<Long> count() { return Hero.count(); }

    public Uni<List<Hero>> findAll() {
        return Hero.listAll();
    }

    public Uni<Hero> findById(long id) {
        return Hero.findById(id);
    }

    public Uni<Hero> findRandom(long minId, long maxId) {
        Long id = (long) RandomFunctions.randint((int) minId, (int) maxId);

        return Hero.<Hero>findById(id)
            .onItem()
            .transformToUni((hero) -> {
               if (hero != null)
                   return Uni.createFrom().item(hero);

               return findRandom(minId, maxId);
            });
    }

    public Uni<Hero> create(@Valid Hero hero) {
        return hero.persist();
    }

    public Uni<Hero> update(@Valid Hero oldHero, @Valid Hero newHero) {
        oldHero.name      = newHero.name;
        oldHero.otherName = newHero.otherName;
        oldHero.picture   = newHero.picture;
        oldHero.powers    = newHero.powers;
        oldHero.level     = newHero.level;
        return oldHero.persist();
    }

    public Uni<Void> delete(@Valid Hero hero) {
        return hero.delete();
    }
}
