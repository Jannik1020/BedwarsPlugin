package de.thieles.bedwarsplugin.commands;

import de.thieles.bedwarsplugin.BedwarsSetup;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

public class BedwarsAdminCommand implements BasicCommand {
    private final BedwarsSetup setup;

    public BedwarsAdminCommand(BedwarsSetup setup) {
        this.setup = setup;
    }

    public static final String CMD_NAME = "bwa";
    @Override
    public void execute(CommandSourceStack src, String[] args) {
        if(args.length == 1) {
            if (Objects.equals(args[0], "setbed")) {
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

                        BedwarsSetup.TeamBed teamBed = new BedwarsSetup.TeamBed(head, foot);
                        setup.addTeamBed(teamBed);

                    } else {
                        src.getSender().sendMessage("Target block is not a bed");
                    }
                }
            }
            else if(Objects.equals(args[0], "pause")) {
                setup.setPaused(true);
            }
            else if(Objects.equals(args[0], "resume")) {
                setup.setPaused(false);
            }
            else {
                printUsage(src);
            }
        }
        else if(args.length == 2) {
            if (Objects.equals(args[0], "setblock")) {
                if (src.getExecutor() instanceof Player player) {
                    Block block = player.getTargetBlockExact(5);
                    if (block == null) {
                        src.getSender().sendMessage("You must look at a target block");
                        return;
                    }
                    BlockData data = block.getBlockData();

                    BedwarsSetup.SpawnerTier.fromTag(args[1]).ifPresentOrElse(
                            tier -> setup.addSpawnerBlock(new BedwarsSetup.SpawnerBlock(block, tier)),
                            () -> src.getSender().sendMessage("Usage: /bwa setblock [iron | gold | diamond | emerald")

                    );

                    System.out.println(Arrays.toString(setup.getSpawnerBlocks().toArray()));
                } else {
                    printUsage(src);
                }
            }
        }
        else {
            printUsage(src);
        }
    }

    private void printUsage(CommandSourceStack src) {
        src.getSender().sendMessage("Usage: /"+CMD_NAME + " [setbed | setblock]");
    }
}
