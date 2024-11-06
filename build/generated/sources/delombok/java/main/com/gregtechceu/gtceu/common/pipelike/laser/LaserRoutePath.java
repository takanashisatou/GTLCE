package com.gregtechceu.gtceu.common.pipelike.laser;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.ILaserContainer;
import com.gregtechceu.gtceu.api.pipenet.IAttachData;
import com.gregtechceu.gtceu.api.pipenet.IRoutePath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaserRoutePath implements IRoutePath<ILaserContainer>, IAttachData {
    private final BlockPos targetPipePos;
    /**
     * the current face to handler
     */
    @NotNull
    private final Direction targetFacing;
    /**
     * the manhattan distance traveled during walking
     */
    private final int distance;
    byte connections;

    public LaserRoutePath(BlockPos targetPipePos, Direction targetFacing, int distance) {
        this.targetPipePos = targetPipePos;
        this.targetFacing = targetFacing;
        this.distance = distance;
    }

    /**
     * Gets the handler if it exists
     *
     * @return the handler
     */
    @Nullable
    public ILaserContainer getHandler(Level level) {
        return GTCapabilityHelper.getLaser(level, getTargetPipePos().relative(targetFacing), targetFacing.getOpposite());
    }

    @Override
    public boolean canAttachTo(Direction side) {
        return (connections & (1 << side.ordinal())) != 0 && side.getAxis() == this.targetFacing.getAxis();
    }

    @Override
    public boolean setAttached(Direction side, boolean attach) {
        var result = canAttachTo(side);
        if (result != attach) {
            if (attach) {
                connections |= (1 << side.ordinal());
            } else {
                connections &= ~(1 << side.ordinal());
            }
        }
        return result != attach;
    }

    public BlockPos getTargetPipePos() {
        return this.targetPipePos;
    }

    /**
     * the current face to handler
     */
    @NotNull
    public Direction getTargetFacing() {
        return this.targetFacing;
    }

    /**
     * the manhattan distance traveled during walking
     */
    public int getDistance() {
        return this.distance;
    }

    public byte getConnections() {
        return this.connections;
    }
}
