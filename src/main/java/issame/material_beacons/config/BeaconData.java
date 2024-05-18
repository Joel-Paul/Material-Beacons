package issame.material_beacons.config;

import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.List;

public class BeaconData {
    private final List<BlockOrTag> base;
    private final List<List<StatusEffectInstance>> powers;

    public BeaconData(BeaconConfig config) {
        base = config.getBaseTags();
        powers = config.getPowerEffects();

        for (BlockOrTag blockOrTag : base) {
            System.out.println("Base tag: " + blockOrTag);
        }
        for (List<StatusEffectInstance> list : powers) {
            for (StatusEffectInstance effect : list) {
                System.out.println("Effect: " + effect);
            }
        }
    }

    public List<BlockOrTag> getBase() {
        return base;
    }

    public List<List<StatusEffectInstance>> getPowers() {
        return powers;
    }
}
