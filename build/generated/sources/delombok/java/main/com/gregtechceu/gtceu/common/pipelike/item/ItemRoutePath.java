package com.gregtechceu.gtceu.common.pipelike.item;

import com.gregtechceu.gtceu.api.data.chemical.material.properties.ItemPipeProperties;
import com.gregtechceu.gtceu.api.pipenet.IRoutePath;
import com.gregtechceu.gtceu.common.blockentity.ItemPipeBlockEntity;
import com.gregtechceu.gtceu.utils.FacingPos;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.side.item.ItemTransferHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ItemRoutePath implements IRoutePath<IItemTransfer> {
    private final ItemPipeBlockEntity targetPipe;
    @NotNull
    private final Direction targetFacing;
    private final int distance;
    private final ItemPipeProperties properties;
    private final Predicate<ItemStack> filters;

    public ItemRoutePath(ItemPipeBlockEntity targetPipe, @NotNull Direction facing, int distance, ItemPipeProperties properties, List<Predicate<ItemStack>> filters) {
        this.targetPipe = targetPipe;
        this.targetFacing = facing;
        this.distance = distance;
        this.properties = properties;
        this.filters = stack -> {
            for (Predicate<ItemStack> filter : filters) if (!filter.test(stack)) return false;
            return true;
        };
    }

    @Override
    @NotNull
    public BlockPos getTargetPipePos() {
        return targetPipe.getPipePos();
    }

    @Override
    @Nullable
    public IItemTransfer getHandler(Level world) {
        return ItemTransferHelper.getItemTransfer(world, getTargetPipePos().relative(targetFacing), targetFacing.getOpposite());
    }

    public boolean matchesFilters(ItemStack stack) {
        return filters.test(stack);
    }

    public FacingPos toFacingPos() {
        return new FacingPos(getTargetPipePos(), targetFacing);
    }

    public ItemPipeBlockEntity getTargetPipe() {
        return this.targetPipe;
    }

    @NotNull
    public Direction getTargetFacing() {
        return this.targetFacing;
    }

    public int getDistance() {
        return this.distance;
    }

    public ItemPipeProperties getProperties() {
        return this.properties;
    }
}
