package com.gregtechceu.gtceu.integration.rei.orevein;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.BedrockOreDefinition;
import com.gregtechceu.gtceu.client.ClientProxy;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.integration.GTOreVeinWidget;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.rei.IGui2Renderer;
import com.lowdragmc.lowdraglib.rei.ModularUIDisplayCategory;
import com.lowdragmc.lowdraglib.utils.Size;
import net.minecraft.network.chat.Component;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import org.jetbrains.annotations.NotNull;

public class GTBedrockOreDisplayCategory extends ModularUIDisplayCategory<GTBedrockOreDisplay> {
    public static final CategoryIdentifier<GTBedrockOreDisplay> CATEGORY = CategoryIdentifier.of(GTCEu.id("bedrock_ore_diagram"));
    private final Renderer icon;
    private final Size size;

    public GTBedrockOreDisplayCategory() {
        this.icon = IGui2Renderer.toDrawable(new ItemStackTexture(GTMaterials.Oil.getFluid().getBucket().asItem()));
        this.size = new Size(10 + GTOreVeinWidget.width, 140);
    }

    @Override
    public CategoryIdentifier<? extends GTBedrockOreDisplay> getCategoryIdentifier() {
        return CATEGORY;
    }

    @Override
    public int getDisplayHeight() {
        return getSize().height;
    }

    @Override
    public int getDisplayWidth(GTBedrockOreDisplay display) {
        return getSize().width;
    }

    @NotNull
    @Override
    public Component getTitle() {
        return Component.translatable("gtceu.jei.bedrock_ore_diagram");
    }

    public static void registerDisplays(DisplayRegistry registry) {
        for (BedrockOreDefinition fluid : ClientProxy.CLIENT_BEDROCK_ORE_VEINS.values()) {
            registry.add(new GTBedrockOreDisplay(fluid));
        }
    }

    public static void registerWorkstations(CategoryRegistry registry) {
        registry.addWorkstations(GTBedrockOreDisplayCategory.CATEGORY, EntryStacks.of(GTItems.PROSPECTOR_HV.asStack()));
        registry.addWorkstations(GTBedrockOreDisplayCategory.CATEGORY, EntryStacks.of(GTItems.PROSPECTOR_LUV.asStack()));
    }

    public Renderer getIcon() {
        return this.icon;
    }

    public Size getSize() {
        return this.size;
    }
}
