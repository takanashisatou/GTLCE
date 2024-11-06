package com.gregtechceu.gtceu.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.Objects;

public class FacingPos {
    private final BlockPos pos;
    private final Direction facing;
    private final int hashCode;

    public FacingPos(BlockPos pos, Direction facing) {
        this.pos = pos;
        this.facing = facing;
        this.hashCode = Objects.hash(pos, facing);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacingPos facingPos = (FacingPos) o;
        return pos.equals(facingPos.pos) && facing == facingPos.getFacing();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Direction getFacing() {
        return this.facing;
    }
}
