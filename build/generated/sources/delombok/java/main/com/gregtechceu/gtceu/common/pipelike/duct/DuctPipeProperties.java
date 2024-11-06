package com.gregtechceu.gtceu.common.pipelike.duct;

import java.util.Objects;

public class DuctPipeProperties {
    /**
     * rate in stacks per sec
     */
    private float transferRate;

    public DuctPipeProperties(float transferRate) {
        this.transferRate = transferRate;
    }

    /**
     * Default property constructor.
     */
    public DuctPipeProperties() {
        this(0.25F);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DuctPipeProperties that = (DuctPipeProperties) o;
        return Float.compare(that.transferRate, transferRate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferRate);
    }

    @Override
    public String toString() {
        return "DuctPipeProperties{" + "transferRate=" + transferRate + '}';
    }

    /**
     * rate in stacks per sec
     */
    public float getTransferRate() {
        return this.transferRate;
    }

    /**
     * rate in stacks per sec
     */
    public void setTransferRate(final float transferRate) {
        this.transferRate = transferRate;
    }
}
