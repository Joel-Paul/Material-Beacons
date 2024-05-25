package issame.material_beacons;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResult;

public class MaterialBeacons implements ModInitializer {
    public static final String MOD_ID = "material_beacons";
    public static int MAX_LEVEL = 4;

    @Override
    public void onInitialize() {
        disableBeaconGUI();
        DatapackLoader.register();
        System.out.println("Material Beacons initialized!");
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
