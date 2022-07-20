package io.quarkus.workshop.superheroes.fight.util;

import java.util.Random;

public class RandomFunctions {
    private static Random random = new Random();

    public static int randint(int min, int max) {

        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");

        return random.nextInt((max - min) + 1) + min;
    }

}
