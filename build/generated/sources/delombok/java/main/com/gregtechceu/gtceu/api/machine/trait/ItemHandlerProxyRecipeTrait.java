package com.gregtechceu.gtceu.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.List;

public class ItemHandlerProxyRecipeTrait extends NotifiableRecipeHandlerTrait<Ingredient> implements ICapabilityTrait {
    public final IO handlerIO;
    public final IO capabilityIO;
    private long timeStamp;
    private boolean enabled;
    private final Collection<NotifiableRecipeHandlerTrait<Ingredient>> handlers;

    public ItemHandlerProxyRecipeTrait(MetaMachine machine, Collection<NotifiableRecipeHandlerTrait<Ingredient>> handlers, IO handlerIO, IO capabilityIO) {
        super(machine);
        this.timeStamp = Long.MIN_VALUE;
        this.handlerIO = handlerIO;
        this.capabilityIO = capabilityIO;
        this.handlers = handlers;
    }

    @Override
    public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, @Nullable String slotName, boolean simulate) {
        if (!enabled) return left;
        for (IRecipeHandler<Ingredient> handler : handlers) {
            handler.handleRecipeInner(io, recipe, left, slotName, simulate);
            if (left.isEmpty()) return null;
        }
        return left;
    }

    @Override
    public List<Object> getContents() {
        List<Object> contents = new ObjectArrayList<>(2);
        for (NotifiableRecipeHandlerTrait<Ingredient> handler : handlers) {
            contents.addAll(handler.getContents());
        }
        return contents;
    }

    @Override
    public int getSize() {
        int size = 0;
        for (NotifiableRecipeHandlerTrait<Ingredient> handlerTrait : handlers) {
            size += handlerTrait.getSize();
        }
        return size;
    }

    @Override
    public double getTotalContentAmount() {
        long amount = 0;
        for (NotifiableRecipeHandlerTrait<Ingredient> handlerTrait : handlers) {
            amount += handlerTrait.getTotalContentAmount();
        }
        return amount;
    }

    @Override
    public RecipeCapability<Ingredient> getCapability() {
        return ItemRecipeCapability.CAP;
    }

    @Override
    public boolean isDistinct() {
        for (NotifiableRecipeHandlerTrait<Ingredient> handler : handlers) {
            if (!handler.isDistinct) return false;
        }
        return true;
    }

    @Override
    public void setDistinct(boolean distinct) {
        handlers.forEach(handler -> handler.setDistinct(distinct));
        recomputeEnabledState();
    }

    @Override
    public boolean isProxy() {
        return true;
    }

    public void recomputeEnabledState() {
        this.enabled = isDistinct();
    }

    public IO getHandlerIO() {
        return this.handlerIO;
    }

    public IO getCapabilityIO() {
        return this.capabilityIO;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(final long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Collection<NotifiableRecipeHandlerTrait<Ingredient>> getHandlers() {
        return this.handlers;
    }
}
