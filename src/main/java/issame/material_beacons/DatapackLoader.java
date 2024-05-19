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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static issame.material_beacons.MaterialBeacons.MOD_ID;

public class DatapackLoader {
    private static final Gson GSON = new Gson();
    private static final HashSet<BeaconData> beaconData = new HashSet<>();

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
                        beaconData.add(new BeaconData(config));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public static HashSet<BeaconData> getBeaconData() {
        return beaconData;
    }

    @Nullable
    public static List<BeaconData> findMatchingData(Block block) {
        List<BeaconData> matching = new LinkedList<>();
        for (BeaconData data : beaconData) {
            for (BlockOrTag blockOrTag : data.getBase()) {
                if (blockOrTag.has(block)) {
                    matching.add(data);
                }
            }
        }
        return matching.isEmpty() ? null : matching;
    }
}
