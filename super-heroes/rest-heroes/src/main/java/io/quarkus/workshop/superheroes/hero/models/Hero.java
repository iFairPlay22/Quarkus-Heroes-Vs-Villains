package io.quarkus.workshop.superheroes.hero.models;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Hero extends PanacheEntity {

    public Hero() {
        super();
    }

    public Hero(String name, String otherName, int level, String picture, String powers) {
        this();
        this.name = name;
        this.otherName = otherName;
        this.level = level;
        this.picture = picture;
        this.powers = powers;
    }

    @NotNull
    @Size(min = 3, max = 50)
    public String name;

    @Size(min = 3, max = 50)
    public String otherName;

    @NotNull
    @Min(1)
    public int level;

    @Size(min = 3, max = 300)
    public String picture;

    @Column(columnDefinition = "TEXT")
    public String powers;

    @Override
    public String toString() {
        return "Hero{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", otherName='" + otherName + '\'' +
            ", level=" + level +
            ", picture='" + picture + '\'' +
            ", powers='" + powers + '\'' +
            '}';
    }
}
