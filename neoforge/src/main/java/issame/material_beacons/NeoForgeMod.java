package issame.material_beacons;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import static issame.material_beacons.Constants.LOG;
import static issame.material_beacons.Constants.MOD_ID;

@Mod(MOD_ID)
public class NeoForgeMod {
    public NeoForgeMod(IEventBus eventBus) {
        LOG.info("Initializing Material Beacons...");
        CommonMod.init();
        LOG.info("Material Beacons initialized!");
    }
}