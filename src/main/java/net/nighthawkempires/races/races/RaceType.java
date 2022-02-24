package net.nighthawkempires.races.races;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.util.StringUtil;
import net.nighthawkempires.races.recipes.*;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static org.bukkit.ChatColor.*;

public enum RaceType {

    ANGEL,
    DWARF,
    DEMON,
    //ELF,
    HUMAN,
    //LYCAN,
    //ORC,
    //TRITON,
    //VAMPIRE,
    VOIDWALKER;

    private static List<RaceType> holyRaces = Lists.newArrayList(ANGEL, DWARF, HUMAN);

    public String getInitial() {
        return this.name().substring(0, 1).toUpperCase();
    }

    public String getName() {
        return StringUtil.beautify(this.name());
    }

    public ChatColor getRaceColor() {
        switch (this) {
            case ANGEL: return YELLOW;
            case DWARF: return GOLD;
            //case ELF: return GREEN;
            case DEMON: return RED;
            //case LYCAN: return DARK_PURPLE;
            //case ORC: return DARK_GREEN;
            //case TRITON: return AQUA;
            //case VAMPIRE: return DARK_RED;
            case VOIDWALKER: return DARK_GRAY;
            default: return GRAY;
        }
    }

    public double getBaseHealth() {
        switch (this) {
            case ANGEL:
            //case ELF:
            //case VAMPIRE:
            case VOIDWALKER:
                return 19.0;
            case DEMON:
            //case LYCAN:
            //case ORC:
            //case TRITON:
                return 21.0;
            case DWARF: return 23.0;
            default: return 25.0;
        }
    }

    public String[] getRaceDescription() {
        switch (this) {
            case ANGEL: return new String[] {"Angels are generally human-shaped, though some still retain horns or halos and all" +
                    " retain vestigial wings." +
                    "  They cannot fly, but they can glide, and the older Angels are known to soar for miles without rest." +
                    "  Many Angels still worship one god or another (or multiple), and they find that they can draw power from" +
                    " that god to fuel some of their more powerful abilities."};
            case DWARF: return new String[] {"Dwarves are a shy race, spending most of their lives underground amid the sounds and heat" +
                    " of unearthly dwarven forges. Their physicality is as varied as the humans they descended from, but the generations" +
                    " of living underground have turned many of their eyes to pale or even grayscale shades of what used to be vibrant color," +
                    " and many are stockier than humans on average."};
            /*case ELF: return new String[] {"Elves, like Dwarves, are shy people, and they typically live in communal societies." +
                    " They most often live in the forest, high in the trees, but some have been known to frequent the lush" +
                    " caves or even coral reefs at times."};
            */case DEMON: return new String[] {"Similar to Celestials, Infernals are as ancient as the gods." +
                    "  During one of their (many) interdimensional wars, the “good” and “lawful” gods cast out the “evil” and “chaotic”" +
                    " gods from the Ether to the Nether." +
                    "  As they did, the outcast gods’ Celestials warped into Infernals." +
                    "  These beings, in their early stages, actually strongly resemble humans, but as they progress through the “castes”" +
                    " of Infernal biology, they become more and more like the images of their gods."};
            /*case LYCAN: return new String[] {"Lycans can form from any race when someone is infected with lycanthropy." +
                    "  It overpowers all their other racial traits, replacing them with wolfish abilities." +
                    "  All Lycans have the ability to shift into a wolfish form, which -- while not changing the structure or" +
                    " shape of their bodies -- gives them powerful abilities, as well as certain weaknesses." +
                    "  Unlike werewolves, Lycans can freely shift between their wolfish and base forms."};
            case ORC: return new String[] {"If the four human races are meant to reflect the four elements" +
                    " -- which is a fairly prominent theory -- then the Orcs are the fire and fury of the sun." +
                    " Often drawn up as nothing more than savages, they are ostracized and often excluded from “civilized” society," +
                    " expected to be violent and combative; but in reality, they live fairly peaceful lives if left to their own devices," +
                    " and most value the quiet lulls of peacetime more than the dissonance of war."};
            case TRITON: return new String[] {"Tritons are an amphibious race, and most often have blue or green skin that acts as a" +
                    " form of camouflage when they’re underwater." +
                    "  They have webbed feet and fingers that allow them to have an enhanced swimming speed, and fins that give them" +
                    " enhanced maneuverability."};
            case VAMPIRE: return new String[] {"Vampires are stalkers of the night, out of necessity more than choice." +
                    "  The sun burns them if they stay out for too long, but the darkness of night soothes their skin like water." +
                    " Unlike traditional vampires, these don’t subsist on blood or any material thing; instead, they feed on souls and memories. "};
            */case VOIDWALKER: return new String[] {"Voidwalkers are among the most mysterious of the sentient races."
                    + "  They hail from the dangerous End dimension, where very few have stepped foot and where none have returned unchanged."
                    + "  Voidwalkers appear to separate into three “stages” of development, with some always remaining in one stage while" +
                    " others strive to progress even beyond the oldest documented stage."};
            default: return new String[] {"Humans are the most abundant race across the Overworld (which itself is the largest of the realms)," +
                    " and although they are often chided or dismissed for their perceived weaknesses, they are incredibly versatile, adaptable, and quick to learn." +
                    "  While they themselves are a powerful force, humanity’s versatility makes them a “base template” for those seeking to convert to other races." +
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
            case ANGEL: return new String[] {"While above block level 128, die by fall damage while holding a Tear of God in hand."};
            case DWARF: return new String[] {"While below block level 0, die by lava with a Miner's Trophy in hand."};
            //case ELF: return new String[] {"Elf Infection."};
            case DEMON: return new String[] {"While in the Basalt Deltas in the Nether, die by lava with an Infernal Heart in hand."};
            /*case LYCAN: return new String[] {"Using an Empty Wolf Essence Bottle, collect the essence from a wolf." +
                    "  Once the Wolf's Essence is collected, consume it during a full moon while in a forest."};
            *case ORC: return new String[] {"Orc Infection."};
            case TRITON: return new String[] {"While in the Ocean, drown at least 16 blocks below sea level with a Sea Pearl in hand."};
            case VAMPIRE: return new String[] {"Vampire Infection."};
            */case VOIDWALKER: return new String[] {"While in the Outer End Islands, jump into the void holding the Void Spirit."};
            default: return new String[] {"After crafting the Curing Elixir, consume it while in the light."};
        }
    }

    public String getInfectionString() {
        StringBuilder infection = new StringBuilder();
        for (int i = 0; i < getRaceInfection().length; i++) {
            infection.append(getRaceInfection()[i]);

            if (i < getRaceInfection().length - 1) {
                infection.append("\n");
            }
        }
        return infection.toString();
    }

    public ItemStack getRaceItem() {
        switch (this) {
            case ANGEL:
                return AngelRecipes.itemTearOfGod();
            case DWARF:
                return DwarfRecipes.itemMinersTrophy();
            case DEMON:
                return DemonRecipes.itemInfernalHeart();
            case VOIDWALKER:
                return VoidwalkerRecipes.itemVoidForgedPendant();
            default:
                return HumanRecipes.itemElixirOfLife();
        }
    }

    public boolean isHolyRace() {
        return holyRaces.contains(this);
    }

    public static RaceType getRace(String name) {
        for (RaceType raceType : RaceType.values()) {
            if (raceType.getName().equalsIgnoreCase(name)) {
                return raceType;
            }
        }
        return null;
    }
}