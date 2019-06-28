package fr.inria.prophet4j.feature.S4RO;

import fr.inria.prophet4j.feature.Feature;
import fr.inria.prophet4j.feature.FeatureCross;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static fr.inria.prophet4j.feature.S4RO.S4ROFeature.*;
import static fr.inria.prophet4j.feature.S4RO.S4ROFeature.CrossType;

public class S4ROFeatureCross implements FeatureCross, Serializable {
    static final long serialVersionUID = 1L;
    private Integer id;
    private Double degree;
    private List<Feature> features;

    public S4ROFeatureCross(Integer id) {
        this(id, 1.0);
    }

    public S4ROFeatureCross(Integer id, Double degree) {
        this.id = id;
        this.degree = degree;
        this.features = new ArrayList<>();
        if (id >= FEATURE_BASE_3) {
            int tmp = id - FEATURE_BASE_3;
            int ordinal0 = tmp / VF_SIZE;
            int ordinal1 = tmp % VF_SIZE;
            this.features.add(AtomicFeature.values()[ordinal0]);
            this.features.add(ValueFeature.values()[ordinal1]);
        } else if (id >= FEATURE_BASE_2) {
            int tmp = id - FEATURE_BASE_2;
            int ordinal0 = tmp / (AF_SIZE * AF_SIZE);
            int ordinal1 = (tmp % (AF_SIZE * AF_SIZE)) / AF_SIZE;
            int ordinal2 = (tmp % (AF_SIZE * AF_SIZE)) % AF_SIZE;
            this.features.add(Position.values()[ordinal0]);
            this.features.add(AtomicFeature.values()[ordinal1]);
            this.features.add(AtomicFeature.values()[ordinal2]);
        } else if (id >= FEATURE_BASE_1) {
            int tmp = id - FEATURE_BASE_1;
            int ordinal0 = tmp / (AF_SIZE * RF_SIZE);
            int ordinal1 = (tmp % (AF_SIZE * RF_SIZE)) / RF_SIZE;
            int ordinal2 = (tmp % (AF_SIZE * RF_SIZE)) % RF_SIZE;
            this.features.add(Position.values()[ordinal0]);
            this.features.add(AtomicFeature.values()[ordinal1]);
            this.features.add(RepairFeature.values()[ordinal2]);
        } else if (id >= FEATURE_BASE_0) {
            int ordinal0 = id - FEATURE_BASE_0;
            this.features.add(RepairFeature.values()[ordinal0]);
        } else if (id >= FEATURE_SIZE_S4R) {
            int ordinal0 = id - FEATURE_SIZE_S4R;
            this.features.add(CodeFeature.values()[ordinal0]);
        }
    }

    public S4ROFeatureCross(CrossType crossType, List<Feature> features) {
        this(crossType, features, 1.0);
    }

    public S4ROFeatureCross(CrossType crossType, List<Feature> features, Double degree) {
        int ordinal0, ordinal1, ordinal2;
        switch (crossType) {
            case CF_CT:
                assert features.size() == 1;
                assert features.get(0) instanceof CodeFeature;
                this.id = FEATURE_SIZE_S4R + ((CodeFeature) features.get(0)).ordinal();
                break;
            case RF_CT:
                assert features.size() == 1;
                assert features.get(0) instanceof RepairFeature;
                ordinal0 = ((RepairFeature) features.get(0)).ordinal();
                this.id = FEATURE_BASE_0 + ordinal0;
                break;
            case POS_AF_RF_CT:
                assert features.size() == 3;
                assert features.get(0) instanceof Position;
                assert features.get(1) instanceof AtomicFeature;
                assert features.get(2) instanceof RepairFeature;
                ordinal0 = ((Position) features.get(0)).ordinal();
                ordinal1 = ((AtomicFeature) features.get(1)).ordinal();
                ordinal2 = ((RepairFeature) features.get(2)).ordinal();
                this.id = FEATURE_BASE_1 + ordinal0 * AF_SIZE * RF_SIZE + ordinal1 * RF_SIZE + ordinal2;
                break;
            case POS_AF_AF_CT:
                assert features.size() == 3;
                assert features.get(0) instanceof Position;
                assert features.get(1) instanceof AtomicFeature;
                assert features.get(2) instanceof AtomicFeature;
                ordinal0 = ((Position) features.get(0)).ordinal();
                ordinal1 = ((AtomicFeature) features.get(1)).ordinal();
                ordinal2 = ((AtomicFeature) features.get(2)).ordinal();
                this.id = FEATURE_BASE_2 + ordinal0 * AF_SIZE * AF_SIZE + ordinal1 * AF_SIZE + ordinal2;
                break;
            case AF_VF_CT:
                assert features.size() == 2;
                assert features.get(0) instanceof AtomicFeature;
                assert features.get(1) instanceof ValueFeature;
                ordinal0 = ((AtomicFeature) features.get(0)).ordinal();
                ordinal1 = ((ValueFeature) features.get(1)).ordinal();
                this.id = FEATURE_BASE_3 + ordinal0 * VF_SIZE + ordinal1;
                break;
        }
        this.degree = degree;
        this.features = features;
    }

    public Integer getId() {
        return id;
    }

    public Double getDegree() {
        return degree;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public boolean containFeature(Feature feature) {
        return features.contains(feature);
    }

    @Override
    public String toString() {
        return "FeatureCross: " + features;
    }
}