package issame.material_beacons.config;

import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.List;

public class BeaconData {
    private final List<BlockOrTag> base;
    private final List<List<StatusEffectInstance>> powers;
    private final List<List<Double>> ranges;

    public BeaconData(BeaconConfig config) {
        base = config.getBaseTags();
        powers = config.getPowerEffects();
        ranges = config.getEffectRanges();
    }

    public List<BlockOrTag> getBase() {
        return base;
    }

    public List<List<StatusEffectInstance>> getPowers() {
        return powers;
    }

    public List<List<Double>> getRanges() {
        return ranges;
    }

    @Override
    public String toString() {
        return "BeaconData{" +
                "base=" + base +
                ", powers=" + powers +
                ", ranges=" + ranges +
                '}';
    }
}
