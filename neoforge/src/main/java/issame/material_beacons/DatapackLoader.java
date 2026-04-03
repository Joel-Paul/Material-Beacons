package issame.material_beacons;

import issame.material_beacons.config.BeaconData;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.util.HashMap;
import java.util.Map;

import static issame.material_beacons.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class DatapackLoader {
    private static final Map<Identifier, BeaconData> beaconData = new HashMap<>();

    @SubscribeEvent
    public static void onReload(AddServerReloadListenersEvent event) {
        event.addListener(
                Identifier.fromNamespaceAndPath(MOD_ID, "beacon"),
                CommonMod.getReloadListener(beaconData)
        );
    }

    public static Map<Identifier, BeaconData> getBeaconData() {
        return beaconData;
    }
}
