package io.quarkus.workshop.superheroes.statistics.models;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;

// Save a score and a name
@RegisterForReflection
public class Score {

    @NotNull
    public String name;

    @NotNull
    public int score = 0;
}
