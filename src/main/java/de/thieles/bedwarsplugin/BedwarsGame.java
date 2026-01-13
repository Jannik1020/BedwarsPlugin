package de.thieles.bedwarsplugin;

public class BedwarsGame {
    private final BedwarsSetup setup = new BedwarsSetup();
    private boolean itemSpawnsPaused = false;

    public BedwarsSetup getSetup() {
        return setup;
    }

    public boolean isItemSpawnsPaused() {
        return itemSpawnsPaused;
    }

    public void setItemSpawnsPaused(boolean paused) {
        itemSpawnsPaused = paused;
    }
}
