package net.nighthawkempires.races.infection;

import net.nighthawkempires.core.datasection.DataSection;
import org.bukkit.Bukkit;

import java.util.UUID;

public class ActiveInfection {

    private UUID infected;
    private UUID infectedBy;

    private int taskId = -1;

    private long infectTimestamp;

    public ActiveInfection(UUID infected, UUID infectedBy, int seconds) {
        this.infected = infected;
        this.infectedBy = infectedBy;

        this.infectTimestamp = System.currentTimeMillis() + (seconds * 1000L);
    }

    public ActiveInfection(DataSection data) {
        this.infected = UUID.fromString(data.getString("infected"));
        this.infectedBy = UUID.fromString(data.getString("infected_by"));

        this.infectTimestamp = data.getLong("infect_timestamp");
    }

    public UUID getInfected() {
        return infected;
    }

    public UUID getInfectedBy() {
        return infectedBy;
    }

    public int getTaskId() {
        return this.taskId;
    }

    public void setTaskId(int taskId) {
        if (this.taskId != -1) Bukkit.getScheduler().cancelTask(this.taskId);
        this.taskId = taskId;
    }

    public long getInfectTimestamp() {
        return infectTimestamp;
    }

    public long millisLeft() {
        if (System.currentTimeMillis() >= this.infectTimestamp) return 0;

        return this.infectTimestamp - System.currentTimeMillis();
    }

    public int secondsLeft() {
        if (millisLeft() == 0) return 0;

        return (int) Math.ceil((double) millisLeft() / 1000);
    }

    public InactiveInfection toInactive() {
        if (this.taskId != -1) Bukkit.getScheduler().cancelTask(this.taskId);
        return new InactiveInfection(this.infected, this.infectedBy, secondsLeft());
    }
}