package com.gregtechceu.gtceu.common.recipe;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Screret
 * @date 2023/6/16
 * @implNote EUToStartCondition
 */
public class EUToStartCondition extends RecipeCondition {
    public static final EUToStartCondition INSTANCE = new EUToStartCondition();
    private long euToStart;

    public EUToStartCondition(long euToStart) {
        this.euToStart = euToStart;
    }

    @Override
    public String getType() {
        return "eu_to_start";
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("recipe.condition.eu_to_start.tooltip");
    }

    @Override
    public boolean test(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        return recipeLogic.getMachine().getTraits().stream().filter(IEnergyContainer.class::isInstance).anyMatch(energyContainer -> ((IEnergyContainer) energyContainer).getEnergyCapacity() > euToStart);
    }

    @Override
    public RecipeCondition createTemplate() {
        return new EUToStartCondition();
    }

    @NotNull
    @Override
    public JsonObject serialize() {
        JsonObject config = super.serialize();
        config.addProperty("euToStart", euToStart);
        return config;
    }

    @Override
    public RecipeCondition deserialize(@NotNull JsonObject config) {
        super.deserialize(config);
        euToStart = GsonHelper.getAsLong(config, "euToStart", 0);
        return this;
    }

    @Override
    public RecipeCondition fromNetwork(FriendlyByteBuf buf) {
        super.fromNetwork(buf);
        euToStart = buf.readLong();
        return this;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        super.toNetwork(buf);
        buf.writeLong(euToStart);
    }

    public EUToStartCondition() {
    }
}
