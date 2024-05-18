package issame.material_beacons.config;

import net.minecraft.block.Block;
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
            return other.getRegistryEntry().isIn(tag);
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
