package issame.material_beacons.config;


import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class BeaconData {
    private final List<BlockOrTag> bases;
    private final List<List<MobEffectInstance>> powers;
    private final List<List<Double>> ranges;

    public BeaconData(BeaconConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Unable to parse beacon data");
        }
        bases = config.getBaseTags();
        powers = config.getPowerEffects();
        ranges = config.getEffectRanges();

        if (powers.size() != ranges.size()) {
            throw new IllegalArgumentException("Powers and ranges must have the same size!");
        }
        for (int i = 0; i < powers.size(); i++) {
            if (powers.get(i).size() != ranges.get(i).size()) {
                throw new IllegalArgumentException("Each power must have an associated range!");
            }
        }
    }

    public List<BlockOrTag> getBases() {
        return bases;
    }

    public List<List<MobEffectInstance>> getAllPowers() {
        return powers;
    }

    public int getMaxLevel() {
        return powers.size();
    }

    public List<MobEffectInstance> getPowers(int level) {
        return powers.get(Math.max(0, Math.min(level, getMaxLevel()) - 1));
    }

    public List<Double> getRanges(int level) {
        return ranges.get(Math.max(0, Math.min(level, getMaxLevel()) - 1));
    }

    @Override
    public String toString() {
        return "BeaconData{" +
                "bases=" + bases +
                ", powers=" + powers +
                ", ranges=" + ranges +
                '}';
    }
}
