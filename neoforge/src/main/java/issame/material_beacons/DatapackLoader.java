package issame.material_beacons;

import com.google.gson.Gson;
import issame.material_beacons.config.BeaconConfig;
import issame.material_beacons.config.BeaconData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static issame.material_beacons.Constants.LOG;
import static issame.material_beacons.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class DatapackLoader {
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, BeaconData> beaconData = new HashMap<>();

    @SubscribeEvent
    public static void onReload(AddServerReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath(MOD_ID, "beacon"), (ResourceManagerReloadListener) resourceManager -> {
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
        });
    }

    public static Map<Identifier, BeaconData> getBeaconData() {
        return beaconData;
    }
}
