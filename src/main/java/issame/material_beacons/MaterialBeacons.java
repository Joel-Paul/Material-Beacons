package issame.material_beacons;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResult;

public class MaterialBeacons implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("Hello Fabric world!");
        disableBeaconGUI();
    }

    private void disableBeaconGUI() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.BEACON) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }
}
