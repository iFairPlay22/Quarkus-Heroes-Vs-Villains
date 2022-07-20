package io.quarkus.workshop.superheroes.fight.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Schema(description="Each fight has a winner and a loser")
public class Fight extends PanacheEntity {

    public Fight() {
        super();
    }

    public Fight(
        @NotNull Instant fightDate,
        @NotNull String winnerName,
        @NotNull int winnerLevel,
        @NotNull String winnerPicture,
        @NotNull String winnerTeam,
        @NotNull String looserName,
        @NotNull int looserLevel,
        @NotNull String looserPicture,
        @NotNull String looserTeam
    ) {
        this();
        this.fightDate = fightDate;
        this.winnerName = winnerName;
        this.winnerLevel = winnerLevel;
        this.winnerPicture = winnerPicture;
        this.winnerTeam = winnerTeam;
        this.looserName = looserName;
        this.looserLevel = looserLevel;
        this.looserPicture = looserPicture;
        this.looserTeam = looserTeam;
    }

    // Fight
    @NotNull
    public Instant fightDate;

    // Winner
    @NotNull
    public String winnerName;
    @NotNull
    public int winnerLevel;
    @NotNull
    public String winnerPicture;
    @NotNull
    public String winnerTeam;

    // Looser
    @NotNull
    public String looserName;
    @NotNull
    public int looserLevel;
    @NotNull
    public String looserPicture;
    @NotNull
    public String looserTeam;

    @Override
    public String toString() {
        return "Fight{" +
            "fightDate=" + fightDate +
            ", winnerName='" + winnerName + '\'' +
            ", winnerLevel=" + winnerLevel +
            ", winnerPicture='" + winnerPicture + '\'' +
            ", winnerTeam='" + winnerTeam + '\'' +
            ", looserName='" + looserName + '\'' +
            ", looserLevel=" + looserLevel +
            ", looserPicture='" + looserPicture + '\'' +
            ", looserTeam='" + looserTeam + '\'' +
            '}';
    }
}
