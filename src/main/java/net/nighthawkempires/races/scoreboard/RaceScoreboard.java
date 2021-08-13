package net.nighthawkempires.races.scoreboard;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.core.scoreboard.NEScoreboard;
import net.nighthawkempires.core.settings.ConfigModel;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static org.bukkit.ChatColor.*;

public class RaceScoreboard extends NEScoreboard {

    private int taskId;

    @Override
    public int getPriority() {
        return 6;
    }

    public String getName() {
        return "races";
    }

    public int getTaskId() {
        return this.taskId;
    }

    public Scoreboard getFor(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(CorePlugin.getMessages().getMessage(Messages.SCOREBOARD_HEADER).replaceAll("%SERVER%",
                CorePlugin.getMessages().getServerTag(getConfig().getServerType())));
        Team top = scoreboard.registerNewTeam("top");
        top.addEntry(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " ➛  " + ChatColor.BLUE);
        top.setPrefix("");
        top.setSuffix("");
        Team middle = scoreboard.registerNewTeam("middle");
        middle.addEntry(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " ➛  " + ChatColor.GREEN);
        middle.setPrefix("");
        middle.setSuffix("");
        Team bottom = scoreboard.registerNewTeam("bottom");
        bottom.addEntry(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " ➛  " + ChatColor.GOLD);
        bottom.setPrefix("");
        bottom.setSuffix("");

        objective.getScore(ChatColor.GRAY + " Race" + ChatColor.GRAY + ": ").setScore(9);
        objective.getScore(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " ➛  " + ChatColor.BLUE).setScore(8);
        objective.getScore(ChatColor.DARK_PURPLE + " ").setScore(7);
        objective.getScore(ChatColor.GRAY + " Class" + ChatColor.GRAY + ": ")
                .setScore(6);
        objective.getScore(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " ➛  " + ChatColor.GREEN).setScore(5);
        objective.getScore(ChatColor.YELLOW + "  ").setScore(4);
        objective.getScore(ChatColor.GRAY + " Perk Points" + ChatColor.GRAY + ": ").setScore(3);
        objective.getScore(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " ➛  " + ChatColor.GOLD).setScore(2);
        objective.getScore(ChatColor.DARK_GRAY + "  " + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "━━━━━━━━━━━━━━━━━━━━")
                .setScore(1);

        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(CorePlugin.getPlugin(), () -> {
            top.setSuffix(userModel.getRace().getRaceType().getRaceColor() + userModel.getRace().getRaceType().getName());
            middle.setSuffix(userModel.getRace().getRaceType().getRaceColor() + userModel.getRace().getName());
            bottom.setSuffix(GOLD + "" + userModel.getPerkPoints());
        }, 0 , 5);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CorePlugin.getPlugin(), () -> {
            Bukkit.getScheduler().cancelTask(getTaskId());
        }, 295);
        return scoreboard;
    }

    private ConfigModel getConfig() {
        return CorePlugin.getConfigg();
    }

    private String enumName(String s) {
        if (s.contains("_")) {
            String[] split = s.split("_");

            StringBuilder matName = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                matName.append(enumName(split[i]));

                if (i < split.length - 1) {
                    matName.append(" ");
                }
            }

            return matName.toString();
        }

        return s.toUpperCase().substring(0, 1) + s.substring(1).toLowerCase();
    }
}
