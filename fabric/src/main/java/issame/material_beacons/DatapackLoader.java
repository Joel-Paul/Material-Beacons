package issame.material_beacons;

import com.google.gson.Gson;
import issame.material_beacons.config.BeaconConfig;
import issame.material_beacons.config.BeaconData;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static issame.material_beacons.Constants.LOG;
import static issame.material_beacons.Constants.MOD_ID;

public class DatapackLoader {
    private static final Gson GSON = new Gson();
    private static final Map<ResourceLocation, BeaconData> beaconData = new HashMap<>();

    public static void register() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return ResourceLocation.fromNamespaceAndPath(MOD_ID, "beacon");
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                beaconData.clear();
                resourceManager.listResources("beacon", path -> path.getPath().endsWith(".json")).forEach((resourceLocation, resource) -> {
                    try (InputStream stream = resource.open()) {
                        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                        BeaconConfig config = GSON.fromJson(reader, BeaconConfig.class);
                        beaconData.put(resourceLocation, new BeaconData(config));
                    } catch (Exception e) {
                        LOG.warn("Failed to load beacon data from {}!\n{}", resourceLocation, e);
                    }
                });
                LOG.info("Loaded {} beacon materials", beaconData.size());
            }
        });
    }

    public static Map<ResourceLocation, BeaconData> getBeaconData() {
        return beaconData;
    }
}
