package issame.material_beacons;

import issame.material_beacons.config.BeaconData;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

import java.util.HashMap;
import java.util.Map;

import static issame.material_beacons.Constants.MOD_ID;

public class DatapackLoader {
    private static final Map<Identifier, BeaconData> beaconData = new HashMap<>();

    public static void register() {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(
                Identifier.fromNamespaceAndPath(MOD_ID, "beacon"),
                CommonMod.getReloadListener(beaconData)
        );
    }

    public static Map<Identifier, BeaconData> getBeaconData() {
        return beaconData;
    }
}
