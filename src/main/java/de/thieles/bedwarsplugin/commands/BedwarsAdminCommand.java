package de.thieles.bedwarsplugin.commands;

import de.thieles.bedwarsplugin.BedwarsGame;
import de.thieles.bedwarsplugin.BedwarsPlugin;
import de.thieles.bedwarsplugin.BedwarsSetup;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Objects;

public class BedwarsAdminCommand implements BasicCommand {
    private final BedwarsGame game;
    private final BedwarsPlugin plugin;

    public BedwarsAdminCommand(BedwarsGame game, BedwarsPlugin plugin) {
        this.game = game;
        this.plugin = plugin;
    }

    public static final String CMD_NAME = "bwa";
    @Override
    public void execute(@NonNull CommandSourceStack src, String[] args) {
        if(args.length == 1) {
            if(Objects.equals(args[0], "pause")) {
                game.setItemSpawnsPaused(true);
            }
            else if(Objects.equals(args[0], "resume")) {
                game.setItemSpawnsPaused(false);
            }
            else if(Objects.equals(args[0], "save")) {
                plugin.saveBedwarsConfig();
            }
            else if(Objects.equals(args[0], "load")) {
                plugin.loadBedwarsConfig();
            }
        } else if(args.length == 2) {
            if (Objects.equals(args[0], "setblock")) {
                if (src.getExecutor() instanceof Player player) {
                    Block block = player.getTargetBlockExact(5);
                    if (block == null) {
                        src.getSender().sendMessage("You must look at a target block");
                        return;
                    }
                    BlockData data = block.getBlockData();

                    BedwarsSetup.SpawnerTier.fromTag(args[1]).ifPresentOrElse(
                            tier -> game.getSetup().addSpawnerBlock(new BedwarsSetup.SpawnerBlock(block, tier)),
                            () -> src.getSender().sendMessage("Usage: /bwa setblock [iron | gold | diamond | emerald]")

                    );

                    System.out.println(Arrays.toString(game.getSetup().getSpawnerBlocks().toArray()));
                } else {
                    printUsage(src);
                }
            } else if (Objects.equals(args[0], "setbed")) {
                if (src.getExecutor() instanceof Player player) {
                    Block block = player.getTargetBlockExact(5);
                    if (block == null) {
                        src.getSender().sendMessage("You must look at a target block");
                        return;
                    }
                    BlockData data = block.getBlockData();

                    if (data instanceof Bed bed) {
                        Block head = bed.getPart() == Bed.Part.HEAD
                                ? block
                                : block.getRelative(bed.getFacing());

                        Block foot = bed.getPart() == Bed.Part.FOOT
                                ? block
                                : block.getRelative(bed.getFacing().getOppositeFace());

                        BedwarsSetup.TeamBed teamBed = new BedwarsSetup.TeamBed(head, foot, args[1]);
                        game.getSetup().addTeamBed(teamBed);

                    } else {
                        src.getSender().sendMessage("Target block is not a bed");
                    }
                }
            }
            else {
                printUsage(src);
            }
        }
        else {
            printUsage(src);
        }
    }

    private void printUsage(CommandSourceStack src) {
        src.getSender().sendMessage("Usage: /"+CMD_NAME + " [ setbed <team> | setblock <tier> | pause | resume | save ]");
    }
}
