package com.gregtechceu.gtceu.common.recipe;

import com.gregtechceu.gtceu.api.capability.ICleanroomReceiver;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.config.ConfigHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class CleanroomCondition extends RecipeCondition {
    public static final CleanroomCondition INSTANCE = new CleanroomCondition();
    private CleanroomType cleanroom = CleanroomType.CLEANROOM;

    @Override
    public String getType() {
        return "cleanroom";
    }

    @Override
    public Component getTooltips() {
        return cleanroom == null ? null : Component.translatable("gtceu.recipe.cleanroom", Component.translatable(cleanroom.getTranslationKey()));
    }

    @Override
    public boolean test(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        if (!ConfigHolder.INSTANCE.machines.enableCleanroom) return true;
        MetaMachine machine = recipeLogic.getMachine();
        if (machine instanceof ICleanroomReceiver receiver && this.cleanroom != null) {
            if (ConfigHolder.INSTANCE.machines.cleanMultiblocks && machine instanceof IMultiController) return true;
            ICleanroomProvider provider = receiver.getCleanroom();
            if (provider == null) return false;
            return provider.isClean() && provider.getTypes().contains(this.cleanroom);
        }
        return true;
    }

    @NotNull
    @Override
    public JsonObject serialize() {
        JsonObject value = super.serialize();
        value.addProperty("cleanroom", cleanroom.getName());
        return value;
    }

    @Override
    public RecipeCondition deserialize(@NotNull JsonObject config) {
        super.deserialize(config);
        this.cleanroom = CleanroomType.getByNameOrDefault(GsonHelper.getAsString(config, "cleanroom", "cleanroom"));
        return this;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        super.toNetwork(buf);
        buf.writeUtf(this.cleanroom.getName());
    }

    @Override
    public RecipeCondition fromNetwork(FriendlyByteBuf buf) {
        super.fromNetwork(buf);
        this.cleanroom = CleanroomType.getByNameOrDefault(buf.readUtf());
        return this;
    }

    @Override
    public RecipeCondition createTemplate() {
        return new CleanroomCondition();
    }

    public CleanroomCondition(final CleanroomType cleanroom) {
        this.cleanroom = cleanroom;
    }

    public CleanroomCondition() {
    }

    public CleanroomType getCleanroom() {
        return this.cleanroom;
    }
}
