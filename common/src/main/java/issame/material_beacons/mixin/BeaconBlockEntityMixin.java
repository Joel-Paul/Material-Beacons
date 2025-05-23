package issame.material_beacons.mixin;

import issame.material_beacons.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static issame.material_beacons.BeaconBehaviour.updateBeaconBase;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @Inject(at = @At("HEAD"), method = "updateBase", cancellable = true)
    private static void updateBase(Level level, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(updateBeaconBase(level, x, y, z, Services.PLATFORM.getBeaconData()));
    }

    @Inject(at = @At("HEAD"), method = "applyEffects", cancellable = true)
    private static void applyEffects(Level level, BlockPos pos, int beaconLevel, Holder<MobEffect> primaryEffect, Holder<MobEffect> secondaryEffect, CallbackInfo ci) {
        ci.cancel();
    }
}
