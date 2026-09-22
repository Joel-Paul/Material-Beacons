package issame.material_beacons;

import issame.material_beacons.config.BeaconData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static issame.material_beacons.Constants.MAX_LAYER;

public class BeaconBehaviour {
    public static int updateBeaconBase(Level level, int x, int y, int z, Map<Identifier, BeaconData> beaconDataMap) {
        BlockPos beaconPos = new BlockPos(x, y, z);
        Block below = level.getBlockState(beaconPos.below()).getBlock();
        List<BeaconData> beaconDataList = filterBeaconData(below, beaconDataMap.values());
        if (beaconDataList.isEmpty()) {
            return 0;
        }

        int maxLayer = matchBeaconMaterial(level, x, y, z, beaconDataList);
        applyEffects(level, beaconPos, maxLayer, beaconDataList.getFirst());

        return maxLayer;
    }

    private static List<BeaconData> filterBeaconData(Block block, Collection<BeaconData> beaconDataCollection) {
        return beaconDataCollection.stream().filter(
                base -> base.getBases().stream().anyMatch(blockOrTag -> blockOrTag.has(block))
        ).toList();
    }

    private static int matchBeaconMaterial(Level level, int x, int y, int z, List<BeaconData> beaconDataList) {
        int maxLayer = 0;
        for (int layer = 1; layer <= MAX_LAYER; layer++) {
            if (y - layer < level.getMinY()) {
                break;
            }
            // Loops in a square shape for each layer
            List<BeaconData> newBases = beaconDataList;
            for (int i = x - layer; (i <= x + layer) && !newBases.isEmpty(); i++) {
                for (int j = z - layer; (j <= z + layer) && !newBases.isEmpty(); j++) {
                    Block block = level.getBlockState(new BlockPos(i, y - layer, j)).getBlock();
                    newBases = filterBeaconData(block, newBases);
                }
            }
            if (newBases.isEmpty()) {
                break;
            }
            maxLayer = layer;
            beaconDataList = newBases;
        }
        return maxLayer;
    }

    private static void applyEffects(Level level, BlockPos pos, int layer, BeaconData beaconData) {
        if (level.isClientSide() || layer <= 0 || beaconData.getAllPowers().isEmpty()) {
            return;
        }

        List<MobEffectInstance> effects = beaconData.getPowers(layer);
        List<Double> ranges = beaconData.getRanges(layer);

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
