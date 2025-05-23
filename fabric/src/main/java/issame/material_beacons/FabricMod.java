package issame.material_beacons;

import net.fabricmc.api.ModInitializer;

import static issame.material_beacons.Constants.LOG;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        LOG.info("Initializing Material Beacons...");
        CommonMod.init();
        DatapackLoader.register();
        LOG.info("Material Beacons initialized!");
    }
}
