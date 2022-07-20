package io.quarkus.workshop.superheroes.fight;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.workshop.super_heroes.fight.mock.MockHeroProxy;
import io.quarkus.workshop.superheroes.fight.mock.MockVillainProxy;
import io.quarkus.workshop.superheroes.fight.models.Fight;
import io.quarkus.workshop.superheroes.fight.urls._Fighters;
import io.quarkus.workshop.superheroes.fight.urls._Hero;
import io.quarkus.workshop.superheroes.fight.urls._Villain;
import io.restassured.common.mapper.TypeRef;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.*;
import org.wildfly.common.Assert;

import java.time.Instant;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Test the Fight CRUD")
public class FightResourceTest {

    private void shouldNotGetList() {
        given()
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
            .when()
                .get("/api/fights")
            .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    private void shouldGetListOfLength(int length) {

        if (!(1 <= length))
            throw new IllegalArgumentException("Length must be greater than 1");

        var l =
            given()
                    .header(CONTENT_TYPE, JSON)
                    .header(ACCEPT, JSON)
                .when()
                    .get("/api/fights")
                .then()
                    .statusCode(OK.getStatusCode())
                    .contentType(APPLICATION_JSON)
                .extract()
                    .body()
                    .as(getListTypeRef());

        assertThat(l, hasSize(length));
    }

    private void shouldNotGetSingleItem(long id) {
        given()
                .pathParam("id", id)
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
            .when()
                .get("/api/fights/{id}")
            .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    private void shouldGetSingleItem(long id, Fight fight) {

        if (!(1 <= id))
            throw new IllegalArgumentException("Id must be greater than 1");

        var e =
            given()
                .pathParam("id", id)
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
            .when()
                .get("/api/fights/{id}")
            .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
            .extract()
                .body()
                .as(getElementTypeRef());

        // assertThat(e.fightDate,       Is.is(fight.fightDate));

        assertThat(e.winnerName,        anyOf(Is.is(fight.winnerName),      Is.is(fight.looserName))        );
        assertThat(e.winnerPicture,     anyOf(Is.is(fight.winnerPicture),   Is.is(fight.looserPicture))     );
        assertThat(e.winnerTeam,        anyOf(Is.is("villains"),      Is.is("heroes"))          );
        Assert.assertNotNull(e.winnerLevel);

        assertThat(e.looserName,        anyOf(Is.is(fight.winnerName),      Is.is(fight.looserName))        );
        assertThat(e.looserPicture,     anyOf(Is.is(fight.winnerPicture),   Is.is(fight.looserPicture))     );
        assertThat(e.looserTeam,        anyOf(Is.is("villains"),      Is.is("heroes"))          );
        Assert.assertNotNull(e.looserLevel);
    }

    private void shouldGetRandomSingleItem() {

        _Fighters fighters =
            given()
                    .header(CONTENT_TYPE, JSON)
                    .header(ACCEPT, JSON)
                .when()
                .   get("/api/fights/randomfighters")
                .then()
                    .statusCode(OK.getStatusCode())
                    .contentType(APPLICATION_JSON)
                .extract()
                    .body()
                    .as(new TypeRef<_Fighters>() {});

        assertThat(fighters.hero.name,          Is.is(MockHeroProxy.DEFAULT_HERO_NAME));
        assertThat(fighters.hero.picture,       Is.is(MockHeroProxy.DEFAULT_HERO_PICTURE));
        assertThat(fighters.hero.level,         Is.is(MockHeroProxy.DEFAULT_HERO_LEVEL));

        assertThat(fighters.villain.name,       Is.is(MockVillainProxy.DEFAULT_VILLAIN_NAME));
        assertThat(fighters.villain.picture,    Is.is(MockVillainProxy.DEFAULT_VILLAIN_PICTURE));
        assertThat(fighters.villain.level,      Is.is(MockVillainProxy.DEFAULT_VILLAIN_LEVEL));
    }

    private long shouldAddSingleItem(_Fighters fighters) {

        var r =
            given()
                .body(fighters)
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
            .when()
                .post("/api/fights")
            .then()
                .statusCode(CREATED.getStatusCode())
                .contentType(APPLICATION_JSON);

        var l = r.extract().header("Location");

        assertTrue(l.contains("/api/fights"));

        String[] segments = l.split("/");
        String id = segments[segments.length - 1];
        assertNotNull(id);

        var e = r.extract().body().as(getElementTypeRef());
        Assert.assertNotNull(e.winnerName);
        Assert.assertNotNull(e.winnerPicture);
        Assert.assertNotNull(e.winnerLevel);
        assertThat(e.winnerTeam, anyOf(Is.is("villains"), Is.is("heroes")));

        Assert.assertNotNull(e.looserName);
        Assert.assertNotNull(e.looserPicture);
        Assert.assertNotNull(e.looserLevel);
        assertThat(e.looserTeam, anyOf(Is.is("villains"), Is.is("heroes")));

        return Long.parseLong(id);
    }

    private TypeRef<List<Fight>> getListTypeRef() {
        return new TypeRef<>() {};
    }

    private TypeRef<Fight> getElementTypeRef() {
        return new TypeRef<>() {};
    }

    @Test
    @Order(1)
    @DisplayName(" (1) : Ensure that it is possible to add a first valid element to the list of fights")
    public void s1_AddOneElementToList() {
        _Fighters validFighters1 = new _Fighters(
            new _Hero("heroName1", 1, "heroPicture1", "heroPowers1"),
            new _Villain("villainName1", 1, "villainPicture1", "villainPowers1")
        );
        Fight validFight1 = new Fight(Instant.now(), "heroName1", 1, "heroPicture1", "winnerTeam1", "villainName1", 1, "villainPicture1", "looserTeam1");
        validFight1.id = shouldAddSingleItem(validFighters1);
        shouldGetSingleItem(validFight1.id, validFight1);
    }

    @Test
    @Order(2)
    @DisplayName(" (2) : Ensure that it is possible to add a secondary valid element to the list of fights")
    public void s1_AddSecondElementToList() {
        _Fighters validFighters2 = new _Fighters(
            new _Hero("heroName2", 2, "heroPicture2", "heroPowers2"),
            new _Villain("villainName2", 2, "villainPicture2", "villainPowers2")
        );
        Fight validFight2 = new Fight(Instant.now(), "heroName2", 1, "heroPicture2", "winnerTeam2", "villainName2", 2, "villainPicture2", "looserTeam2");
        validFight2.id = shouldAddSingleItem(validFighters2);
        shouldGetSingleItem(validFight2.id, validFight2);
    }

    @RepeatedTest(5)
    @Order(3)
    @DisplayName(" (3) : Ensure that it is possible to get a random element")
    public void s1_GetRandomElement() {
        shouldGetRandomSingleItem();
    }
}
