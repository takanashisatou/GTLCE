package com.gregtechceu.gtceu.common.pipelike.duct;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IHazardParticleContainer;
import com.gregtechceu.gtceu.api.pipenet.IRoutePath;
import com.gregtechceu.gtceu.common.blockentity.DuctPipeBlockEntity;
import com.gregtechceu.gtceu.utils.FacingPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DuctRoutePath implements IRoutePath<IHazardParticleContainer> {
    private final DuctPipeBlockEntity targetPipe;
    @NotNull
    private final Direction targetFacing;
    private final int distance;
    private final DuctPipeProperties properties;

    public DuctRoutePath(DuctPipeBlockEntity targetPipe, @NotNull Direction facing, int distance, DuctPipeProperties properties) {
        this.targetPipe = targetPipe;
        this.targetFacing = facing;
        this.distance = distance;
        this.properties = properties;
    }

    @Override
    @NotNull
    public BlockPos getTargetPipePos() {
        return targetPipe.getPipePos();
    }

    @Override
    @Nullable
    public IHazardParticleContainer getHandler(Level world) {
        return GTCapabilityHelper.getHazardContainer(world, getTargetPipePos().relative(targetFacing), targetFacing.getOpposite());
    }

    public FacingPos toFacingPos() {
        return new FacingPos(getTargetPipePos(), targetFacing);
    }

    public DuctPipeBlockEntity getTargetPipe() {
        return this.targetPipe;
    }

    @NotNull
    public Direction getTargetFacing() {
        return this.targetFacing;
    }

    public int getDistance() {
        return this.distance;
    }

    public DuctPipeProperties getProperties() {
        return this.properties;
    }
}
