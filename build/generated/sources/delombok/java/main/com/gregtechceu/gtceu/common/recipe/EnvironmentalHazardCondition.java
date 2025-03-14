package com.gregtechceu.gtceu.common.recipe;

import com.gregtechceu.gtceu.api.data.medicalcondition.MedicalCondition;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.common.capability.EnvironmentalHazardSavedData;
import com.gregtechceu.gtceu.common.data.GTMedicalConditions;
import com.gregtechceu.gtceu.config.ConfigHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class EnvironmentalHazardCondition extends RecipeCondition {
    public static final EnvironmentalHazardCondition INSTANCE = new EnvironmentalHazardCondition();
    private MedicalCondition condition = GTMedicalConditions.CARBON_MONOXIDE_POISONING;

    @Override
    public String getType() {
        return "environmental_hazard";
    }

    @Override
    public Component getTooltips() {
        return isReverse ? Component.translatable("gtceu.recipe.environmental_hazard.reverse", Component.translatable("gtceu.medical_condition." + condition.name)) : Component.translatable("gtceu.recipe.environmental_hazard", Component.translatable("gtceu.medical_condition." + condition.name));
    }

    @Override
    public boolean test(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        if (!ConfigHolder.INSTANCE.gameplay.hazardsEnabled) return true;
        if (!(recipeLogic.getMachine().getLevel() instanceof ServerLevel serverLevel)) {
            return false;
        }
        EnvironmentalHazardSavedData savedData = EnvironmentalHazardSavedData.getOrCreate(serverLevel);
        var zone = savedData.getZoneByContainedPos(recipeLogic.getMachine().getPos());
        return zone != null && zone.strength() > 0;
    }

    @NotNull
    @Override
    public JsonObject serialize() {
        JsonObject value = super.serialize();
        value.addProperty("condition", condition.name);
        return value;
    }

    @Override
    public RecipeCondition deserialize(@NotNull JsonObject config) {
        super.deserialize(config);
        this.condition = MedicalCondition.CONDITIONS.get(GsonHelper.getAsString(config, "condition"));
        return this;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        super.toNetwork(buf);
        buf.writeUtf(this.condition.name);
    }

    @Override
    public RecipeCondition fromNetwork(FriendlyByteBuf buf) {
        super.fromNetwork(buf);
        this.condition = MedicalCondition.CONDITIONS.get(buf.readUtf());
        return this;
    }

    @Override
    public RecipeCondition createTemplate() {
        return new EnvironmentalHazardCondition();
    }

    public EnvironmentalHazardCondition() {
    }

    public EnvironmentalHazardCondition(final MedicalCondition condition) {
        this.condition = condition;
    }

    public MedicalCondition getCondition() {
        return this.condition;
    }
}
