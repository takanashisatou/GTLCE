package com.gregtechceu.gtceu.api.registry.registrate;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.gui.editor.EditableMachineUI;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifierList;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.client.renderer.GTRendererProvider;
import com.gregtechceu.gtceu.client.renderer.machine.*;
import com.gregtechceu.gtceu.common.data.GTCompassSections;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.function.*;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author KilaBash
 * @date 2023/2/18
 * @implNote MachineBuilder
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MachineBuilder<DEFINITION extends MachineDefinition> extends BuilderBase<DEFINITION> {
    protected final Registrate registrate;
    protected final String name;
    protected final BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory;
    protected final BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory;
    protected final TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory;
    protected Function<ResourceLocation, DEFINITION> definitionFactory; // non-final for KJS
    protected Function<IMachineBlockEntity, MetaMachine> metaMachine; // non-final for KJS
    @Nullable
    private Supplier<IRenderer> renderer;
    private VoxelShape shape = Shapes.block();
    private RotationState rotationState = RotationState.NONE;
    private boolean hasTESR;
    private boolean renderMultiblockWorldPreview = true;
    private boolean renderMultiblockXEIPreview = true;
    private NonNullUnaryOperator<BlockBehaviour.Properties> blockProp = p -> p;
    private NonNullUnaryOperator<Item.Properties> itemProp = p -> p;
    private Consumer<BlockBuilder<? extends Block, ?>> blockBuilder;
    private Consumer<ItemBuilder<? extends MetaMachineItem, ?>> itemBuilder;
    private NonNullConsumer<BlockEntityType<BlockEntity>> onBlockEntityRegister = MetaMachineBlockEntity::onBlockEntityRegister;
    private GTRecipeType[] recipeTypes;
    // getter for KJS
    private int tier;
    private Object2IntMap<RecipeCapability<?>> recipeOutputLimits = new Object2IntOpenHashMap<>();
    private int paintingColor = Long.decode(ConfigHolder.INSTANCE.client.defaultPaintingColor).intValue();
    private BiFunction<ItemStack, Integer, Integer> itemColor = ((itemStack, tintIndex) -> tintIndex == 2 ? GTValues.VC[tier] : tintIndex == 1 ? paintingColor : -1);
    private PartAbility[] abilities = new PartAbility[0];
    private final List<Component> tooltips = new ArrayList<>();
    private BiConsumer<ItemStack, List<Component>> tooltipBuilder;
    private RecipeModifier recipeModifier = new RecipeModifierList(GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK));
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
    private Supplier<BlockState> appearance;
    @Nullable
    private EditableMachineUI editableUI;
    private String langValue = null;
    private final Set<CompassSection> compassSections = new HashSet<>();
    @Nullable
    private String compassNode = null;
    @Nullable
    private ResourceLocation compassPage = null;
    private final List<ResourceLocation> preNodes = new ArrayList<>();

    protected MachineBuilder(Registrate registrate, String name, Function<ResourceLocation, DEFINITION> definitionFactory, Function<IMachineBlockEntity, MetaMachine> metaMachine, BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory, BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory, TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        super(new ResourceLocation(registrate.getModid(), name));
        this.registrate = registrate;
        this.name = name;
        this.metaMachine = metaMachine;
        this.blockFactory = blockFactory;
        this.itemFactory = itemFactory;
        this.blockEntityFactory = blockEntityFactory;
        this.definitionFactory = definitionFactory;
    }

    public MachineBuilder<DEFINITION> recipeType(GTRecipeType type) {
        this.recipeTypes = ArrayUtils.add(this.recipeTypes, type);
        return this;
    }

    public MachineBuilder<DEFINITION> recipeTypes(GTRecipeType... types) {
        for (GTRecipeType type : types) {
            this.recipeTypes = ArrayUtils.add(this.recipeTypes, type);
        }
        return this;
    }

    public static <DEFINITION extends MachineDefinition> MachineBuilder<DEFINITION> create(Registrate registrate, String name, Function<ResourceLocation, DEFINITION> definitionFactory, Function<IMachineBlockEntity, MetaMachine> metaMachine, BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory, BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory, TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        return new MachineBuilder<>(registrate, name, definitionFactory, metaMachine, blockFactory, itemFactory, blockEntityFactory);
    }

    public MachineBuilder<DEFINITION> modelRenderer(Supplier<ResourceLocation> model) {
        this.renderer = () -> new MachineRenderer(model.get());
        return this;
    }

    public MachineBuilder<DEFINITION> defaultModelRenderer() {
        return modelRenderer(() -> new ResourceLocation(registrate.getModid(), "block/" + name));
    }

    public MachineBuilder<DEFINITION> tieredHullRenderer(ResourceLocation model) {
        return renderer(() -> new TieredHullMachineRenderer(tier, model));
    }

    public MachineBuilder<DEFINITION> overlayTieredHullRenderer(String name) {
        return renderer(() -> new OverlayTieredMachineRenderer(tier, new ResourceLocation(registrate.getModid(), "block/machine/part/" + name)));
    }

    public MachineBuilder<DEFINITION> overlaySteamHullRenderer(String name) {
        return renderer(() -> new OverlaySteamMachineRenderer(new ResourceLocation(registrate.getModid(), "block/machine/part/" + name)));
    }

    public MachineBuilder<DEFINITION> workableTieredHullRenderer(ResourceLocation workableModel) {
        return renderer(() -> new WorkableTieredHullMachineRenderer(tier, workableModel));
    }

    public MachineBuilder<DEFINITION> workableSteamHullRenderer(boolean isHighPressure, ResourceLocation workableModel) {
        return renderer(() -> new WorkableSteamMachineRenderer(isHighPressure, workableModel));
    }

    public MachineBuilder<DEFINITION> workableCasingRenderer(ResourceLocation baseCasing, ResourceLocation workableModel) {
        return renderer(() -> new WorkableCasingMachineRenderer(baseCasing, workableModel));
    }

    public MachineBuilder<DEFINITION> workableCasingRenderer(ResourceLocation baseCasing, ResourceLocation workableModel, boolean tint) {
        return renderer(() -> new WorkableCasingMachineRenderer(baseCasing, workableModel, tint));
    }

    public MachineBuilder<DEFINITION> sidedWorkableCasingRenderer(String basePath, ResourceLocation overlayModel, boolean tint) {
        return renderer(() -> new WorkableSidedCasingMachineRenderer(basePath, overlayModel, tint));
    }

    public MachineBuilder<DEFINITION> sidedWorkableCasingRenderer(String basePath, ResourceLocation overlayModel) {
        return renderer(() -> new WorkableSidedCasingMachineRenderer(basePath, overlayModel));
    }

    public MachineBuilder<DEFINITION> appearanceBlock(Supplier<? extends Block> block) {
        appearance = () -> block.get().defaultBlockState();
        return this;
    }

    public MachineBuilder<DEFINITION> tooltips(Component... components) {
        tooltips.addAll(Arrays.stream(components).filter(Objects::nonNull).toList());
        return this;
    }

    public MachineBuilder<DEFINITION> abilities(PartAbility... abilities) {
        this.abilities = abilities;
        compassSections(GTCompassSections.PARTS);
        return this;
    }

    public MachineBuilder<DEFINITION> recipeModifier(RecipeModifier recipeModifier) {
        this.recipeModifier = recipeModifier instanceof RecipeModifierList list ? list : new RecipeModifierList(recipeModifier);
        return this;
    }

    public MachineBuilder<DEFINITION> recipeModifier(RecipeModifier recipeModifier, boolean alwaysTryModifyRecipe) {
        this.alwaysTryModifyRecipe = alwaysTryModifyRecipe;
        return this.recipeModifier(recipeModifier);
    }

    public MachineBuilder<DEFINITION> recipeModifiers(RecipeModifier... recipeModifiers) {
        this.recipeModifier = new RecipeModifierList(recipeModifiers);
        return this;
    }

    public MachineBuilder<DEFINITION> recipeModifiers(boolean alwaysTryModifyRecipe, RecipeModifier... recipeModifiers) {
        return this.recipeModifier(new RecipeModifierList(recipeModifiers), alwaysTryModifyRecipe);
    }

    public MachineBuilder<DEFINITION> noRecipeModifier() {
        this.recipeModifier = new RecipeModifierList(((machine, recipe, params, result) -> recipe));
        this.alwaysTryModifyRecipe = false;
        return this;
    }

    public MachineBuilder<DEFINITION> addOutputLimit(RecipeCapability<?> capability, int limit) {
        this.recipeOutputLimits.put(capability, limit);
        return this;
    }

    public MachineBuilder<DEFINITION> multiblockPreviewRenderer(boolean multiBlockWorldPreview, boolean multiBlockXEIPreview) {
        this.renderMultiblockWorldPreview = multiBlockWorldPreview;
        this.renderMultiblockXEIPreview = multiBlockXEIPreview;
        return this;
    }

    public MachineBuilder<DEFINITION> compassSections(CompassSection... sections) {
        this.compassSections.addAll(Arrays.stream(sections).toList());
        return this;
    }

    public MachineBuilder<DEFINITION> compassNodeSelf() {
        this.compassNode = name;
        return this;
    }

    public MachineBuilder<DEFINITION> compassNode(String compassNode) {
        this.compassNode = compassNode;
        return this;
    }

    public MachineBuilder<DEFINITION> compassPreNodes(CompassSection section, String... compassNodes) {
        for (String nodeID : compassNodes) {
            preNodes.add(GTCEu.id(section.sectionID().getPath() + "/" + nodeID));
        }
        return this;
    }

    public MachineBuilder<DEFINITION> compassPreNodes(ResourceLocation... compassNodes) {
        preNodes.addAll(Arrays.asList(compassNodes));
        return this;
    }

    public MachineBuilder<DEFINITION> compassPreNodes(CompassNode... compassNodes) {
        preNodes.addAll(Arrays.stream(compassNodes).map(CompassNode::nodeID).toList());
        return this;
    }

    protected DEFINITION createDefinition() {
        return definitionFactory.apply(new ResourceLocation(registrate.getModid(), name));
    }

    // @HideFromJS
    public DEFINITION register() {
        var definition = createDefinition();
        var blockBuilder = 
        // .tag(GTToolType.WRENCH.harvestTag)
        registrate.block(name, properties -> {
            RotationState.set(rotationState);
            MachineDefinition.setBuilt(definition);
            var b = blockFactory.apply(properties, definition);
            RotationState.clear();
            MachineDefinition.clearBuilt();
            return b.self();
        }).color(() -> () -> IMachineBlock::colorTinted).initialProperties(() -> Blocks.DISPENSER).properties(BlockBehaviour.Properties::noLootTable).addLayer(() -> RenderType::cutoutMipped).blockstate(NonNullBiConsumer.noop()).properties(blockProp).onRegister(b -> Arrays.stream(abilities).forEach(a -> a.register(tier, b)));
        if (this.langValue != null) {
            blockBuilder.lang(langValue);
        }
        if (this.blockBuilder != null) {
            this.blockBuilder.accept(blockBuilder);
        }
        var block = blockBuilder.register();
        var itemBuilder =  // do not gen any lang keys
        registrate.item(name, properties -> itemFactory.apply((IMachineBlock) block.get(), properties)).setData(ProviderType.LANG, NonNullBiConsumer.noop()).model(NonNullBiConsumer.noop()).color(() -> () -> itemColor::apply).properties(itemProp);
        if (this.itemBuilder != null) {
            this.itemBuilder.accept(itemBuilder);
        }
        if (this.compassNode != null) {
            if (compassSections.isEmpty()) {
                compassSections.add(GTCompassSections.MACHINES);
            }
            for (CompassSection section : compassSections) {
                itemBuilder.onRegister(item -> {
                    var node = CompassNode.getOrCreate(section, compassNode).addItem(item::asItem).addPreNode(preNodes.toArray(ResourceLocation[]::new));
                    if (compassPage != null) {
                        node.page(compassPage);
                    }
                });
            }
        }
        var item = itemBuilder.register();
        var blockEntityBuilder = registrate.blockEntity(name, (type, pos, state) -> blockEntityFactory.apply(type, pos, state).self()).onRegister(onBlockEntityRegister).validBlock(block);
        if (hasTESR) {
            blockEntityBuilder = blockEntityBuilder.renderer(() -> GTRendererProvider::getOrCreate);
        }
        var blockEntity = blockEntityBuilder.register();
        definition.setRecipeTypes(recipeTypes);
        definition.setBlockSupplier(block);
        definition.setItemSupplier(item);
        definition.setTier(tier);
        definition.setRecipeOutputLimits(recipeOutputLimits);
        definition.setBlockEntityTypeSupplier(blockEntity::get);
        definition.setMachineSupplier(metaMachine);
        definition.setTooltipBuilder((itemStack, components) -> {
            components.addAll(tooltips);
            if (tooltipBuilder != null) tooltipBuilder.accept(itemStack, components);
        });
        definition.setRecipeModifier(recipeModifier);
        definition.setAlwaysTryModifyRecipe(alwaysTryModifyRecipe);
        definition.setBeforeWorking(this.beforeWorking);
        definition.setOnWorking(this.onWorking);
        definition.setOnWaiting(this.onWaiting);
        definition.setAfterWorking(this.afterWorking);
        if (renderer == null) {
            renderer = () -> new MachineRenderer(new ResourceLocation(registrate.getModid(), "block/machine/" + name));
        }
        if (recipeTypes != null) {
            for (GTRecipeType type : recipeTypes) {
                if (type != null && type.getIconSupplier() == null) {
                    type.setIconSupplier(definition::asStack);
                }
            }
        }
        if (appearance == null) {
            appearance = block::getDefaultState;
        }
        if (editableUI != null) {
            definition.setEditableUI(editableUI);
        }
        definition.setAppearance(appearance);
        definition.setRenderer(LDLib.isClient() ? renderer.get() : IRenderer.EMPTY);
        definition.setShape(shape);
        definition.setDefaultPaintingColor(paintingColor);
        definition.setRenderXEIPreview(renderMultiblockXEIPreview);
        definition.setRenderWorldPreview(renderMultiblockWorldPreview);
        GTRegistries.MACHINES.register(definition.getId(), definition);
        return definition;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> definitionFactory(final Function<ResourceLocation, DEFINITION> definitionFactory) {
        this.definitionFactory = definitionFactory;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> metaMachine(final Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        this.metaMachine = metaMachine;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> renderer(@Nullable final Supplier<IRenderer> renderer) {
        this.renderer = renderer;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> shape(final VoxelShape shape) {
        this.shape = shape;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> rotationState(final RotationState rotationState) {
        this.rotationState = rotationState;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> hasTESR(final boolean hasTESR) {
        this.hasTESR = hasTESR;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> renderMultiblockWorldPreview(final boolean renderMultiblockWorldPreview) {
        this.renderMultiblockWorldPreview = renderMultiblockWorldPreview;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> renderMultiblockXEIPreview(final boolean renderMultiblockXEIPreview) {
        this.renderMultiblockXEIPreview = renderMultiblockXEIPreview;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> blockProp(final NonNullUnaryOperator<BlockBehaviour.Properties> blockProp) {
        this.blockProp = blockProp;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> itemProp(final NonNullUnaryOperator<Item.Properties> itemProp) {
        this.itemProp = itemProp;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> blockBuilder(final Consumer<BlockBuilder<? extends Block, ?>> blockBuilder) {
        this.blockBuilder = blockBuilder;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> itemBuilder(final Consumer<ItemBuilder<? extends MetaMachineItem, ?>> itemBuilder) {
        this.itemBuilder = itemBuilder;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> onBlockEntityRegister(final NonNullConsumer<BlockEntityType<BlockEntity>> onBlockEntityRegister) {
        this.onBlockEntityRegister = onBlockEntityRegister;
        return this;
    }

    public int tier() {
        return this.tier;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> tier(final int tier) {
        this.tier = tier;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> recipeOutputLimits(final Object2IntMap<RecipeCapability<?>> recipeOutputLimits) {
        this.recipeOutputLimits = recipeOutputLimits;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> paintingColor(final int paintingColor) {
        this.paintingColor = paintingColor;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> itemColor(final BiFunction<ItemStack, Integer, Integer> itemColor) {
        this.itemColor = itemColor;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> tooltipBuilder(final BiConsumer<ItemStack, List<Component>> tooltipBuilder) {
        this.tooltipBuilder = tooltipBuilder;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> alwaysTryModifyRecipe(final boolean alwaysTryModifyRecipe) {
        this.alwaysTryModifyRecipe = alwaysTryModifyRecipe;
        return this;
    }

    @NotNull
    public BiPredicate<IRecipeLogicMachine, GTRecipe> beforeWorking() {
        return this.beforeWorking;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> beforeWorking(@NotNull final BiPredicate<IRecipeLogicMachine, GTRecipe> beforeWorking) {
        if (beforeWorking == null) {
            throw new NullPointerException("beforeWorking is marked non-null but is null");
        }
        this.beforeWorking = beforeWorking;
        return this;
    }

    @NotNull
    public Predicate<IRecipeLogicMachine> onWorking() {
        return this.onWorking;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> onWorking(@NotNull final Predicate<IRecipeLogicMachine> onWorking) {
        if (onWorking == null) {
            throw new NullPointerException("onWorking is marked non-null but is null");
        }
        this.onWorking = onWorking;
        return this;
    }

    @NotNull
    public Consumer<IRecipeLogicMachine> onWaiting() {
        return this.onWaiting;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> onWaiting(@NotNull final Consumer<IRecipeLogicMachine> onWaiting) {
        if (onWaiting == null) {
            throw new NullPointerException("onWaiting is marked non-null but is null");
        }
        this.onWaiting = onWaiting;
        return this;
    }

    @NotNull
    public Consumer<IRecipeLogicMachine> afterWorking() {
        return this.afterWorking;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> afterWorking(@NotNull final Consumer<IRecipeLogicMachine> afterWorking) {
        if (afterWorking == null) {
            throw new NullPointerException("afterWorking is marked non-null but is null");
        }
        this.afterWorking = afterWorking;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> appearance(final Supplier<BlockState> appearance) {
        this.appearance = appearance;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> editableUI(@Nullable final EditableMachineUI editableUI) {
        this.editableUI = editableUI;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> langValue(final String langValue) {
        this.langValue = langValue;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public MachineBuilder<DEFINITION> compassPage(@Nullable final ResourceLocation compassPage) {
        this.compassPage = compassPage;
        return this;
    }
}
