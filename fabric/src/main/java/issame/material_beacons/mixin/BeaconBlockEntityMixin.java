package issame.material_beacons.mixin;

import issame.material_beacons.DatapackLoader;
import issame.material_beacons.config.BeaconData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static issame.material_beacons.MaterialBeacons.MAX_LAYER;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @Inject(at = @At("HEAD"), method = "updateBase", cancellable = true)
    private static void updateBase(Level level, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        BlockPos pos = new BlockPos(x, y, z);
        Block below = level.getBlockState(pos.below()).getBlock();
        List<BeaconData> bases = DatapackLoader.findMatchingData(below);
        if (bases == null) {
            cir.setReturnValue(0);
            return;
        }

        // Verify that the beacon pyramid is using a valid base
        int currentLevel = 0;
        for (int layer = 1; layer <= MAX_LAYER; layer++) {
            if (y - layer < level.getMinY()) {
                break;
            }
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
            currentLevel = layer;
            bases = nextBases;
        }

        if (currentLevel > 0 && !bases.isEmpty()) {
            // Apply the effects of a beacon base.
            BeaconData base = bases.getFirst();
            applyPlayerEffects(level, pos, currentLevel, base);
        }

        cir.setReturnValue(currentLevel);
    }

    @Inject(at = @At("HEAD"), method = "applyEffects", cancellable = true)
    private static void applyEffects(Level level, BlockPos pos, int beaconLevel, Holder<MobEffect> primaryEffect, Holder<MobEffect> secondaryEffect, CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    private static void applyPlayerEffects(Level level, BlockPos pos, int layer, BeaconData base) {
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
