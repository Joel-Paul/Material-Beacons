package issame.material_beacons.mixin;

import issame.material_beacons.DatapackLoader;
import issame.material_beacons.config.BeaconData;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static issame.material_beacons.MaterialBeacons.MAX_LEVEL;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @Inject(at = @At("HEAD"), method = "updateLevel", cancellable = true)
    private static void updateLevel(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        BlockPos pos = new BlockPos(x, y, z);
        Block below = world.getBlockState(pos.down()).getBlock();
        List<BeaconData> bases = DatapackLoader.findMatchingData(below);
        if (bases == null) {
            cir.setReturnValue(0);
            return;
        }

        // Verify that the beacon pyramid is using a valid base
        int currentLevel = 0;
        for (int level = 1; level <= MAX_LEVEL; level++) {
            if (y - level < world.getBottomY()) {
                break;
            }
            List<BeaconData> nextBases = bases;
            for (int i = x - level; (i <= x + level) && !nextBases.isEmpty(); i++) {
                for (int j = z - level; (j <= z + level) && !nextBases.isEmpty(); j++) {
                    Block block = world.getBlockState(new BlockPos(i, y - level, j)).getBlock();
                    nextBases = bases.stream()
                            .filter(data -> data.getBases().stream()
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

        if (currentLevel > 0 && !bases.isEmpty()) {
            // Apply the effects of a beacon base.
            BeaconData base = bases.get(0);
            applyPlayerEffects(world, pos, currentLevel, base);
        }

        cir.setReturnValue(currentLevel);
    }

    @Inject(at = @At("HEAD"), method = "applyPlayerEffects", cancellable = true)
    private static void applyPlayerEffects(World world, BlockPos pos, int beaconLevel, StatusEffect primaryEffect, StatusEffect secondaryEffect, CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    private static void applyPlayerEffects(World world, BlockPos pos, int level, BeaconData base) {
        if (world.isClient || base.getAllPowers().isEmpty()) {
            return;
        }

        List<StatusEffectInstance> effects = base.getPowers(level);
        List<Double> ranges = base.getRanges(level);

        for (int i = 0; i < effects.size(); i++) {
            StatusEffectInstance effect = effects.get(i);
            double range = ranges.get(i);

            Box box = new Box(pos).expand(range).stretch(0, world.getHeight(), 0);
            List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);
            for (PlayerEntity player : players) {
                player.addStatusEffect(new StatusEffectInstance(effect));
            }
        }
    }
}
