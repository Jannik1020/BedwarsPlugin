package de.thieles.bedwarsplugin.listeners;

import de.thieles.bedwarsplugin.BedwarsSetup;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BedBreakListener implements Listener {
    private final BedwarsSetup setup;
    public BedBreakListener(BedwarsSetup setup) {
        this.setup = setup;
    }
    @EventHandler
    public void onBedBreak(BlockBreakEvent event) {
        if(setup.getTeamBeds().stream().anyMatch(bed -> bed.isBlockOfBed(event.getBlock()))) {

            final Component broadcastMessage = Component.text("Bed is broken");
            Bukkit.broadcast(broadcastMessage);

        }
    }
}
