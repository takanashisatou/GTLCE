package com.gregtechceu.gtceu.api.machine;

import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.editor.EditableMachineUI;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.lowdragmc.lowdraglib.utils.ShapeUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

/**
 * @author KilaBash
 * @date 2023/2/18
 * @implNote MachineDefinition
 *           Representing basic information of a machine.
 */
public class MachineDefinition implements Supplier<IMachineBlock> {
    private final ResourceLocation id;
    private Supplier<? extends Block> blockSupplier;
    private Supplier<? extends MetaMachineItem> itemSupplier;
    private Supplier<BlockEntityType<? extends BlockEntity>> blockEntityTypeSupplier;
    private Function<IMachineBlockEntity, MetaMachine> machineSupplier;
    @Nullable
    private GTRecipeType[] recipeTypes;
    private int tier;
    private int defaultPaintingColor;
    private RecipeModifier recipeModifier;
    private boolean alwaysTryModifyRecipe;
    @NotNull
    private BiPredicate<IRecipeLogicMachine, GTRecipe> beforeWorking = (machine, recipe) -> true;
    @NotNull
    private Predicate<IRecipeLogicMachine> onWorking = machine -> true;
    @NotNull
    private Consumer<IRecipeLogicMachine> onWaiting = machine -> {
    };
    @NotNull
    private Consumer<IRecipeLogicMachine> afterWorking = machine -> {
    };
    private IRenderer renderer;
    private VoxelShape shape;
    private boolean renderWorldPreview;
    private boolean renderXEIPreview;
    private final Map<Direction, VoxelShape> cache = new EnumMap<>(Direction.class);
    private BiConsumer<ItemStack, List<Component>> tooltipBuilder;
    private Supplier<BlockState> appearance;
    @Nullable
    private EditableMachineUI editableUI;
    private Object2IntMap<RecipeCapability<?>> recipeOutputLimits = new Object2IntOpenHashMap<>();

    protected MachineDefinition(ResourceLocation id) {
        this.id = id;
    }

    public static MachineDefinition createDefinition(ResourceLocation id) {
        return new MachineDefinition(id);
    }

    public Block getBlock() {
        return blockSupplier.get();
    }

    public MetaMachineItem getItem() {
        return itemSupplier.get();
    }

    public BlockEntityType<? extends BlockEntity> getBlockEntityType() {
        return blockEntityTypeSupplier.get();
    }

    public MetaMachine createMetaMachine(IMachineBlockEntity blockEntity) {
        return machineSupplier.apply(blockEntity);
    }

    public ItemStack asStack() {
        return new ItemStack(getItem());
    }

    public ItemStack asStack(int count) {
        return new ItemStack(getItem(), count);
    }

    public VoxelShape getShape(Direction direction) {
        if (shape.isEmpty() || shape == Shapes.block() || direction == Direction.NORTH) return shape;
        return this.cache.computeIfAbsent(direction, dir -> ShapeUtils.rotate(shape, dir));
    }

    @Override
    public IMachineBlock get() {
        return (IMachineBlock) blockSupplier.get();
    }

    public String getName() {
        return id.getPath();
    }

    @Override
    public String toString() {
        return "[Definition: %s]".formatted(id);
    }

    public String getDescriptionId() {
        return getBlock().getDescriptionId();
    }

    public BlockState defaultBlockState() {
        return getBlock().defaultBlockState();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MachineDefinition that = (MachineDefinition) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    static final ThreadLocal<MachineDefinition> STATE = new ThreadLocal<>();

    public static MachineDefinition getBuilt() {
        return STATE.get();
    }

    public static void setBuilt(MachineDefinition state) {
        STATE.set(state);
    }

    public static void clearBuilt() {
        STATE.remove();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public void setBlockSupplier(final Supplier<? extends Block> blockSupplier) {
        this.blockSupplier = blockSupplier;
    }

    public void setItemSupplier(final Supplier<? extends MetaMachineItem> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public void setBlockEntityTypeSupplier(final Supplier<BlockEntityType<? extends BlockEntity>> blockEntityTypeSupplier) {
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
    }

    public void setMachineSupplier(final Function<IMachineBlockEntity, MetaMachine> machineSupplier) {
        this.machineSupplier = machineSupplier;
    }

    @Nullable
    public GTRecipeType[] getRecipeTypes() {
        return this.recipeTypes;
    }

    public void setRecipeTypes(@Nullable final GTRecipeType[] recipeTypes) {
        this.recipeTypes = recipeTypes;
    }

    public int getTier() {
        return this.tier;
    }

    public void setTier(final int tier) {
        this.tier = tier;
    }

    public int getDefaultPaintingColor() {
        return this.defaultPaintingColor;
    }

    public void setDefaultPaintingColor(final int defaultPaintingColor) {
        this.defaultPaintingColor = defaultPaintingColor;
    }

    public RecipeModifier getRecipeModifier() {
        return this.recipeModifier;
    }

    public void setRecipeModifier(final RecipeModifier recipeModifier) {
        this.recipeModifier = recipeModifier;
    }

    public boolean isAlwaysTryModifyRecipe() {
        return this.alwaysTryModifyRecipe;
    }

    public void setAlwaysTryModifyRecipe(final boolean alwaysTryModifyRecipe) {
        this.alwaysTryModifyRecipe = alwaysTryModifyRecipe;
    }

    @NotNull
    public BiPredicate<IRecipeLogicMachine, GTRecipe> getBeforeWorking() {
        return this.beforeWorking;
    }

    public void setBeforeWorking(@NotNull final BiPredicate<IRecipeLogicMachine, GTRecipe> beforeWorking) {
        if (beforeWorking == null) {
            throw new NullPointerException("beforeWorking is marked non-null but is null");
        }
        this.beforeWorking = beforeWorking;
    }

    @NotNull
    public Predicate<IRecipeLogicMachine> getOnWorking() {
        return this.onWorking;
    }

    public void setOnWorking(@NotNull final Predicate<IRecipeLogicMachine> onWorking) {
        if (onWorking == null) {
            throw new NullPointerException("onWorking is marked non-null but is null");
        }
        this.onWorking = onWorking;
    }

    @NotNull
    public Consumer<IRecipeLogicMachine> getOnWaiting() {
        return this.onWaiting;
    }

    public void setOnWaiting(@NotNull final Consumer<IRecipeLogicMachine> onWaiting) {
        if (onWaiting == null) {
            throw new NullPointerException("onWaiting is marked non-null but is null");
        }
        this.onWaiting = onWaiting;
    }

    @NotNull
    public Consumer<IRecipeLogicMachine> getAfterWorking() {
        return this.afterWorking;
    }

    public void setAfterWorking(@NotNull final Consumer<IRecipeLogicMachine> afterWorking) {
        if (afterWorking == null) {
            throw new NullPointerException("afterWorking is marked non-null but is null");
        }
        this.afterWorking = afterWorking;
    }

    public IRenderer getRenderer() {
        return this.renderer;
    }

    public void setRenderer(final IRenderer renderer) {
        this.renderer = renderer;
    }

    public void setShape(final VoxelShape shape) {
        this.shape = shape;
    }

    public boolean isRenderWorldPreview() {
        return this.renderWorldPreview;
    }

    public void setRenderWorldPreview(final boolean renderWorldPreview) {
        this.renderWorldPreview = renderWorldPreview;
    }

    public boolean isRenderXEIPreview() {
        return this.renderXEIPreview;
    }

    public void setRenderXEIPreview(final boolean renderXEIPreview) {
        this.renderXEIPreview = renderXEIPreview;
    }

    public BiConsumer<ItemStack, List<Component>> getTooltipBuilder() {
        return this.tooltipBuilder;
    }

    public void setTooltipBuilder(final BiConsumer<ItemStack, List<Component>> tooltipBuilder) {
        this.tooltipBuilder = tooltipBuilder;
    }

    public Supplier<BlockState> getAppearance() {
        return this.appearance;
    }

    public void setAppearance(final Supplier<BlockState> appearance) {
        this.appearance = appearance;
    }

    @Nullable
    public EditableMachineUI getEditableUI() {
        return this.editableUI;
    }

    public void setEditableUI(@Nullable final EditableMachineUI editableUI) {
        this.editableUI = editableUI;
    }

    public Object2IntMap<RecipeCapability<?>> getRecipeOutputLimits() {
        return this.recipeOutputLimits;
    }

    public void setRecipeOutputLimits(final Object2IntMap<RecipeCapability<?>> recipeOutputLimits) {
        this.recipeOutputLimits = recipeOutputLimits;
    }
}
