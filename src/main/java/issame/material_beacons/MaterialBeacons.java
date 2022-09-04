package issame.material_beacons;

import issame.material_beacons.block.entity.MBBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class MaterialBeacons implements ModInitializer {
	public static final String MODID = "material_beacons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static BlockEntityType<MBBlockEntity> MATERIAL_BEACON_BLOCK_ENTITY;

	@Override
	public void onInitialize() {
		LOGGER.info("Initialising");

		MATERIAL_BEACON_BLOCK_ENTITY = Registry.register(
				Registry.BLOCK_ENTITY_TYPE,
				new Identifier(MODID, "beacon_block_entity"),
				FabricBlockEntityTypeBuilder.create(MBBlockEntity::new, Blocks.BEACON).build()
		);

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MBResourceReloadListener());
	}
}
