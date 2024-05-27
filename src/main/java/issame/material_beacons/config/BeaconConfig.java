package issame.material_beacons.config;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;

import static issame.material_beacons.MaterialBeacons.LOGGER;

public record BeaconConfig(List<String> bases, List<List<EffectConfig>> powers) {

    public List<BlockOrTag> getBaseTags() {
        return bases.stream()
                .map(tag -> {
                    if (tag == null) {
                        LOGGER.warn("Null value found in bases: {}", bases);
                        return null;
                    } else if (tag.startsWith("#")) {
                        return new BlockOrTag(TagKey.of(RegistryKeys.BLOCK, Identifier.tryParse(tag.substring(1))));
                    } else {
                        return new BlockOrTag(Registries.BLOCK.get(Identifier.tryParse(tag)));
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public List<List<StatusEffectInstance>> getPowerEffects() {
        return powers.stream()
                .map(list -> list.stream()
                        .map(config -> {
                            if (config.effect() == null || config.duration() == null || config.amplifier() == null) {
                                LOGGER.warn("Null value found in powers: {}", config);
                                return null;
                            }
                            return new StatusEffectInstance(
                                    Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(config.effect())).orElse(null),
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

    public record EffectConfig(String effect, Integer duration, Integer amplifier, Double range) {
    }
}
