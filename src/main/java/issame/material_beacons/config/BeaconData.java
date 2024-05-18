package issame.material_beacons.config;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class BeaconData {
    private final List<TagKey<Block>> base;
    private final List<List<StatusEffectInstance>> powers;

    public BeaconData(BeaconConfig config) {
        base = config.getBaseTags();
        powers = config.getPowerEffects();

        for (TagKey<Block> tag : base) {
            System.out.println("Base tag: " + tag);
        }
        for (List<StatusEffectInstance> list : powers) {
            for (StatusEffectInstance effect : list) {
                System.out.println("Effect: " + effect);
            }
        }
    }

    public List<TagKey<Block>> getBase() {
        return base;
    }

    public List<List<StatusEffectInstance>> getPowers() {
        return powers;
    }
}
