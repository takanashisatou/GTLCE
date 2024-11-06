package com.gregtechceu.gtceu.common.pipelike.cable;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.pipenet.IRoutePath;
import com.gregtechceu.gtceu.common.blockentity.CableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EnergyRoutePath implements IRoutePath<IEnergyContainer> {
    private final CableBlockEntity targetPipe;
    private final BlockPos targetPipePos;
    private final Direction targetFacing;
    private final int distance;
    private final CableBlockEntity[] path;
    private final long maxLoss;

    public EnergyRoutePath(BlockPos targetPipePos, Direction targetFacing, CableBlockEntity[] path, int distance, long maxLoss) {
        this.targetPipe = path[path.length - 1];
        this.targetPipePos = targetPipePos;
        this.targetFacing = targetFacing;
        this.path = path;
        this.distance = distance;
        this.maxLoss = maxLoss;
    }

    @Nullable
    public IEnergyContainer getHandler(Level world) {
        return GTCapabilityHelper.getEnergyContainer(world, getTargetPipePos().relative(targetFacing), targetFacing.getOpposite());
    }

    public BlockPos getTargetPipePos() {
        return this.targetPipePos;
    }

    public Direction getTargetFacing() {
        return this.targetFacing;
    }

    public int getDistance() {
        return this.distance;
    }

    public CableBlockEntity[] getPath() {
        return this.path;
    }

    public long getMaxLoss() {
        return this.maxLoss;
    }
}
