package com.gregtechceu.gtceu.api.misc;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler.handleIngredient;

/**
 * @author KilaBash
 * @date 2023/7/13
 * @implNote ItemRecipeHandler
 */
public class ItemRecipeHandler implements IRecipeHandler<Ingredient> {
    public final IO handlerIO;
    public final ItemStackTransfer storage;

    public ItemRecipeHandler(IO handlerIO, int slots) {
        this.handlerIO = handlerIO;
        this.storage = new ItemStackTransfer(slots);
    }

    @Override
    public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, @Nullable String slotName, boolean simulate) {
        return handleIngredient(io, recipe, left, simulate, this.handlerIO, storage);
    }

    @Override
    public List<Object> getContents() {
        List<ItemStack> ingredients = new ArrayList<>();
        for (int i = 0; i < storage.getSlots(); ++i) {
            ItemStack stack = storage.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ingredients.add(stack);
            }
        }
        return Arrays.asList(ingredients.toArray());
    }

    @Override
    public double getTotalContentAmount() {
        long amount = 0;
        for (int i = 0; i < storage.getSlots(); ++i) {
            ItemStack stack = storage.getStackInSlot(i);
            if (!stack.isEmpty()) {
                amount += stack.getCount();
            }
        }
        return amount;
    }

    @Override
    public int getSize() {
        return this.storage.getSlots();
    }

    @Override
    public RecipeCapability<Ingredient> getCapability() {
        return ItemRecipeCapability.CAP;
    }

    public IO getHandlerIO() {
        return this.handlerIO;
    }
}
