package io.quarkus.workshop.superheroes.statistics.models;

import io.quarkus.workshop.superheroes.statistics.urls.Fight;

// Get the average of the victory of heroes
public class TeamStats {

    private int heroes = 0;
    private int villains = 0;

    public double add(Fight result) {
        if (result.winnerTeam.equalsIgnoreCase("heroes"))
            heroes++;
        else
            villains++;

        return getAverage();
    }

    public double getAverage() {

        if (heroes + villains == 0)
            return 0;

        return ((double) heroes / (heroes + villains));
    }

}
