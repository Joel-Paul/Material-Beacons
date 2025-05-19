package issame.material_beacons;


import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static issame.material_beacons.Constants.LOG;
import static issame.material_beacons.Constants.MOD_ID;

@Mod(MOD_ID)
public class NeoForgeMod {
    public NeoForgeMod(IEventBus eventBus) {
        LOG.info("Initializing Material Beacons...");
        CommonMod.init();
        LOG.info("Material Beacons initialized!");
    }

    @EventBusSubscriber(modid = MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void disableBeaconGUI(PlayerInteractEvent.RightClickBlock event) {
            if (event.getLevel().getBlockState(event.getPos()).getBlock() == Blocks.BEACON) {
                event.setCanceled(true);
            }
        }
    }
}