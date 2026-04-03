package issame.material_beacons;


import com.google.gson.Gson;
import issame.material_beacons.config.BeaconConfig;
import issame.material_beacons.config.BeaconData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static issame.material_beacons.Constants.LOG;
import static issame.material_beacons.Constants.MOD_ID;

public class CommonMod {
    private static final Gson GSON = new Gson();

    public static void init() {
    }

    public static ResourceManagerReloadListener getReloadListener(Map<Identifier, BeaconData> beaconData) {
        return resourceManager -> {
            beaconData.clear();
            resourceManager.listResources("beacon", identifier -> identifier.getNamespace().equals(MOD_ID)).forEach((id, resource) -> {
                try (InputStream stream = resource.open()) {
                    InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                    BeaconConfig config = GSON.fromJson(reader, BeaconConfig.class);
                    beaconData.put(id, new BeaconData(config));
                } catch (Exception e) {
                    LOG.warn("Failed to load beacon data from {}!\n{}", resource, e);
                }
            });
            LOG.info("Loaded {} beacon materials", beaconData.size());
        };
    }
}