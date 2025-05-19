package issame.material_beacons.platform;

import issame.material_beacons.DatapackLoader;
import issame.material_beacons.config.BeaconData;
import issame.material_beacons.platform.services.IPlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.util.Map;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public Map<ResourceLocation, BeaconData> getBeaconData() {
        return DatapackLoader.getBeaconData();
    }
}