package issame.material_beacons.config;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

public class BlockOrTag {
    private Block block;
    private TagKey<Block> tag;

    public BlockOrTag(Block block) {
        this.block = block;
    }

    public BlockOrTag(TagKey<Block> tag) {
        this.tag = tag;
    }

    public boolean isBlock() {
        return block != null;
    }

    public boolean isTag() {
        return tag != null;
    }

    public boolean has(Block other) {
        if (isBlock()) {
            return other == block;
        } else {
            RegistryEntry<Block> entry = Registries.BLOCK.getEntry(other);
            return entry != null && entry.isIn(tag);
        }
    }

    @Override
    public String toString() {
        if (isBlock()) {
            return block.toString();
        } else {
            return tag.toString();
        }
    }
}
