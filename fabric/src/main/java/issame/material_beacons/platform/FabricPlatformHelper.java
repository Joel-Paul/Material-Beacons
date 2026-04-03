package issame.material_beacons.platform;

import issame.material_beacons.DatapackLoader;
import issame.material_beacons.config.BeaconData;
import issame.material_beacons.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Map<ResourceLocation, BeaconData> getBeaconData() {
        return DatapackLoader.getBeaconData();
    }
}
