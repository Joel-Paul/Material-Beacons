package issame.material_beacons.config;


import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

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

    public boolean has(Block other) {
        if (isBlock()) {
            return other.defaultBlockState().is(block);
        } else {
            return other.defaultBlockState().is(tag);
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
