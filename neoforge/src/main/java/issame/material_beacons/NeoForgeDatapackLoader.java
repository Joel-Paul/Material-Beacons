package issame.material_beacons;

import com.google.gson.Gson;
import issame.material_beacons.config.BeaconConfig;
import issame.material_beacons.config.BeaconData;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static issame.material_beacons.Constants.LOG;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class NeoForgeDatapackLoader {
    private static final Gson GSON = new Gson();
    private static final Map<ResourceLocation, BeaconData> beaconData = new HashMap<>();

    @SubscribeEvent
    public static void onReload(AddServerReloadListenersEvent event) {
        event.addListener(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "beacon"), new SimpleJsonResourceReloadListener<>(BeaconConfig.CODEC, FileToIdConverter.json("beacon")) {
            @Override
            protected @NotNull Map<ResourceLocation, BeaconConfig> prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
                Map<ResourceLocation, BeaconConfig> configMap = new HashMap<>();
                resourceManager.listResources("beacon", path -> path.getPath().endsWith(".json"))
                        .forEach((resourceLocation, resource) -> {
                            try (InputStream stream = resource.open()) {
                                InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                                BeaconConfig config = GSON.fromJson(reader, BeaconConfig.class);
                                configMap.put(resourceLocation, config);
                            } catch (Exception e) {
                                LOG.warn("Failed to read JSON file {}:\n{}", resourceLocation, e);
                            }
                        });
                return configMap;
            }

            @Override
            protected void apply(@NotNull Map<ResourceLocation, BeaconConfig> configMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
                beaconData.clear();
                beaconData.putAll(configMap.entrySet().stream()
                        .collect(HashMap::new, (map, entry) -> {
                            ResourceLocation id = entry.getKey();
                            BeaconConfig config = entry.getValue();
                            BeaconData data = new BeaconData(config);
                            map.put(id, data);
                        }, HashMap::putAll));
            }
        });

    }

    public static Map<ResourceLocation, BeaconData> getBeaconData() {
        return beaconData;
    }
}
