package com.gregtechceu.gtceu.integration.rei.recipe;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.rei.IGui2Renderer;
import com.lowdragmc.lowdraglib.rei.ModularUIDisplayCategory;
import com.lowdragmc.lowdraglib.utils.Size;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.function.Function;

public class GTRecipeTypeDisplayCategory extends ModularUIDisplayCategory<GTRecipeDisplay> {
    public static final Function<GTRecipeType, CategoryIdentifier<GTRecipeDisplay>> CATEGORIES = Util.memoize(recipeType -> CategoryIdentifier.of(recipeType.registryName));
    private final GTRecipeType recipeType;
    private final Renderer icon;
    private final Size size;

    public GTRecipeTypeDisplayCategory(GTRecipeType recipeType) {
        this.recipeType = recipeType;
        var size = recipeType.getRecipeUI().getJEISize();
        this.size = new Size(size.width + 8, size.height + 8);
        if (recipeType.getIconSupplier() != null) {
            icon = IGui2Renderer.toDrawable(new ItemStackTexture(recipeType.getIconSupplier().get()));
        } else {
            icon = IGui2Renderer.toDrawable(new ItemStackTexture(Items.BARRIER.getDefaultInstance()));
        }
    }

    @Override
    public CategoryIdentifier<? extends GTRecipeDisplay> getCategoryIdentifier() {
        return CATEGORIES.apply(recipeType);
    }

    @Override
    public int getDisplayHeight() {
        return getSize().height;
    }

    @Override
    public int getDisplayWidth(GTRecipeDisplay display) {
        return getSize().width;
    }

    @NotNull
    @Override
    public Component getTitle() {
        return Component.translatable(recipeType.registryName.toLanguageKey());
    }

    public static void registerDisplays(DisplayRegistry registry) {
        for (RecipeType<?> recipeType : BuiltInRegistries.RECIPE_TYPE) {
            if (recipeType instanceof GTRecipeType gtRecipeType) {
                if (Platform.isDevEnv() || gtRecipeType.getRecipeUI().isXEIVisible()) {
                    registry.registerRecipeFiller(GTRecipe.class, gtRecipeType, GTRecipeDisplay::new);
                    if (gtRecipeType.isScanner()) {
                        List<GTRecipe> scannerRecipes = gtRecipeType.getRepresentativeRecipes();
                        if (!scannerRecipes.isEmpty()) {
                            scannerRecipes.stream().map(GTRecipeDisplay::new).forEach(registry::add);
                        }
                    }
                }
            }
        }
    }

    public static void registerWorkStations(CategoryRegistry registry) {
        for (GTRecipeType gtRecipeType : GTRegistries.RECIPE_TYPES) {
            if (Platform.isDevEnv() || gtRecipeType.getRecipeUI().isXEIVisible()) {
                for (MachineDefinition machine : GTRegistries.MACHINES) {
                    if (machine.getRecipeTypes() != null) {
                        for (GTRecipeType type : machine.getRecipeTypes()) {
                            if (type == gtRecipeType) {
                                registry.addWorkstations(GTRecipeTypeDisplayCategory.CATEGORIES.apply(gtRecipeType), EntryStacks.of(machine.asStack()));
                            }
                        }
                    }
                }
            }
        }
    }

    public Renderer getIcon() {
        return this.icon;
    }

    public Size getSize() {
        return this.size;
    }
}
