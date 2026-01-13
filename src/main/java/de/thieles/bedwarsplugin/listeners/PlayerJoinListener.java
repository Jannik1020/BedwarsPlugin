package de.thieles.bedwarsplugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Component mainTitle = Component.text("Willkommen", NamedTextColor.WHITE);
        final Component subtitle = Component.text("Bedwars-Server", NamedTextColor.GRAY);

        // Creates a simple title with the default values for fade-in, stay on screen and fade-out durations
        final Title title = Title.title(mainTitle, subtitle);

        // Send the title to your audience
        event.getPlayer().showTitle(title);
    }
}
