package io.quarkus.workshop.superheroes.villain;

import io.quarkus.workshop.superheroes.villain.models.Villain;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Test the Villain CRUD")
public class VillainControllerTest {

    private void shouldNotGetList() {
        given()
            .header(CONTENT_TYPE, JSON)
            .header(ACCEPT, JSON)
            .when()
            .get("/api/villains")
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
                .get("/api/villains")
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
            .get("/api/villains/{id}")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
    }

    private void shouldGetSingleItem(Villain villain) {

        long id = villain.id;

        if (!(1 <= id))
            throw new IllegalArgumentException("Id must be greater than 1");

        var e =
            given()
                .pathParam("id", id)
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
                .when()
                .get("/api/villains/{id}")
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .body()
                .as(getElementTypeRef());

        assertThat(e.name,       Is.is(villain.name));
        assertThat(e.otherName,  Is.is(villain.otherName));
        assertThat(e.level,      Is.is(villain.level));
        assertThat(e.picture,    Is.is(villain.picture));
        assertThat(e.powers,     Is.is(villain.powers));
    }

    private void shouldNotGetRandomSingleItem() {
        given()
            .header(CONTENT_TYPE, JSON)
            .header(ACCEPT, JSON)
            .when()
            .get("/api/villains/random")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
    }

    private void shouldGetRandomSingleItem() {

        var e =
            given()
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
                .when()
                .get("/api/villains/random")
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .body()
                .as(getElementTypeRef());

        assertNotNull(e.name);
    }

    private long shouldAddSingleItem(Villain villain) {

        var l =
            given()
                .body(villain)
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
                .when()
                .post("/api/villains")
                .then()
                .statusCode(CREATED.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .header("Location");

        assertTrue(l.contains("/api/villains"));

        String[] segments = l.split("/");
        String id = segments[segments.length - 1];
        assertNotNull(id);

        return Long.parseLong(id);
    }

    private void shouldNotAddAnInvalidSingleItem(Villain villain) {

        given()
            .body(villain)
            .header(CONTENT_TYPE, JSON)
            .header(ACCEPT, JSON)
            .when()
            .post("/api/villains")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());
    }

    private long shouldEditSingleItem(long id, Villain villain) {

        if (!(1 <= id))
            throw new IllegalArgumentException("Id must be greater than 1");

        var e =
            given()
                .pathParam("id", id)
                .body(villain)
                .header(CONTENT_TYPE, JSON)
                .header(ACCEPT, JSON)
                .when()
                .put("/api/villains/{id}")
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .body()
                .as(getElementTypeRef());

        assertThat(e.name,       Is.is(villain.name));
        assertThat(e.otherName,  Is.is(villain.otherName));
        assertThat(e.level,      Is.is(villain.level));
        assertThat(e.picture,    Is.is(villain.picture));
        assertThat(e.powers,     Is.is(villain.powers));

        return id;
    }

    private void shouldNotEditNotExistingVillain(long id, Villain villain) {

        given()
            .pathParam("id", id)
            .body(villain)
            .header(CONTENT_TYPE, JSON)
            .header(ACCEPT, JSON)
            .when()
            .put("/api/villains/{id}")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
    }

    private void shouldNotEditExistingVillainWithInvalidValues(long id, Villain villain) {

        if (!(1 <= id))
            throw new IllegalArgumentException("Id must be greater than 1");

        given()
            .pathParam("id", id)
            .body(villain)
            .header(CONTENT_TYPE, JSON)
            .header(ACCEPT, JSON)
            .when()
            .put("/api/villains/{id}")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());
    }

    private void shouldDeleteSingleItem(long id) {

        given()
            .pathParam("id", id)
            .header(CONTENT_TYPE, JSON)
            .header(ACCEPT, JSON)
            .when()
            .delete("/api/villains/{id}")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
    }

    private void shouldNotDeleteSingleItem(long id) {

        given()
            .pathParam("id", id)
            .header(CONTENT_TYPE, JSON)
            .header(ACCEPT, JSON)
            .when()
            .delete("/api/villains/{id}")
            .then()
            .statusCode(NOT_MODIFIED.getStatusCode());
    }

    private TypeRef<List<Villain>> getListTypeRef() {
        return new TypeRef<>() {};
    }

    private TypeRef<Villain> getElementTypeRef() {
        return new TypeRef<>() {};
    }

    @Test
    @Order(1)
    @DisplayName(" (1) : Ensure that it is possible to add a first valid element to the list of villains")
    public void s1_AddOneElementToList() {
        Villain validVillain1 = new Villain("name1", "otherName1", 1, "picture1", "powers1");
        validVillain1.id = shouldAddSingleItem(validVillain1);
        shouldGetSingleItem(validVillain1);
    }

    @Test
    @Order(2)
    @DisplayName(" (2) : Ensure that it is not possible to add an invalid element to the list of villains")
    public void s1_AddInvalidElementToList() {
        Villain invalidVillain1 = new Villain("n", "o", -1, "p", "p");
        invalidVillain1.id = -1L;
        shouldNotAddAnInvalidSingleItem(invalidVillain1);
        shouldNotGetSingleItem(invalidVillain1.id);
    }

    @Test
    @Order(3)
    @DisplayName(" (3) : Ensure that it is possible to add a secondary valid element to the list of villains")
    public void s1_AddSecondElementToList() {
        Villain validVillain2 = new Villain("name2", "otherName2", 2, "picture2", "powers2");
        validVillain2.id = shouldAddSingleItem(validVillain2);
        shouldGetSingleItem(validVillain2);
    }

    @RepeatedTest(5)
    @Order(4)
    @DisplayName(" (4) : Ensure that it is possible to get a random element")
    public void s1_GetRandomElement() {
        shouldGetRandomSingleItem();
    }

    @RepeatedTest(2)
    @Order(5)
    @DisplayName(" (5) : Ensure that is it possible to modify an element")
    public void s1_EditElement() {
        Villain validVillain3 = new Villain("name3", "otherName3", 3, "picture3", "powers3");
        validVillain3.id = shouldAddSingleItem(validVillain3);
        shouldGetSingleItem(validVillain3);

        Villain validVillain4 = new Villain("name4", "otherName4", 4, "picture4", "powers4");
        validVillain4.id = shouldEditSingleItem(validVillain3.id, validVillain4);
        shouldGetSingleItem(validVillain4);
    }

    @Test
    @Order(6)
    @DisplayName(" (6) : Ensure that is it not possible to modify an element that does not exists")
    public void s1_EditInvalidElement() {
        Villain validVillain6 = new Villain("name6", "otherName6", 6, "picture6", "powers6");
        validVillain6.id = -1L;
        shouldNotEditNotExistingVillain(validVillain6.id, validVillain6);
        shouldNotGetSingleItem(validVillain6.id);
    }

    @Test
    @Order(7)
    @DisplayName(" (7) : Ensure that is it not possible to modify an element that exists with bad values")
    public void s1_EditInvalidValues() {

        Villain validVillain7 = new Villain("name7", "otherName7", 7, "picture7", "powers7");
        validVillain7.id = shouldAddSingleItem(validVillain7);
        shouldGetSingleItem(validVillain7);

        Villain invalidVillain2 = new Villain("n2", "o2", -2, "p2", "p2");
        invalidVillain2.id = -1L;
        shouldNotEditExistingVillainWithInvalidValues(validVillain7.id, invalidVillain2);
        shouldGetSingleItem(validVillain7);

        shouldNotGetSingleItem(invalidVillain2.id);
    }

    @Test
    @Order(8)
    @DisplayName(" (8) : Ensure that is it not possible to delete an invalid element")
    public void s1_DeleteInvalidElement() {
        shouldNotDeleteSingleItem(-1L);
        shouldNotGetSingleItem(-1L);
    }

    @Test
    @Order(9)
    @DisplayName(" (9) : Ensure that is it possible to delete an valid element")
    public void s1_DeleteValidElement() {
        Villain validVillain8 = new Villain("name8", "otherName8", 8, "picture8", "powers8");
        validVillain8.id = shouldAddSingleItem(validVillain8);
        shouldGetSingleItem(validVillain8);

        shouldDeleteSingleItem(validVillain8.id);
        shouldNotGetSingleItem(validVillain8.id);
    }

}

