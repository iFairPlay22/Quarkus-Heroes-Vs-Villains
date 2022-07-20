package io.quarkus.workshop.superheroes.fight.mock;
import io.quarkus.test.Mock;
import io.quarkus.workshop.superheroes.fight.proxies.VillainProxy;
import io.quarkus.workshop.superheroes.fight.urls._Villain;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
@RestClient
public class MockVillainProxy implements VillainProxy {

    public static final String DEFAULT_VILLAIN_NAME = "Super Chocolatine";
    public static final String DEFAULT_VILLAIN_PICTURE = "super_chocolatine.png";
    public static final String DEFAULT_VILLAIN_POWERS = "does not eat pain au chocolat";
    public static final int DEFAULT_VILLAIN_LEVEL = 42;

    @Override
    public _Villain findRandomVillain() {
        _Villain _villain = new _Villain();
        _villain.name = DEFAULT_VILLAIN_NAME;
        _villain.picture = DEFAULT_VILLAIN_PICTURE;
        _villain.powers = DEFAULT_VILLAIN_POWERS;
        _villain.level = DEFAULT_VILLAIN_LEVEL;
        return _villain;
    }
}
