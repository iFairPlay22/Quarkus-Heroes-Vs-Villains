package io.quarkus.workshop.superheroes.villain.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jdk.jfr.Description;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Description("Represents a villain, that fight heroes")
public class Villain extends PanacheEntity {

    public Villain() {
        super();
    }

    public Villain(String name, String otherName, int level, String picture, String powers) {
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

    @Size(min = 3, max = 100)
    public String picture;

    @Column(columnDefinition = "TEXT")
    public String powers;

    @Override
    public String toString() {
        return "Villain{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", otherName='" + otherName + '\'' +
            ", level=" + level +
            ", picture='" + picture + '\'' +
            ", powers='" + powers + '\'' +
            '}';
    }
}
