package issame.material_beacons.config;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;

public class BeaconConfig {
    private List<String> base;
    private List<List<EffectConfig>> powers;

    public List<String> getBase() {
        return base;
    }

    public List<List<EffectConfig>> getPowers() {
        return powers;
    }

    public List<BlockOrTag> getBaseTags() {
        return base.stream()
                .map(tag -> {
                    if (tag.startsWith("#")) {
                        return new BlockOrTag(TagKey.of(RegistryKeys.BLOCK, Identifier.tryParse(tag.substring(1))));
                    } else {
                        return new BlockOrTag(Registries.BLOCK.get(Identifier.tryParse(tag)));
                    }
                })
                .toList();
    }

    public List<List<StatusEffectInstance>> getPowerEffects() {
        return powers.stream()
                .map(list -> list.stream()
                        .map(config -> new StatusEffectInstance(
                                Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(config.getEffect())).orElse(null),
                                config.getDuration(),
                                config.getAmplifier()))
                        .toList())
                .toList();
    }

    public static class EffectConfig {
        private String effect;
        private int duration;
        private int amplifier;
        private int range;

        public String getEffect() {
            return effect;
        }

        public int getDuration() {
            return duration;
        }

        public int getAmplifier() {
            return amplifier;
        }

        public int getRange() {
            return range;
        }
    }
}
