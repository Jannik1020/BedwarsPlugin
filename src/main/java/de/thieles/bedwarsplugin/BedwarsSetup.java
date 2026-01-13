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

    public record TeamBed (Block head, Block foot) {
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
        IRON("iron", Material.IRON_INGOT),
        GOLD("gold", Material.GOLD_INGOT),
        DIAMOND("diamond", Material.DIAMOND),
        EMERALD("emerald", Material.EMERALD);

        private final String tagName;
        private final Material item;

        SpawnerTier(String tagName, Material item) {
            this.tagName = tagName;
            this.item = item;
        }

        public String getTagName() {
            return tagName;
        }

        public Material getItem() {
            return item;
        }

        public static Optional<SpawnerTier> fromTag(String tag) {
            return Arrays.stream(values()).filter(spawnerTier -> spawnerTier.tagName.equals(tag)).findFirst();
        }
    }
}
