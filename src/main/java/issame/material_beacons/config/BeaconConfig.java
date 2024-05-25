package issame.material_beacons.config;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;

public class BeaconConfig {
    private List<String> bases;
    private List<List<EffectConfig>> powers;

    public List<String> getBases() {
        return bases;
    }

    public List<List<EffectConfig>> getPowers() {
        return powers;
    }

    public List<BlockOrTag> getBaseTags() {
        return bases.stream()
                .map(tag -> {
                    if (tag == null) {
                        System.out.println("Null tag in bases: " + bases);
                        return null;
                    } else if (tag.startsWith("#")) {
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
                                config.getDuration() * 20,
                                config.getAmplifier(),
                                true,
                                true))
                        .toList())
                .toList();
    }

    public List<List<Double>> getEffectRanges() {
        return powers.stream()
                .map(list -> list.stream()
                        .map(EffectConfig::getRange)
                        .toList())
                .toList();
    }

    public static class EffectConfig {
        private String effect;
        private int duration;
        private int amplifier;
        private double range;

        public String getEffect() {
            return effect;
        }

        public int getDuration() {
            return duration;
        }

        public int getAmplifier() {
            return amplifier;
        }

        public double getRange() {
            return range;
        }
    }
}
