package de.thieles.bedwarsplugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BedwarsSetup {
    private final List<TeamBed> teamBeds = new ArrayList<>();
    private final List<SpawnerBlock> spawnerBlocks = new ArrayList<>();
    private boolean paused = false;

    public List<TeamBed> getTeamBeds() {
        return teamBeds;
    }
    public void addTeamBed(TeamBed bed) {
        teamBeds.add(bed);
    }

    public List<SpawnerBlock> getSpawnerBlocks() {
        return spawnerBlocks;
    }
    public void addSpawnerBlock(SpawnerBlock block) {
        spawnerBlocks.add(block);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public record TeamBed (Block head, Block foot, String teamIdent) {
        public boolean isBlockOfBed(Block block) {
            if(block.getBlockData() instanceof Bed bed) {
                return block.getLocation().equals(head.getLocation())
                        || block.getLocation().equals(foot.getLocation());
            }
            return false;
        }
    }

    public record SpawnerBlock(Block block, SpawnerTier tier) {

    }

    public enum SpawnerTier {
        IRON("iron", Material.IRON_INGOT, 40), // 2s
        GOLD("gold", Material.GOLD_INGOT, 160),  //8s
        DIAMOND("diamond", Material.DIAMOND, 160 * 4), //8s
        EMERALD("emerald", Material.EMERALD, 160 * 4 * 4); //16s

        private final String tagName;
        private final Material item;
        private final int ticksBetweenSpawn;

        SpawnerTier(String tagName, Material item, int tickBetweenSpawn) {
            this.tagName = tagName;
            this.item = item;
            this.ticksBetweenSpawn = tickBetweenSpawn;
        }

        public String getTagName() {
            return tagName;
        }

        public Material getItem() {
            return item;
        }

        public int getTicksBetweenSpawn() {
            return ticksBetweenSpawn;
        }

        public static Optional<SpawnerTier> fromTag(String tag) {
            return Arrays.stream(values()).filter(spawnerTier -> spawnerTier.tagName.equals(tag)).findFirst();
        }
    }
}
