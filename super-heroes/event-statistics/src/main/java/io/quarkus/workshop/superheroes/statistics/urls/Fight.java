package io.quarkus.workshop.superheroes.statistics.urls;

import io.smallrye.common.constraint.NotNull;

import java.time.Instant;


public class Fight {

    @NotNull
    public Instant fightDate;

    @NotNull
    public String winnerName;

    @NotNull
    public String winnerTeam;

    @NotNull
    public int winnerLevel;

    @NotNull
    public String winnerPicture;

    @NotNull
    public String looserName;

    @NotNull
    public int looserLevel;

    @NotNull
    public String looserPicture;

    @NotNull
    public String looserTeam;
}
