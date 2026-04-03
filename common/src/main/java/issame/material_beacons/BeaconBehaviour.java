package issame.material_beacons;

import issame.material_beacons.config.BeaconData;
import issame.material_beacons.config.BlockOrTag;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static issame.material_beacons.Constants.MAX_LAYER;

public class BeaconBehaviour {
    public static int updateBeaconBase(Level level, int x, int y, int z, Map<ResourceLocation, BeaconData> beaconData) {
        BlockPos pos = new BlockPos(x, y, z);
        Block below = level.getBlockState(pos.below()).getBlock();
        List<BeaconData> bases = matchBases(below, beaconData);
        if (bases == null) {
            return 0;
        }

        // Verify that the beacon pyramid is using a valid base
        // Loops in a pyramid shape from top to bottom
        int currentLayer = 0;
        for (int layer = 1; layer <= MAX_LAYER; layer++) {
            if (y - layer < level.getMinY()) {
                break;
            }
            // Loops in a square shape for each layer
            List<BeaconData> nextBases = bases;
            for (int i = x - layer; (i <= x + layer) && !nextBases.isEmpty(); i++) {
                for (int j = z - layer; (j <= z + layer) && !nextBases.isEmpty(); j++) {
                    Block block = level.getBlockState(new BlockPos(i, y - layer, j)).getBlock();
                    nextBases = bases.stream()
                            .filter(data -> data.getBases().stream()
                                    .anyMatch(blockOrTag -> blockOrTag.has(block)))
                            .toList();
                }
            }
            if (nextBases.isEmpty()) {
                break;
            }
            currentLayer = layer;
            bases = nextBases;
        }

        if (currentLayer > 0 && !bases.isEmpty()) {
            // Apply the effects of a beacon base.
            BeaconData base = bases.getFirst();
            applyEffects(level, pos, currentLayer, base);
        }

        return currentLayer;
    }

    @Nullable
    private static List<BeaconData> matchBases(Block block, Map<ResourceLocation, BeaconData> beaconData) {
        List<BeaconData> matching = new LinkedList<>();
        for (BeaconData data : beaconData.values()) {
            for (BlockOrTag blockOrTag : data.getBases()) {
                if (blockOrTag.has(block)) {
                    matching.add(data);
                }
            }
        }
        return matching.isEmpty() ? null : matching;
    }

    private static void applyEffects(Level level, BlockPos pos, int layer, BeaconData base) {
        if (level.isClientSide() || base.getAllPowers().isEmpty()) {
            return;
        }

        List<MobEffectInstance> effects = base.getPowers(layer);
        List<Double> ranges = base.getRanges(layer);

        for (int i = 0; i < effects.size(); i++) {
            MobEffectInstance effect = effects.get(i);
            double range = ranges.get(i);

            AABB box = new AABB(pos).inflate(range).expandTowards(0, level.getHeight(), 0);
            List<Player> players = level.getEntitiesOfClass(Player.class, box);
            for (Player player : players) {
                player.addEffect(new MobEffectInstance(effect));
            }
        }
    }
}
