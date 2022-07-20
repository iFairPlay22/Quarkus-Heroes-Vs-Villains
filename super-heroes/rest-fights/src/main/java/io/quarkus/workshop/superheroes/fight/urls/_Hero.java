package io.quarkus.workshop.superheroes.fight.urls;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description="The hero fighting against the villain")
public class _Hero {

    public _Hero() {
        super();
    }

    public _Hero(
        @NotNull String name,
        @NotNull int level,
        @NotNull String picture,
        String powers
    ) {
        this();
        this.name = name;
        this.level = level;
        this.picture = picture;
        this.powers = powers;
    }

    @NotNull
    @Size(min = 3, max = 50)
    public String name;

    @NotNull
    @Min(1)
    public int level;

    @NotNull
    @Size(min = 3, max = 300)
    public String picture;

    public String powers;

    @Override
    public String toString() {
        return "_Hero{" +
            "name='" + name + '\'' +
            ", level=" + level +
            ", picture='" + picture + '\'' +
            ", powers='" + powers + '\'' +
            '}';
    }
}
