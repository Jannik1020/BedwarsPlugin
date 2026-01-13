package de.thieles.bedwarsplugin.listeners;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import de.thieles.bedwarsplugin.BedwarsSetup;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class SpawnerTickListener implements Listener {
    private final BedwarsSetup setup;

    public SpawnerTickListener(BedwarsSetup setup) {

        this.setup = setup;
    }

    @EventHandler
    public void onServerTick(ServerTickStartEvent event) {
        setup.getSpawnerBlocks().forEach(spawnerBlock -> {
            if(event.getTickNumber() % spawnerBlock.tier().getTicksBetweenSpawn() == 0) {
                Item item = spawnerBlock.block().getWorld().dropItem(
                        spawnerBlock.block().getLocation().add(0,1,0),
                        new ItemStack(spawnerBlock.tier().getItem())
                );
                item.setVelocity(new Vector(0, 0, 0));
                item.setGravity(true);
            }
        });
    }
}
