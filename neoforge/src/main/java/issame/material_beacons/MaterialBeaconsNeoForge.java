package issame.material_beacons;


import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(Constants.MOD_ID)
public class MaterialBeaconsNeoForge {
    public MaterialBeaconsNeoForge(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        MaterialBeaconsCommon.init();

    }

    @EventBusSubscriber(modid = Constants.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            if (event.getLevel().getBlockState(event.getPos()).getBlock() == Blocks.BEACON) {
                event.setCanceled(true);
            }
        }
    }
}