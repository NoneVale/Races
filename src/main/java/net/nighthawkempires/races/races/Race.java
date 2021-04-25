package net.nighthawkempires.races.races;

public interface Race {

    String getName();

    RaceType getRaceType();

    int getTier();

    String[] getDescription();

    default String getDescriptionString() {
        StringBuilder description = new StringBuilder();
        for (int i = 0; i < getDescription().length; i++) {
            description.append(getDescription()[i]);

            if (i < getDescription().length - 1) {
                description.append("\n");
            }
        }
        return description.toString();
    }
}