package de.thieles.bedwarsplugin;

import de.thieles.bedwarsplugin.commands.BedwarsAdminCommand;
import de.thieles.bedwarsplugin.listeners.BedBreakListener;
import de.thieles.bedwarsplugin.listeners.PlayerJoinListener;
import de.thieles.bedwarsplugin.listeners.SpawnerTickListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BedwarsPlugin extends JavaPlugin {
    BedwarsGame game = new BedwarsGame();

    @Override
    public void onEnable() {
        getLogger().info("BedwarsPlugin enabled!");
        // Events
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new BedBreakListener(game.getSetup()), this);
        Bukkit.getPluginManager().registerEvents(new SpawnerTickListener(game), this);

        // Commands
        BedwarsAdminCommand bwaCmd = new BedwarsAdminCommand(game, this);
        registerCommand(BedwarsAdminCommand.CMD_NAME, bwaCmd);
    }

    @Override
    public void onDisable() {
        getLogger().info("BedwarsPlugin disabled!");
        saveBedwarsConfig();
    }

    private void loadConfig(BedwarsSetup setup) {
        FileConfiguration cfg = getConfig();
    }

    private void saveBed(Block bedFoot, Block bedHead, FileConfiguration cfg, String path) {
        Location locFoot = bedFoot.getLocation();
        Location locHead = bedHead.getLocation();

        cfg.set(path + ".world", locFoot.getWorld().getName());
        cfg.set(path + ".footX", locFoot.getBlockX());
        cfg.set(path + ".footY", locFoot.getBlockY());
        cfg.set(path + ".footZ", locFoot.getBlockZ());
        cfg.set(path + ".headX", locHead.getBlockX());
        cfg.set(path + ".headZ", locHead.getBlockZ());

        saveConfig();
    }

    private void saveBeds(List<BedwarsSetup.TeamBed> beds, FileConfiguration cfg) {
        beds.forEach( bed -> {
            saveBed(bed.foot(), bed.head(), cfg, "beds." + bed.teamIdent());
        });
    }

    private void saveSpawnerBlocks(List<BedwarsSetup.SpawnerBlock> blocks, FileConfiguration cfg) {
        String path = "spawners";
        List<Map<String, Object>> spawnerList = new ArrayList<>();

        for (BedwarsSetup.SpawnerBlock spawner : blocks) {
            Map<String, Object> data = new HashMap<>();
            Location loc = spawner.block().getLocation();
            data.put("world", loc.getWorld().getName());
            data.put("x", loc.getBlockX());
            data.put("y", loc.getBlockY());
            data.put("z", loc.getBlockZ());
            data.put("tier", spawner.tier().getTagName());
            spawnerList.add(data);
        }

        cfg.set(path, spawnerList);
        saveConfig();
    }

    public void saveBedwarsConfig() {
        FileConfiguration cfg = getConfig();
        saveBeds(game.getSetup().getTeamBeds(), cfg);
        saveSpawnerBlocks(game.getSetup().getSpawnerBlocks(), cfg);
    }
}
