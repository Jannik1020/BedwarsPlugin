package de.thieles.bedwarsplugin;

import de.thieles.bedwarsplugin.commands.BedwarsAdminCommand;
import de.thieles.bedwarsplugin.listeners.BedBreakListener;
import de.thieles.bedwarsplugin.listeners.PlayerJoinListener;
import de.thieles.bedwarsplugin.listeners.SpawnerTickListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

        loadBedwarsConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("BedwarsPlugin disabled!");
        saveBedwarsConfig();
    }

    public void loadBedwarsConfig() {
        loadBeds();
        loadSpawnerBlocks();
    }



    private void loadBeds() {
        FileConfiguration cfg = getConfig();
        ConfigurationSection beds = cfg.getConfigurationSection("beds");

        if(beds == null) {
            getLogger().info("Could not load config section 'beds'");
            return;
        }

        Set<String> teamIdents = beds.getKeys(false);
        for(String s: teamIdents) {
            ConfigurationSection team = beds.getConfigurationSection(s);
            if (team == null) continue;

            String worldName = team.getString("world");
            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                getLogger().warning("World not found for team " + team);
                continue;
            }

            int fx = team.getInt("foot.x");
            int fy = team.getInt("foot.y");
            int fz = team.getInt("foot.z");

            int hx = team.getInt("head.x");
            int hy = team.getInt("head.y");
            int hz = team.getInt("head.z");

            Block foot = world.getBlockAt(fx, fy, fz);
            Block head = world.getBlockAt(hx, hy, hz);

            BedwarsSetup.TeamBed bed = new BedwarsSetup.TeamBed(head, foot, s);
            game.getSetup().addTeamBed(bed);
        }
    }
    private void saveBed(Block bedFoot, Block bedHead, FileConfiguration cfg, String path) {
        Location locFoot = bedFoot.getLocation();
        Location locHead = bedHead.getLocation();

        cfg.set(path + ".world", locFoot.getWorld().getName());

        cfg.set(path + ".foot.x", locFoot.getBlockX());
        cfg.set(path + ".foot.y", locFoot.getBlockY());
        cfg.set(path + ".foot.z", locFoot.getBlockZ());

        cfg.set(path + ".head.x", locHead.getBlockX());
        cfg.set(path + ".head.y", locHead.getBlockY());
        cfg.set(path + ".head.z", locHead.getBlockZ());
        saveConfig();
    }

    private void saveBeds(List<BedwarsSetup.TeamBed> beds, FileConfiguration cfg) {
        beds.forEach( bed -> {
            saveBed(bed.foot(), bed.head(), cfg, "beds." + bed.teamIdent());
        });
    }
    private void loadSpawnerBlocks() {
        FileConfiguration cfg = getConfig();
        @NotNull List<Map<?, ?>> spawners = cfg.getMapList("spawners");
        for(Map<?,?> spawnerBlock: spawners){
            String worldName = (String) spawnerBlock.get("world");
            int x = (int) spawnerBlock.get("x");
            int y = (int) spawnerBlock.get("y");
            int z = (int) spawnerBlock.get("z");
            String tierTag = (String) spawnerBlock.get("tier");

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                getLogger().warning("World not found: " + worldName);
                continue;
            }

            Optional<BedwarsSetup.SpawnerTier> tier = BedwarsSetup.SpawnerTier.fromTag(tierTag);
            if(tier.isEmpty()) {
                getLogger().warning("Invalid tier at " + worldName + " " + x + " " + y + " " + z);
                continue;
            }

            Location location = new Location(world, x, y, z);
            BedwarsSetup.SpawnerBlock spawner = new BedwarsSetup.SpawnerBlock(location.getBlock(), tier.get());
            game.getSetup().addSpawnerBlock(spawner);
        }
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
