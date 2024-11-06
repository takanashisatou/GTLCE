package com.gregtechceu.gtceu.api.registry.registrate.forge;

import com.gregtechceu.gtceu.GTCEu;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public class GTClientFluidTypeExtensions implements IClientFluidTypeExtensions {
    public static final ResourceLocation FLUID_SCREEN_OVERLAY = GTCEu.id("textures/misc/fluid_screen_overlay.png");

    public GTClientFluidTypeExtensions(ResourceLocation stillTexture, ResourceLocation flowingTexture, int tintColor) {
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.tintColor = tintColor;
    }

    private ResourceLocation flowingTexture;
    private ResourceLocation stillTexture;
    private int tintColor;

    @Override
    public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
        return FLUID_SCREEN_OVERLAY;
    }

    public ResourceLocation getFlowingTexture() {
        return this.flowingTexture;
    }

    public ResourceLocation getStillTexture() {
        return this.stillTexture;
    }

    public void setFlowingTexture(final ResourceLocation flowingTexture) {
        this.flowingTexture = flowingTexture;
    }

    public void setStillTexture(final ResourceLocation stillTexture) {
        this.stillTexture = stillTexture;
    }

    public int getTintColor() {
        return this.tintColor;
    }

    public void setTintColor(final int tintColor) {
        this.tintColor = tintColor;
    }
}
