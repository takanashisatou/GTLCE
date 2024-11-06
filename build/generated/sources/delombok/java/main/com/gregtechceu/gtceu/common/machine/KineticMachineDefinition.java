package com.gregtechceu.gtceu.common.machine;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * @author KilaBash
 * @date 2023/3/31
 * @implNote KineticMachineDefinition
 */
public class KineticMachineDefinition extends MachineDefinition {
    public final boolean isSource;
    public final float torque;
    /**
     * false (default) - rotation axis = frontFacing clockWise axis
     * <br>
     * true - rotation axis = frontFacing axis
     */
    public boolean frontRotation;

    public KineticMachineDefinition(ResourceLocation id, boolean isSource, float torque) {
        super(id);
        this.isSource = isSource;
        this.torque = torque;
    }

    public boolean isSource() {
        return this.isSource;
    }

    public float getTorque() {
        return this.torque;
    }

    /**
     * false (default) - rotation axis = frontFacing clockWise axis
     * <br>
     * true - rotation axis = frontFacing axis
     */
    public boolean isFrontRotation() {
        return this.frontRotation;
    }

    /**
     * false (default) - rotation axis = frontFacing clockWise axis
     * <br>
     * true - rotation axis = frontFacing axis
     * @return {@code this}.
     */
    public KineticMachineDefinition setFrontRotation(final boolean frontRotation) {
        this.frontRotation = frontRotation;
        return this;
    }
}
