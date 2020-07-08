package net.nighthawkempires.races.races;

public interface Race {

    String getName();

    RaceType getRaceType();

    int getTier();

    String[] getDescription();
}