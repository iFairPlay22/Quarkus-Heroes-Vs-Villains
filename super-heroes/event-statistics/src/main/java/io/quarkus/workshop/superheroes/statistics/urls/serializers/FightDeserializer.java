package io.quarkus.workshop.superheroes.statistics.urls.serializers;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkus.workshop.superheroes.statistics.urls.Fight;

public class FightDeserializer extends ObjectMapperDeserializer<Fight> {

    public FightDeserializer() {
        super(Fight.class);
    }
}
