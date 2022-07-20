package io.quarkus.workshop.superheroes.villain.controllers;

import io.quarkus.workshop.superheroes.villain.models.Villain;
import io.quarkus.workshop.superheroes.villain.services.VillainService;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/villains")
public class VillainController {

    @Inject VillainService villainService;

    @GET
    public Response findAll() {

        if (Villain.count() == 0)
            return Response.noContent().build();

        List<Villain> r = villainService.findAll();
        return Response.ok(r).build();
    }

    @GET
    @Path("/random")
    public Response findRandom() {

        if (Villain.count() == 0)
            return Response.noContent().build();

        Villain r = villainService.findRandom();
        return Response.ok(r).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@RestPath Long id) {
        Villain r = villainService.findById(id);

        if (r == null)
            return Response.noContent().build();

        return Response.ok(r).build();
    }

    @POST
    @Transactional
    public Response create(@Valid Villain villain, @Context UriInfo uriInfo) {
        villainService.create(villain);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.id));
        return Response.created(builder.build()).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response update(@RestPath Long id, @Valid Villain newVillain) {

        Villain oldVillain = villainService.findById(id);

        if (oldVillain == null)
            return Response.noContent().build();

        Villain r = villainService.update(oldVillain, newVillain);
        return Response.ok(r).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@RestPath Long id) {
        Villain villain = villainService.findById(id);

        if (villain == null)
            return Response.notModified().build();

        villainService.delete(villain);
        return Response.noContent().build();
    }
}
