package io.quarkus.workshop.superheroes.fight.urls;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Schema(description="A fight between one hero and one villain")
public class _Fighters {

    public _Fighters() {
        super();
    }

    public _Fighters(@Valid _Hero hero, @Valid _Villain villain) {
        this.hero = hero;
        this.villain = villain;
    }

    @NotNull
    public _Hero hero;

    @NotNull
    public _Villain villain;

    @Override
    public String toString() {
        return "_Fighters{" +
            "hero=" + hero +
            ", villain=" + villain +
            '}';
    }
}
