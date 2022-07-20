package io.quarkus.workshop.super_heroes.fight.mock;

import io.quarkus.test.Mock;
import io.quarkus.workshop.superheroes.fight.proxies.HeroProxy;
import io.quarkus.workshop.superheroes.fight.urls._Hero;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
@RestClient
public class MockHeroProxy implements HeroProxy {

    public static final String DEFAULT_HERO_NAME = "Super Baguette";
    public static final String DEFAULT_HERO_PICTURE = "super_baguette.png";
    public static final String DEFAULT_HERO_POWERS = "eats baguette really quickly";
    public static final int DEFAULT_HERO_LEVEL = 42;

    @Override
    public _Hero findRandomHero() {
        _Hero _hero = new _Hero();
        _hero.name = DEFAULT_HERO_NAME;
        _hero.picture = DEFAULT_HERO_PICTURE;
        _hero.powers = DEFAULT_HERO_POWERS;
        _hero.level = DEFAULT_HERO_LEVEL;
        return _hero;
    }
}
