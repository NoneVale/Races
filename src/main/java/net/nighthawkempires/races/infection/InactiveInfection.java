package net.nighthawkempires.races.infection;

import net.nighthawkempires.core.datasection.DataSection;

import java.util.UUID;

public class InactiveInfection {

    private UUID infected;
    private UUID infectedBy;

    private int secondsLeft;

    public InactiveInfection(UUID infected, UUID infectedBy, int secondsLeft) {
        this.infected = infected;
        this.infectedBy = infectedBy;

        this.secondsLeft = secondsLeft;
    }

    public InactiveInfection(DataSection data) {
        this.infected = UUID.fromString(data.getString("infected"));
        this.infectedBy = UUID.fromString(data.getString("infected_by"));

        this.secondsLeft = data.getInt("seconds_left");
    }

    public UUID getInfected() {
        return infected;
    }

    public UUID getInfectedBy() {
        return infectedBy;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public ActiveInfection toActive() {
        return new ActiveInfection(this.infected, this.infectedBy, secondsLeft);
    }
}