package com.gregtechceu.gtceu.common.pipelike.optical;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IDataAccessHatch;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.capability.IOpticalDataAccessHatch;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.pipenet.IRoutePath;
import com.gregtechceu.gtceu.common.blockentity.OpticalPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpticalRoutePath implements IRoutePath<IOpticalComputationProvider> {
    private final OpticalPipeBlockEntity targetPipe;
    private final Direction targetFacing;
    private final int distance;

    public OpticalRoutePath(OpticalPipeBlockEntity targetPipe, Direction targetFacing, int distance) {
        this.targetPipe = targetPipe;
        this.targetFacing = targetFacing;
        this.distance = distance;
    }

    @Nullable
    public IOpticalDataAccessHatch getDataHatch() {
        IDataAccessHatch dataAccessHatch = getTargetCapability(GTCapability.CAPABILITY_DATA_ACCESS, targetPipe.getPipeLevel());
        return dataAccessHatch instanceof IOpticalDataAccessHatch opticalHatch ? opticalHatch : null;
    }

    @Nullable
    public IOpticalComputationProvider getComputationHatch() {
        return getTargetCapability(GTCapability.CAPABILITY_COMPUTATION_PROVIDER, targetPipe.getPipeLevel());
    }

    @Override
    @NotNull
    public BlockPos getTargetPipePos() {
        return targetPipe.getPipePos();
    }

    @Nullable
    @Override
    public IOpticalComputationProvider getHandler(Level world) {
        return GTCapabilityHelper.getOpticalComputationProvider(world, getTargetPipePos().relative(targetFacing), targetFacing.getOpposite());
    }

    public OpticalPipeBlockEntity getTargetPipe() {
        return this.targetPipe;
    }

    public Direction getTargetFacing() {
        return this.targetFacing;
    }

    public int getDistance() {
        return this.distance;
    }
}
