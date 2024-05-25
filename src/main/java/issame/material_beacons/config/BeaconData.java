package issame.material_beacons.config;

import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.List;

public class BeaconData {
    private final List<BlockOrTag> bases;
    private final List<List<StatusEffectInstance>> powers;
    private final List<List<Double>> ranges;

    public BeaconData(BeaconConfig config) {
        bases = config.getBaseTags();
        powers = config.getPowerEffects();
        ranges = config.getEffectRanges();

        if (powers.size() != ranges.size()) {
            System.out.println("WARNING: Powers and ranges must have the same number of levels!");
        }
    }

    public List<BlockOrTag> getBases() {
        return bases;
    }

    public List<List<StatusEffectInstance>> getAllPowers() {
        return powers;
    }

    public List<List<Double>> getAllRanges() {
        return ranges;
    }

    public int getMaxLevel() {
        return powers.size();
    }

    public List<StatusEffectInstance> getPowers(int level) {
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
