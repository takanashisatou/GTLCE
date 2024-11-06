package com.gregtechceu.gtceu.api.machine;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import lombok.NonNull;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/3/4
 * @implNote MultiblockMachineDefinition
 */
public class MultiblockMachineDefinition extends MachineDefinition {
    private boolean generator;
    @NonNull
    private Supplier<BlockPattern> patternFactory;
    private Supplier<List<MultiblockShapeInfo>> shapes;
    /**
     * Whether this multi can be rotated or face upwards.
     */
    private boolean allowExtendedFacing;
    /**
     * Set this to false only if your multiblock is set up such that it could have a wall-shared controller.
     */
    private boolean allowFlip;
    private boolean renderXEIPreview;
    @Nullable
    private Supplier<ItemStack[]> recoveryItems;
    private Comparator<IMultiPart> partSorter;
    private TriFunction<IMultiController, IMultiPart, Direction, BlockState> partAppearance;
    private BiConsumer<IMultiController, List<Component>> additionalDisplay;

    protected MultiblockMachineDefinition(ResourceLocation id) {
        super(id);
    }

    public static MultiblockMachineDefinition createDefinition(ResourceLocation id) {
        return new MultiblockMachineDefinition(id);
    }

    public List<MultiblockShapeInfo> getMatchingShapes() {
        var designs = shapes.get();
        if (!designs.isEmpty()) return designs;
        var structurePattern = patternFactory.get();
        int[][] aisleRepetitions = structurePattern.aisleRepetitions;
        return repetitionDFS(structurePattern, new ArrayList<>(), aisleRepetitions, new Stack<>());
    }

    private List<MultiblockShapeInfo> repetitionDFS(BlockPattern pattern, List<MultiblockShapeInfo> pages, int[][] aisleRepetitions, Stack<Integer> repetitionStack) {
        if (repetitionStack.size() == aisleRepetitions.length) {
            int[] repetition = new int[repetitionStack.size()];
            for (int i = 0; i < repetitionStack.size(); i++) {
                repetition[i] = repetitionStack.get(i);
            }
            pages.add(new MultiblockShapeInfo(pattern.getPreview(repetition)));
        } else {
            for (int i = aisleRepetitions[repetitionStack.size()][0]; i <= aisleRepetitions[repetitionStack.size()][1]; i++) {
                repetitionStack.push(i);
                repetitionDFS(pattern, pages, aisleRepetitions, repetitionStack);
                repetitionStack.pop();
            }
        }
        return pages;
    }

    public boolean isGenerator() {
        return this.generator;
    }

    public void setGenerator(final boolean generator) {
        this.generator = generator;
    }

    public void setPatternFactory(@NonNull final Supplier<BlockPattern> patternFactory) {
        if (patternFactory == null) {
            throw new NullPointerException("patternFactory is marked non-null but is null");
        }
        this.patternFactory = patternFactory;
    }

    @NonNull
    public Supplier<BlockPattern> getPatternFactory() {
        return this.patternFactory;
    }

    public void setShapes(final Supplier<List<MultiblockShapeInfo>> shapes) {
        this.shapes = shapes;
    }

    public Supplier<List<MultiblockShapeInfo>> getShapes() {
        return this.shapes;
    }

    /**
     * Whether this multi can be rotated or face upwards.
     */
    public boolean isAllowExtendedFacing() {
        return this.allowExtendedFacing;
    }

    /**
     * Whether this multi can be rotated or face upwards.
     */
    public void setAllowExtendedFacing(final boolean allowExtendedFacing) {
        this.allowExtendedFacing = allowExtendedFacing;
    }

    /**
     * Set this to false only if your multiblock is set up such that it could have a wall-shared controller.
     */
    public boolean isAllowFlip() {
        return this.allowFlip;
    }

    /**
     * Set this to false only if your multiblock is set up such that it could have a wall-shared controller.
     */
    public void setAllowFlip(final boolean allowFlip) {
        this.allowFlip = allowFlip;
    }

    public boolean isRenderXEIPreview() {
        return this.renderXEIPreview;
    }

    public void setRenderXEIPreview(final boolean renderXEIPreview) {
        this.renderXEIPreview = renderXEIPreview;
    }

    public void setRecoveryItems(@Nullable final Supplier<ItemStack[]> recoveryItems) {
        this.recoveryItems = recoveryItems;
    }

    @Nullable
    public Supplier<ItemStack[]> getRecoveryItems() {
        return this.recoveryItems;
    }

    public void setPartSorter(final Comparator<IMultiPart> partSorter) {
        this.partSorter = partSorter;
    }

    public Comparator<IMultiPart> getPartSorter() {
        return this.partSorter;
    }

    public TriFunction<IMultiController, IMultiPart, Direction, BlockState> getPartAppearance() {
        return this.partAppearance;
    }

    public void setPartAppearance(final TriFunction<IMultiController, IMultiPart, Direction, BlockState> partAppearance) {
        this.partAppearance = partAppearance;
    }

    public BiConsumer<IMultiController, List<Component>> getAdditionalDisplay() {
        return this.additionalDisplay;
    }

    public void setAdditionalDisplay(final BiConsumer<IMultiController, List<Component>> additionalDisplay) {
        this.additionalDisplay = additionalDisplay;
    }
}
