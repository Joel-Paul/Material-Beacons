package issame.material_beacons;

import com.google.gson.Gson;
import issame.material_beacons.config.BeaconConfig;
import issame.material_beacons.config.BeaconData;
import issame.material_beacons.config.BlockOrTag;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static issame.material_beacons.MaterialBeacons.LOGGER;
import static issame.material_beacons.MaterialBeacons.MOD_ID;

public class DatapackLoader {
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, BeaconData> beaconData = new HashMap<>();

    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(MOD_ID, "beacons");
            }

            @Override
            public void reload(ResourceManager manager) {
                beaconData.clear();
                manager.findResources("beacons", path -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
                    try (InputStream stream = resource.getInputStream()) {
                        InputStreamReader reader = new InputStreamReader(stream);
                        BeaconConfig config = GSON.fromJson(reader, BeaconConfig.class);
                        beaconData.put(id, new BeaconData(config));
                    } catch (Exception e) {
                        LOGGER.warn("Failed to load beacon data from %s!".formatted(id), e);
                    }
                });
                LOGGER.info("Loaded %d beacons".formatted(beaconData.size()));
            }
        });
    }

    @Nullable
    public static List<BeaconData> findMatchingData(Block block) {
        List<BeaconData> matching = new LinkedList<>();
        for (BeaconData data : beaconData.values()) {
            for (BlockOrTag blockOrTag : data.getBases()) {
                if (blockOrTag.has(block)) {
                    matching.add(data);
                }
            }
        }
        return matching.isEmpty() ? null : matching;
    }
}
