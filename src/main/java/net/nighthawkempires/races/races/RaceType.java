package net.nighthawkempires.races.races;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.util.StringUtil;
import org.bukkit.ChatColor;

import java.util.List;

import static org.bukkit.ChatColor.*;

public enum RaceType {

    CELESTIAL,
    DWARF,
    ELF,
    HUMAN,
    INFERNAL,
    LYCAN,
    ORC,
    TRITON,
    VAMPIRE,
    VOIDWALKER;

    private static List<RaceType> holyRaces = Lists.newArrayList(CELESTIAL, DWARF, ELF, HUMAN, TRITON);

    public String getInitial() {
        return this.name().substring(0, 1).toUpperCase();
    }

    public String getName() {
        return StringUtil.beautify(this.name());
    }

    public ChatColor getRaceColor() {
        switch (this) {
            case CELESTIAL: return YELLOW;
            case DWARF: return GOLD;
            case ELF: return GREEN;
            case INFERNAL: return RED;
            case LYCAN: return DARK_PURPLE;
            case ORC: return DARK_GREEN;
            case TRITON: return AQUA;
            case VAMPIRE: return DARK_RED;
            case VOIDWALKER: return DARK_GRAY;
            default: return GRAY;
        }
    }

    public double getBaseHealth() {
        switch (this) {
            case CELESTIAL:
            case ELF:
            case VAMPIRE:
            case VOIDWALKER:
                return 19.0;
            case INFERNAL:
            case LYCAN:
            case ORC:
            case TRITON:
                return 21.0;
            case DWARF: return 23.0;
            default: return 25.0;
        }
    }

    public String[] getRaceDescription() {
        switch (this) {
            case CELESTIAL: return new String[] {"Celestials are generally human-shaped, though some still retain horns or halos and all" +
                    " retain vestigial wings." +
                    "  They cannot fly, but they can glide, and the older Celestials are known to soar for miles without rest." +
                    "  Many Celestials still worship one god or another (or multiple), and they find that they can draw power from" +
                    " that god to fuel some of their more powerful abilities."};
            case DWARF: return new String[] {"Dwarf Description."};
            case ELF: return new String[] {"Elf Description."};
            case INFERNAL: return new String[] {"Similar to Celestials, Infernals are as ancient as the gods." +
                    "  During one of their (many) interdimensional wars, the “good” and “lawful” gods cast out the “evil” and “chaotic”" +
                    " gods from the Ether to the Nether." +
                    "  As they did, the outcast gods’ Celestials warped into Infernals." +
                    "  These beings, in their early stages, actually strongly resemble humans, but as they progress through the “castes”" +
                    " of Infernal biology, they become more and more like the images of their gods."};
            case LYCAN: return new String[] {"Lycans can form from any race when someone is infected with lycanthropy." +
                    "  It overpowers all their other racial traits, replacing them with wolfish abilities." +
                    "  All Lycans have the ability to shift into a wolfish form, which -- while not changing the structure or" +
                    " shape of their bodies -- gives them powerful abilities, as well as certain weaknesses." +
                    "  Unlike werewolves, Lycans can freely shift between their wolfish and base forms."};
            case ORC: return new String[] {"Orc Description."};
            case TRITON: return new String[] {"Aurora Description."};
            case VAMPIRE: return new String[] {"Vampire Description."};
            case VOIDWALKER: return new String[] {"Voidwalkers are among the most mysterious of the sentient races."
                    + "  They hail from the dangerous End dimension, where very few have stepped foot and where none have returned unchanged."
                    + "  Voidwalkers appear to separate into three “stages” of development, with some always remaining in one stage while" +
                    " others strive to progress even beyond the oldest documented stage."};
            default: return new String[] {"Humans are the most abundant race across the Overworld (which itself is the largest of the realms)," +
                    " and although they are often chided or dismissed for their perceived weaknesses, they are incredibly versatile, adaptable, and quick to learn." +
                    "  While they themselves are a powerful force, humanity’s versatility makes them a “base template” for players who seek to convert to other races." +
                    "  Relatively few members of these other races are human converts, though, and humanity itself is still very widespread."};
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
            case CELESTIAL: return new String[] {"CELESTIAL Infection.."};
            case DWARF: return new String[] {"Dwarf Infection."};
            case ELF: return new String[] {"Elf Infection."};
            case INFERNAL: return new String[] {"INFERNAL Infection."};
            case LYCAN: return new String[] {"Werewolf Infection."};
            case ORC: return new String[] {"Orc Infection."};
            case TRITON: return new String[] {"Aurora Infection."};
            case VAMPIRE: return new String[] {"Vampire Infection."};
            case VOIDWALKER: return new String[] {"Void Walker Infection."};
            default: return new String[] {"Human Infection."};
        }
    }

    public boolean isHolyRace() {
        return holyRaces.contains(this);
    }
}
