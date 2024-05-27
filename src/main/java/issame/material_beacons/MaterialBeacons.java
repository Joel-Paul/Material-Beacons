package issame.material_beacons;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MaterialBeacons implements ModInitializer {
    public static final String MOD_ID = "material_beacons";
    public static final Logger LOGGER = LogManager.getLogger();
    public static int MAX_LEVEL = 4;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Material Beacons...");
        disableBeaconGUI();
        DatapackLoader.register();
        LOGGER.info("Material Beacons initialized!");
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
