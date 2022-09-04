package issame.material_beacons;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.recipe.Ingredient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class MBResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    private static final String DATA_PATH = "beacons";

    @Override
    public Identifier getFabricId() {
        return new Identifier(MaterialBeacons.MODID, MaterialBeacons.MODID);
    }

    @Override
    public void reload(ResourceManager manager) {
        // TODO: clear cache

        Map<Identifier, Resource> resources =
                manager.findResources(DATA_PATH, path -> path.getPath().endsWith(".json"));

        for (Identifier id : resources.keySet()) {
            if (manager.getResource(id).isEmpty()) {
                continue;
            }
            try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                // TODO: load json
                JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
                load(jsonObject);
            } catch (Exception e) {
                MaterialBeacons.LOGGER.error("Error occurred while loading data json " + id.toString(), e);
            }
        }
    }

    private void load(JsonObject jsonObject) {
        // TODO: load blocks
        // TODO: load effects
    }
}
