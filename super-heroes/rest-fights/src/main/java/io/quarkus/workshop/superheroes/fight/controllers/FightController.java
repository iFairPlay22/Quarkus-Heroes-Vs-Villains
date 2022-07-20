package io.quarkus.workshop.superheroes.fight.controllers;

import io.quarkus.workshop.superheroes.fight.models.Fight;
import io.quarkus.workshop.superheroes.fight.urls._Fighters;
import io.quarkus.workshop.superheroes.fight.services.FightService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.MediaType.*;

@Path("/api/fights")
@Produces(APPLICATION_JSON)
public class FightController {

    @Inject
    FightService fightService;

    @GET
    @Path("/randomfighters")
    public Response getRandomFighters() {

        if (Fight.count() == 0)
            return Response.noContent().build();

        var r = fightService.findRandomFighters();
        return Response.ok(r).build();
    }

    @GET
    public Response getAllFights() {

        if (Fight.count() == 0)
            return Response.noContent().build();

        var r = fightService.findAllFights();
        return Response.ok(r).build();
    }

    @GET
    @Path("/{id}")
    public Response getFight(Long id) {
        Fight fight = fightService.findFightById(id);

        if (fight == null)
            return Response.noContent().build();

        return Response.ok(fight).build();
    }

    @POST
    public Response fight(@Valid _Fighters fighters, UriInfo uriInfo) {
        Fight fight = fightService.persistFight(fighters);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(fight.id));
        return Response.created(builder.build())
            .entity(fight)
            .build();
    }
}
