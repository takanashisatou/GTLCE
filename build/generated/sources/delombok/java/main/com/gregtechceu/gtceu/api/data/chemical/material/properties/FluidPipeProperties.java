package com.gregtechceu.gtceu.api.data.chemical.material.properties;

import com.gregtechceu.gtceu.api.capability.IPropertyFluidFilter;
import com.gregtechceu.gtceu.api.fluids.FluidState;
import com.gregtechceu.gtceu.api.fluids.attribute.FluidAttribute;
import com.gregtechceu.gtceu.api.fluids.attribute.FluidAttributes;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import java.util.Collection;
import java.util.Objects;

public class FluidPipeProperties implements IMaterialProperty<FluidPipeProperties>, IPropertyFluidFilter {
    /**
     * The maximum number of channels any fluid pipe can have
     */
    public static final int MAX_PIPE_CHANNELS = 9;
    private long throughput;
    private int channels;
    private int maxFluidTemperature;
    private boolean gasProof;
    private boolean cryoProof;
    private boolean plasmaProof;
    private final Object2BooleanMap<FluidAttribute> containmentPredicate = new Object2BooleanOpenHashMap<>();

    public FluidPipeProperties(int maxFluidTemperature, long throughput, boolean gasProof, boolean acidProof, boolean cryoProof, boolean plasmaProof, int channels) {
        this.maxFluidTemperature = maxFluidTemperature;
        this.throughput = throughput;
        this.gasProof = gasProof;
        if (acidProof) setCanContain(FluidAttributes.ACID, true);
        this.cryoProof = cryoProof;
        this.plasmaProof = plasmaProof;
        this.channels = channels;
    }

    /**
     * Default property constructor.
     */
    public FluidPipeProperties(int maxFluidTemperature, long throughput, boolean gasProof, boolean acidProof, boolean cryoProof, boolean plasmaProof) {
        this(maxFluidTemperature, throughput, gasProof, acidProof, cryoProof, plasmaProof, 1);
    }

    @Override
    public void verifyProperty(MaterialProperties properties) {
        if (!properties.hasProperty(PropertyKey.WOOD)) {
            properties.ensureSet(PropertyKey.INGOT, true);
        }
        if (properties.hasProperty(PropertyKey.ITEM_PIPE)) {
            throw new IllegalStateException("Material " + properties.getMaterial() + " has both Fluid and Item Pipe Property, which is not allowed!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FluidPipeProperties that)) return false;
        return maxFluidTemperature == that.maxFluidTemperature && throughput == that.throughput && gasProof == that.gasProof && channels == that.channels;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxFluidTemperature, throughput, gasProof, channels);
    }

    @Override
    public String toString() {
        return "FluidPipeProperties{" + "maxFluidTemperature=" + maxFluidTemperature + ", throughput=" + throughput + ", gasProof=" + gasProof + ", acidProof=" + isAcidProof() + ", cryoProof=" + cryoProof + ", plasmaProof=" + plasmaProof + ", channels=" + channels + '}';
    }

    public long getPlatformThroughput() {
        return getThroughput() * FluidHelper.getBucket() / 1000;
    }

    @Override
    public boolean canContain(@NotNull FluidState state) {
        return switch (state) {
            case LIQUID -> true;
            case GAS -> gasProof;
            case PLASMA -> plasmaProof;
        };
    }

    public boolean isAcidProof() {
        return canContain(FluidAttributes.ACID);
    }

    @Override
    public boolean canContain(@NotNull FluidAttribute attribute) {
        return containmentPredicate.getBoolean(attribute);
    }

    @Override
    public void setCanContain(@NotNull FluidAttribute attribute, boolean canContain) {
        containmentPredicate.put(attribute, canContain);
    }

    @Override
    @NotNull
    @UnmodifiableView
    public Collection<@NotNull FluidAttribute> getContainedAttributes() {
        return containmentPredicate.keySet();
    }

    public long getThroughput() {
        return this.throughput;
    }

    public void setThroughput(final long throughput) {
        this.throughput = throughput;
    }

    public int getChannels() {
        return this.channels;
    }

    public void setChannels(final int channels) {
        this.channels = channels;
    }

    public int getMaxFluidTemperature() {
        return this.maxFluidTemperature;
    }

    public void setMaxFluidTemperature(final int maxFluidTemperature) {
        this.maxFluidTemperature = maxFluidTemperature;
    }

    public boolean isGasProof() {
        return this.gasProof;
    }

    public void setGasProof(final boolean gasProof) {
        this.gasProof = gasProof;
    }

    public boolean isCryoProof() {
        return this.cryoProof;
    }

    public void setCryoProof(final boolean cryoProof) {
        this.cryoProof = cryoProof;
    }

    public boolean isPlasmaProof() {
        return this.plasmaProof;
    }

    public void setPlasmaProof(final boolean plasmaProof) {
        this.plasmaProof = plasmaProof;
    }
}
