package issame.material_beacons.mixin;

import issame.material_beacons.DatapackLoader;
import issame.material_beacons.config.BeaconData;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @Inject(at = @At("HEAD"), method = "updateLevel", cancellable = true)
    private static void updateLevel(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        Block below = world.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
        List<BeaconData> bases = DatapackLoader.findMatchingData(below);
        if (bases == null) {
            cir.setReturnValue(0);
            return;
        }

        int currentLevel = 0;
        for (int level = 1; level <= 8; level++) {
            if (y - level < world.getBottomY()) {
                break;
            }
            List<BeaconData> nextBases = bases;
            for (int i = x - level; (i <= x + level) && !nextBases.isEmpty(); i++) {
                for (int j = z - level; (j <= z + level) && !nextBases.isEmpty(); j++) {
                    Block block = world.getBlockState(new BlockPos(i, y - level, j)).getBlock();
                    nextBases = bases.stream()
                            .filter(data -> data.getBase().stream()
                                    .anyMatch(blockOrTag -> blockOrTag.has(block)))
                            .toList();
                }
            }
            if (nextBases.isEmpty()) {
                break;
            }
            currentLevel = level;
            bases = nextBases;
        }

        if (currentLevel > 0) {
            BeaconData base = bases.get(0);
        }

        cir.setReturnValue(currentLevel);
    }
}
