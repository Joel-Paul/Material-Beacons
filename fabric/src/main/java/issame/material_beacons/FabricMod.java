package issame.material_beacons;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Blocks;

import static issame.material_beacons.Constants.LOG;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        LOG.info("Initializing Material Beacons...");
        disableBeaconGUI();
        FabricDatapackLoader.register();
        LOG.info("Material Beacons initialized!");
    }

    private void disableBeaconGUI() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            if (level.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.BEACON) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
    }
}
