package net.nighthawkempires.races.infection;

import net.nighthawkempires.core.datasection.DataSection;
import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.Bukkit;

import java.util.UUID;

public class Infection {

    private UUID infected;
    private UUID infectedBy;
    private long finishMillis;

    private int secondsLeft = -1;
    private int taskId = -1;
    private int repeatingTaskId = -1;

    public Infection(UUID infected, UUID infectedBy) {
        this.infected = infected;
        this.infectedBy = infectedBy;
        this.finishMillis = System.currentTimeMillis() + (3600000);
    }

    public Infection(DataSection data) {

    }

    public UUID getInfected() {
        return infected;
    }

    public UUID getInfectedBy() {
        return infectedBy;
    }

    public int getSecondsLeft() {
        return ((int) Math.ceil((double) finishMillis / 1000));
    }

    public void start() {
        this.finishMillis = System.currentTimeMillis() + (this.secondsLeft * 1000L);

        this.repeatingTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
            int hour = 60 * 60, fiftyMins = 50 * 60, fourtyMins = 40 * 60, thirtyMins = 30 * 60, twentyMins = 20 * 60, tenMins = 10 * 60;

            if (getSecondsLeft() <= hour && getSecondsLeft() > fiftyMins) {
                //TODO: Give Player Nausea every 5 minutes // 5 sec
            } else if (getSecondsLeft() <= fiftyMins && getSecondsLeft() > fourtyMins) {
                //TODO: Give Player Nausea every 3 minutes // 7 sec
            } else if (getSecondsLeft() <= fourtyMins && getSecondsLeft() > thirtyMins) {
                //TODO: Give Player Nausea every 2 minutes // 10 sec
            } else if (getSecondsLeft() <= thirtyMins && getSecondsLeft() > twentyMins) {
                //TODO: Give Player Nausea and Blindness every 2 minutes // 10 sec
            } else if (getSecondsLeft() <= twentyMins && getSecondsLeft() > tenMins) {
                //TODO: Give Player Nausea, Blindness and Food Poisoning every minute // 10 sec (Poisoning 15 sec)
            } else if (getSecondsLeft() <= tenMins) {
                //TODO: Give Player Nausea, Blindness, Food Poisoning, and Slowness every 30 seconds // 15 sec (Poisoning 20 sec)
            }
        }, 0, 1800L);
        this.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {

        }, this.secondsLeft * 20L);
    }

    public void stop() {
        this.secondsLeft = getSecondsLeft();
        Bukkit.getScheduler().cancelTask(this.repeatingTaskId);
        Bukkit.getScheduler().cancelTask(this.taskId);
    }
}
