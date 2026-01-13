package de.thieles.bedwarsplugin;

import de.thieles.bedwarsplugin.commands.BedwarsAdminCommand;
import de.thieles.bedwarsplugin.listeners.BedBreakListener;
import de.thieles.bedwarsplugin.listeners.PlayerJoinListener;
import de.thieles.bedwarsplugin.listeners.SpawnerTickListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
        BedwarsAdminCommand bwaCmd = new BedwarsAdminCommand(game);
        registerCommand(BedwarsAdminCommand.CMD_NAME, bwaCmd);
    }

    @Override
    public void onDisable() {
        getLogger().info("BedwarsPlugin disabled!");
    }
}
