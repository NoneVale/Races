package net.nighthawkempires.races.races;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.util.StringUtil;
import org.bukkit.ChatColor;

import java.util.List;

import static org.bukkit.ChatColor.*;

public enum RaceType {

    ANGEL,
    AURORA,
    DEMON,
    DWARF,
    ELF,
    HUMAN,
    ORC,
    VAMPIRE,
    VOID_WALKER,
    WEREWOLF;

    private static List<RaceType> holyRaces = Lists.newArrayList(ANGEL, AURORA, DWARF, ELF, HUMAN);

    public String getInitial() {
        return this.name().substring(0, 1).toUpperCase();
    }

    public String getName() {
        return StringUtil.beautify(this.name());
    }

    public ChatColor getRaceColor() {
        switch (this) {
            case ANGEL: return BLUE;
            case AURORA: return AQUA;
            case DEMON: return RED;
            case DWARF: return GOLD;
            case ELF: return GREEN;
            case ORC: return DARK_GREEN;
            case VAMPIRE: return DARK_RED;
            case VOID_WALKER: return DARK_GRAY;
            case WEREWOLF: return DARK_PURPLE;
            default: return GRAY;
        }
    }

    public double getBaseHealth() {
        switch (this) {
            case ANGEL:
            case ELF:
            case VAMPIRE:
            case VOID_WALKER:
                return 19.0;
            case AURORA:
            case DEMON:
            case ORC:
            case WEREWOLF:
                return 21.0;
            case DWARF: return 23.0;
            default: return 25.0;
        }
    }

    public String[] getRaceDescription() {
        switch (this) {
            case ANGEL: return new String[] {"Angel Description."};
            case AURORA: return new String[] {"Aurora Description."};
            case DEMON: return new String[] {"Demon Description."};
            case DWARF: return new String[] {"Dwarf Description."};
            case ELF: return new String[] {"Elf Description."};
            case ORC: return new String[] {"Orc Description."};
            case VAMPIRE: return new String[] {"Vampire Description."};
            case VOID_WALKER: return new String[] {"Void Walker Description."};
            case WEREWOLF: return new String[] {"Werewolf Description."};
            default: return new String[] {"Human Description."};
        }
    }

    public String getRaceDescriptionString() {
        StringBuilder description = new StringBuilder();
        for (int i = 0; i < getRaceDescription().length; i++) {
            description.append(getRaceDescription()[i]);

            if (i < getRaceDescription().length - 1) {
                description.append("\n");
            }
        }
        return description.toString();
    }

    public String[] getRaceInfection() {
        switch (this) {
            case ANGEL: return new String[] {"Angel Infection.."};
            case AURORA: return new String[] {"Aurora Infection."};
            case DEMON: return new String[] {"Demon Infection."};
            case DWARF: return new String[] {"Dwarf Infection."};
            case ELF: return new String[] {"Elf Infection."};
            case ORC: return new String[] {"Orc Infection."};
            case VAMPIRE: return new String[] {"Vampire Infection."};
            case VOID_WALKER: return new String[] {"Void Walker Infection."};
            case WEREWOLF: return new String[] {"Werewolf Infection."};
            default: return new String[] {"Human Infection."};
        }
    }

    public boolean isHolyRace() {
        return holyRaces.contains(this);
    }
}
