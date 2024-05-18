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

    @Override
    public String toString() {
        if (isBlock()) {
            return "Block: " + block;
        } else {
            return "Tag: " + tag;
        }
    }
}
