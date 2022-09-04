package issame.material_beacons.mixin;

import issame.material_beacons.block.entity.MBBlockEntity;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlock.class)
public abstract class BeaconBlockMixin extends BlockWithEntity implements Stainable {
    protected BeaconBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "createBlockEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/entity/BlockEntity;",
            at = @At("HEAD"), cancellable = true)
    private void createBlockEntity(BlockPos pos, BlockState state, CallbackInfoReturnable<BlockEntity> cir) {
        cir.setReturnValue(new MBBlockEntity(pos, state));
    }
}
