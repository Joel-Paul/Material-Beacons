package issame.material_beacons.config;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Objects;

import static issame.material_beacons.MaterialBeacons.LOGGER;

public record BeaconConfig(List<String> bases, List<List<EffectConfig>> powers) {

    public List<BlockOrTag> getBaseTags() {
        return bases.stream()
                .map(base -> {
                    if (base == null) {
                        LOGGER.warn("Null value found in bases: {}", bases);
                        return null;
                    } else if (base.startsWith("#")) {
                        TagKey<Block> tag = createTag(base.substring(1));
                        return tag == null ? null : new BlockOrTag(tag);
                    } else {
                        Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.tryParse(base));
                        return block.defaultBlockState().is(Blocks.AIR) ? null : new BlockOrTag(block);
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public List<List<MobEffectInstance>> getPowerEffects() {
        return powers.stream()
                .map(list -> list.stream()
                        .map(config -> {
                            MobEffect effect = BuiltInRegistries.MOB_EFFECT.getValue(ResourceLocation.tryParse(config.effect()));
                            if (effect == null || config.duration() == null || config.amplifier() == null) {
                                LOGGER.warn("Null value found in powers: {}", config);
                                return null;
                            }
                            return new MobEffectInstance(
                                    BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect),
                                    config.duration() * 20,
                                    config.amplifier(),
                                    true,
                                    true);
                        })
                        .filter(Objects::nonNull)
                        .toList())
                .toList();
    }

    public List<List<Double>> getEffectRanges() {
        return powers.stream()
                .map(list -> list.stream()
                        .map(config -> {
                            if (config.range() == null) {
                                LOGGER.warn("Null range found in powers: {}", config);
                                return null;
                            }
                            return config.range();
                        })
                        .filter(Objects::nonNull)
                        .toList())
                .toList();
    }

    private static TagKey<Block> createTag(String name) {
        ResourceLocation resourceLocation = ResourceLocation.tryParse(name);
        if (resourceLocation == null) {
            LOGGER.warn("Invalid tag name: {}", name);
            return null;
        }
        return TagKey.create(Registries.BLOCK, resourceLocation);
    }

    public record EffectConfig(String effect, Integer duration, Integer amplifier, Double range) {
    }
}
