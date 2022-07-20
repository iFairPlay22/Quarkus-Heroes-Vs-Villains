package io.quarkus.workshop.superheroes.hero.controllers;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.workshop.superheroes.hero.models.Hero;
import io.quarkus.workshop.superheroes.hero.services.HeroService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/heroes")
@Tag(name = "heroes")
public class HeroController {

    @Inject
    HeroService heroService;

    @GET
    public Uni<Response> findAll() {
        return heroService
            .count()
            .onItem()
            .transformToUni((length) -> {
                if (length == 0)
                    return Uni.createFrom().item(Response.noContent().build());

                return heroService
                    .findAll()
                    .map((heroes) -> Response.ok(heroes).build());
            });
    }

    @GET
    @Path("/random")
    public Uni<Response> findRandom() {
        return heroService.count()
            .onItem()
            .transformToUni((length) -> {
                if (length == 0)
                    return Uni.createFrom().item(Response.noContent().build());

                return heroService.findRandom(0L, length).map((hero -> {
                    return Response.ok(hero).build();
            }));
        });
    }

    @GET
    @Path("/{id}")
    public Uni<Response> findById(@RestPath Long id) {
        return heroService.findById(id).map((hero) -> {
            if (hero == null)
                return Response.noContent().build();

            return Response.ok(hero).build();
        });
    }

    @POST
    @ReactiveTransactional
    public Uni<Response> create(@Valid Hero hero, @Context UriInfo uriInfo) {
        return heroService.create(hero).map((createdHero -> {
            if (createdHero == null)
                return Response.noContent().build();

            UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(createdHero.id));
            return Response.created(builder.build()).build();
        }));
    }

    @PUT
    @ReactiveTransactional
    @Path("/{id}")
    public Uni<Response> update(@RestPath Long id, @Valid Hero newHero) {
        return heroService.findById(id)
            .onItem()
            .transformToUni((oldHero) -> {
                if (oldHero == null)
                    return Uni.createFrom().item(Response.noContent().build());

                return heroService.update(oldHero, newHero).map((updatedHero -> {
                    return Response.ok(updatedHero).build();
            }));
        });
    }

    @DELETE
    @ReactiveTransactional
    @Path("/{id}")
    public Uni<Response> delete(@RestPath Long id) {

        return heroService.findById(id)
            .onItem()
            .transformToUni((hero -> {
                if (hero == null)
                    return Uni.createFrom().item(Response.notModified().build());

                return heroService.delete(hero).replaceWith(() -> {
                    return Response.noContent().build();
            });
        }));

    }
}
