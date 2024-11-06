package com.gregtechceu.gtceu.common.pipelike.duct;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.pipenet.IPipeType;
import com.gregtechceu.gtceu.client.model.PipeModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import java.util.Locale;

public enum DuctPipeType implements IPipeType<DuctPipeProperties>, StringRepresentable {
    SMALL("small", 0.375F, 2.0F), NORMAL("normal", 0.5F, 4.0F), LARGE("large", 0.75F, 8.0F), HUGE("huge", 0.875F, 16.0F);
    public static final ResourceLocation TYPE_ID = GTCEu.id("duct");
    public static final DuctPipeType[] VALUES = values();
    public final String name;
    private final float thickness;
    private final float rateMultiplier;

    DuctPipeType(String name, float thickness, float rateMultiplier) {
        this.name = name;
        this.thickness = thickness;
        this.rateMultiplier = rateMultiplier;
    }

    @Override
    public DuctPipeProperties modifyProperties(DuctPipeProperties baseProperties) {
        return baseProperties;
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public ResourceLocation type() {
        return TYPE_ID;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public PipeModel createPipeModel() {
        return new PipeModel(thickness, () -> GTCEu.id("block/pipe/pipe_duct_side"), () -> GTCEu.id("block/pipe/pipe_duct_in"), null, null);
    }

    public String getName() {
        return this.name;
    }

    public float getThickness() {
        return this.thickness;
    }

    public float getRateMultiplier() {
        return this.rateMultiplier;
    }
}
