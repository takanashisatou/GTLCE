package com.gregtechceu.gtceu.api.recipe;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.gui.SteamTexture;
import com.gregtechceu.gtceu.api.recipe.chance.boost.ChanceBoostFunction;
import com.gregtechceu.gtceu.api.recipe.lookup.GTRecipeLookup;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI;
import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.core.mixins.RecipeManagerInvoker;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/2/20
 * @implNote GTRecipeType
 */
public class GTRecipeType implements RecipeType<GTRecipe> {
    public final ResourceLocation registryName;
    public final String group;
    public final TreeMap<RecipeCapability<?>, Integer> maxInputs = new TreeMap<>(RecipeCapability.COMPARATOR);
    public final TreeMap<RecipeCapability<?>, Integer> maxOutputs = new TreeMap<>(RecipeCapability.COMPARATOR);
    private GTRecipeBuilder recipeBuilder;
    private ChanceBoostFunction chanceFunction = ChanceBoostFunction.OVERCLOCK;
    private GTRecipeTypeUI recipeUI = new GTRecipeTypeUI(this);
    private final Byte2ObjectMap<IGuiTexture> slotOverlays = new Byte2ObjectArrayMap<>();
    private GTRecipeType smallRecipeMap;
    @Nullable
    private Supplier<ItemStack> iconSupplier;
    @Nullable
    protected SoundEntry sound;
    protected List<Function<CompoundTag, String>> dataInfos = new ArrayList<>();
    protected boolean isFuelRecipeType;
    protected boolean isScanner;
    // Does this recipe type have a research item slot? If this is true you MUST create a custom UI.
    protected boolean hasResearchSlot;
    protected final Map<RecipeType<?>, List<GTRecipe>> proxyRecipes;
    private CompoundTag customUICache;
    private final GTRecipeLookup lookup = new GTRecipeLookup(this);
    private boolean offsetVoltageText = false;
    private int voltageTextOffset = 20;
    private final Map<String, Collection<GTRecipe>> researchEntries = new Object2ObjectOpenHashMap<>();
    private final List<ICustomRecipeLogic> customRecipeLogicRunners = new ArrayList<>();

    public GTRecipeType(ResourceLocation registryName, String group, RecipeType<?>... proxyRecipes) {
        this.registryName = registryName;
        this.group = group;
        recipeBuilder = new GTRecipeBuilder(registryName, this);
        // must be linked to stop json contents from shuffling
        Map<RecipeType<?>, List<GTRecipe>> map = new Object2ObjectLinkedOpenHashMap<>();
        for (RecipeType<?> proxyRecipe : proxyRecipes) {
            map.put(proxyRecipe, new ArrayList<>());
        }
        this.proxyRecipes = map;
    }

    public GTRecipeType setMaxIOSize(int maxInputs, int maxOutputs, int maxFluidInputs, int maxFluidOutputs) {
        return setMaxSize(IO.IN, ItemRecipeCapability.CAP, maxInputs).setMaxSize(IO.IN, FluidRecipeCapability.CAP, maxFluidInputs).setMaxSize(IO.OUT, ItemRecipeCapability.CAP, maxOutputs).setMaxSize(IO.OUT, FluidRecipeCapability.CAP, maxFluidOutputs);
    }

    public GTRecipeType setEUIO(IO io) {
        if (io.support(IO.IN)) {
            setMaxSize(IO.IN, EURecipeCapability.CAP, 1);
        }
        if (io.support(IO.OUT)) {
            setMaxSize(IO.OUT, EURecipeCapability.CAP, 1);
        }
        return this;
    }

    public GTRecipeType setMaxSize(IO io, RecipeCapability<?> cap, int max) {
        if (io == IO.IN || io == IO.BOTH) {
            maxInputs.put(cap, max);
        }
        if (io == IO.OUT || io == IO.BOTH) {
            maxOutputs.put(cap, max);
        }
        return this;
    }

    public GTRecipeType setSlotOverlay(boolean isOutput, boolean isFluid, IGuiTexture slotOverlay) {
        this.recipeUI.setSlotOverlay(isOutput, isFluid, slotOverlay);
        return this;
    }

    public GTRecipeType setSlotOverlay(boolean isOutput, boolean isFluid, boolean isLast, IGuiTexture slotOverlay) {
        this.recipeUI.setSlotOverlay(isOutput, isFluid, isLast, slotOverlay);
        return this;
    }

    public GTRecipeType setProgressBar(ResourceTexture progressBar, ProgressTexture.FillDirection moveType) {
        this.recipeUI.setProgressBar(progressBar, moveType);
        return this;
    }

    public GTRecipeType setSteamProgressBar(SteamTexture progressBar, ProgressTexture.FillDirection moveType) {
        this.recipeUI.setSteamProgressBarTexture(progressBar);
        this.recipeUI.setSteamMoveType(moveType);
        return this;
    }

    public GTRecipeType setUiBuilder(BiConsumer<GTRecipe, WidgetGroup> uiBuilder) {
        this.recipeUI.setUiBuilder(uiBuilder);
        return this;
    }

    public GTRecipeType setMaxTooltips(int maxTooltips) {
        this.recipeUI.setMaxTooltips(maxTooltips);
        return this;
    }

    public GTRecipeType setXEIVisible(boolean XEIVisible) {
        this.recipeUI.setXEIVisible(XEIVisible);
        return this;
    }

    public GTRecipeType addDataInfo(Function<CompoundTag, String> dataInfo) {
        this.dataInfos.add(dataInfo);
        return this;
    }

    /**
     * @param recipeLogic A function which is passed the normal findRecipe() result. Returns null if no valid recipe for
     *                    the custom logic is found.
     */
    public GTRecipeType addCustomRecipeLogic(ICustomRecipeLogic recipeLogic) {
        this.customRecipeLogicRunners.add(recipeLogic);
        return this;
    }

    @Override
    public String toString() {
        return registryName.toString();
    }

    @Nullable
    public GTRecipe getRecipe(RecipeManager recipeManager, ResourceLocation id) {
        var recipes = ((RecipeManagerInvoker) recipeManager).getRecipeFromType(this);
        if (recipes.get(id) instanceof GTRecipe recipe) {
            return recipe;
        }
        return null;
    }

    @Nullable
    public Iterator<GTRecipe> searchFuelRecipe(IRecipeCapabilityHolder holder) {
        if (!holder.hasProxies() || !isFuelRecipeType()) return null;
        return getLookup().getRecipeIterator(holder, recipe -> recipe.isFuel && recipe.matchRecipe(holder).isSuccess() && recipe.matchTickRecipe(holder).isSuccess());
    }

    public Iterator<GTRecipe> searchRecipe(IRecipeCapabilityHolder holder) {
        if (!holder.hasProxies()) return null;
        var iterator = getLookup().getRecipeIterator(holder, recipe -> !recipe.isFuel && recipe.matchRecipe(holder).isSuccess() && recipe.matchTickRecipe(holder).isSuccess());
        boolean any = false;
        while (iterator.hasNext()) {
            GTRecipe recipe = iterator.next();
            if (recipe == null) continue;
            any = true;
            break;
        }
        if (any) {
            iterator.reset();
            return iterator;
        }
        for (ICustomRecipeLogic logic : customRecipeLogicRunners) {
            GTRecipe recipe = logic.createCustomRecipe(holder);
            if (recipe != null) return Collections.singleton(recipe).iterator();
        }
        return Collections.emptyIterator();
    }

    public int getMaxInputs(RecipeCapability<?> cap) {
        return maxInputs.getOrDefault(cap, 0);
    }

    public int getMaxOutputs(RecipeCapability<?> cap) {
        return maxOutputs.getOrDefault(cap, 0);
    }

    //////////////////////////////////////
    // ***** Recipe Builder ******//
    //////////////////////////////////////
    public GTRecipeType prepareBuilder(Consumer<GTRecipeBuilder> onPrepare) {
        onPrepare.accept(recipeBuilder);
        return this;
    }

    public GTRecipeBuilder recipeBuilder(ResourceLocation id, Object... append) {
        if (append.length > 0) {
            return recipeBuilder.copy(new ResourceLocation(id.getNamespace(), id.getPath() + Arrays.stream(append).map(Object::toString).map(FormattingUtil::toLowerCaseUnder).reduce("", (a, b) -> a + "_" + b)));
        }
        return recipeBuilder.copy(id);
    }

    public GTRecipeBuilder recipeBuilder(String id, Object... append) {
        return recipeBuilder(GTCEu.id(id), append);
    }

    public GTRecipeBuilder recipeBuilder(UnificationEntry entry, Object... append) {
        return recipeBuilder(GTCEu.id(entry.tagPrefix + (entry.material == null ? "" : "_" + entry.material.getName())), append);
    }

    public GTRecipeBuilder recipeBuilder(Supplier<? extends ItemLike> item, Object... append) {
        return recipeBuilder(item.get(), append);
    }

    public GTRecipeBuilder recipeBuilder(ItemLike itemLike, Object... append) {
        return recipeBuilder(new ResourceLocation(itemLike.asItem().getDescriptionId()), append);
    }

    public GTRecipeBuilder copyFrom(GTRecipeBuilder builder) {
        return recipeBuilder.copyFrom(builder);
    }

    public GTRecipeType onRecipeBuild(BiConsumer<GTRecipeBuilder, Consumer<FinishedRecipe>> onBuild) {
        recipeBuilder.onSave(onBuild);
        return this;
    }

    public void addDataStickEntry(@NotNull String researchId, @NotNull GTRecipe recipe) {
        Collection<GTRecipe> collection = researchEntries.computeIfAbsent(researchId, k -> new ObjectOpenHashSet<>());
        collection.add(recipe);
    }

    @Nullable
    public Collection<GTRecipe> getDataStickEntry(@NotNull String researchId) {
        return researchEntries.get(researchId);
    }

    public boolean removeDataStickEntry(@NotNull String researchId, @NotNull GTRecipe recipe) {
        Collection<GTRecipe> collection = researchEntries.get(researchId);
        if (collection == null) return false;
        if (collection.remove(recipe)) {
            if (collection.isEmpty()) {
                return researchEntries.remove(researchId) != null;
            }
            return true;
        }
        return false;
    }

    public GTRecipe toGTrecipe(ResourceLocation id, Recipe<?> recipe) {
        var builder = recipeBuilder(id);
        for (var ingredient : recipe.getIngredients()) {
            builder.inputItems(ingredient);
        }
        builder.outputItems(recipe.getResultItem(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)));
        if (recipe instanceof SmeltingRecipe smeltingRecipe) {
            builder.duration(smeltingRecipe.getCookingTime());
        }
        return GTRecipeSerializer.SERIALIZER.fromJson(id, builder.build().serializeRecipe());
    }

    @NotNull
    public List<GTRecipe> getRepresentativeRecipes() {
        List<GTRecipe> recipes = new ArrayList<>();
        for (ICustomRecipeLogic logic : customRecipeLogicRunners) {
            List<GTRecipe> logicRecipes = logic.getRepresentativeRecipes();
            if (logicRecipes != null && !logicRecipes.isEmpty()) {
                recipes.addAll(logicRecipes);
            }
        }
        return recipes;
    }


    public interface ICustomRecipeLogic {
        /**
         * @return A custom recipe to run given the current Scanner's inputs. Will be called only if a registered
         *         recipe is not found to run. Return null if no recipe should be run by your logic.
         */
        @Nullable
        GTRecipe createCustomRecipe(IRecipeCapabilityHolder holder);

        /**
         * @return A list of Recipes that are never registered, but are added to JEI to demonstrate the custom logic.
         *         Not required, can return empty or null to not add any.
         */
        @Nullable
        default List<GTRecipe> getRepresentativeRecipes() {
            return null;
        }
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setRecipeBuilder(final GTRecipeBuilder recipeBuilder) {
        this.recipeBuilder = recipeBuilder;
        return this;
    }

    public ChanceBoostFunction getChanceFunction() {
        return this.chanceFunction;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setChanceFunction(final ChanceBoostFunction chanceFunction) {
        this.chanceFunction = chanceFunction;
        return this;
    }

    public GTRecipeTypeUI getRecipeUI() {
        return this.recipeUI;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setRecipeUI(final GTRecipeTypeUI recipeUI) {
        this.recipeUI = recipeUI;
        return this;
    }

    public Byte2ObjectMap<IGuiTexture> getSlotOverlays() {
        return this.slotOverlays;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setSmallRecipeMap(final GTRecipeType smallRecipeMap) {
        this.smallRecipeMap = smallRecipeMap;
        return this;
    }

    public GTRecipeType getSmallRecipeMap() {
        return this.smallRecipeMap;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setIconSupplier(@Nullable final Supplier<ItemStack> iconSupplier) {
        this.iconSupplier = iconSupplier;
        return this;
    }

    @Nullable
    public Supplier<ItemStack> getIconSupplier() {
        return this.iconSupplier;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setSound(@Nullable final SoundEntry sound) {
        this.sound = sound;
        return this;
    }

    @Nullable
    public SoundEntry getSound() {
        return this.sound;
    }

    public List<Function<CompoundTag, String>> getDataInfos() {
        return this.dataInfos;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setFuelRecipeType(final boolean isFuelRecipeType) {
        this.isFuelRecipeType = isFuelRecipeType;
        return this;
    }

    public boolean isFuelRecipeType() {
        return this.isFuelRecipeType;
    }

    public boolean isScanner() {
        return this.isScanner;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setScanner(final boolean isScanner) {
        this.isScanner = isScanner;
        return this;
    }

    public boolean isHasResearchSlot() {
        return this.hasResearchSlot;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setHasResearchSlot(final boolean hasResearchSlot) {
        this.hasResearchSlot = hasResearchSlot;
        return this;
    }

    public Map<RecipeType<?>, List<GTRecipe>> getProxyRecipes() {
        return this.proxyRecipes;
    }

    public GTRecipeLookup getLookup() {
        return this.lookup;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setOffsetVoltageText(final boolean offsetVoltageText) {
        this.offsetVoltageText = offsetVoltageText;
        return this;
    }

    public boolean isOffsetVoltageText() {
        return this.offsetVoltageText;
    }

    /**
     * @return {@code this}.
     */
    public GTRecipeType setVoltageTextOffset(final int voltageTextOffset) {
        this.voltageTextOffset = voltageTextOffset;
        return this;
    }

    public int getVoltageTextOffset() {
        return this.voltageTextOffset;
    }

    public List<ICustomRecipeLogic> getCustomRecipeLogicRunners() {
        return this.customRecipeLogicRunners;
    }
}
