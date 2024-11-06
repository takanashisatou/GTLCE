package com.gregtechceu.gtceu.common.pipelike.item;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ItemPipeProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.pipenet.IMaterialPipeType;
import com.gregtechceu.gtceu.client.model.PipeModel;
import net.minecraft.resources.ResourceLocation;

public enum ItemPipeType implements IMaterialPipeType<ItemPipeProperties> {
    SMALL("small", 0.375F, TagPrefix.pipeSmallItem, 0.5F, 1.5F), NORMAL("normal", 0.5F, TagPrefix.pipeNormalItem, 1.0F, 1.0F), LARGE("large", 0.75F, TagPrefix.pipeLargeItem, 2.0F, 0.75F), HUGE("huge", 0.875F, TagPrefix.pipeHugeItem, 4.0F, 0.5F), RESTRICTIVE_SMALL("small_restrictive", 0.375F, TagPrefix.pipeSmallRestrictive, 0.5F, 150.0F), RESTRICTIVE_NORMAL("normal_restrictive", 0.5F, TagPrefix.pipeNormalRestrictive, 1.0F, 100.0F), RESTRICTIVE_LARGE("large_restrictive", 0.75F, TagPrefix.pipeLargeRestrictive, 2.0F, 75.0F), RESTRICTIVE_HUGE("huge_restrictive", 0.875F, TagPrefix.pipeHugeRestrictive, 4.0F, 50.0F);
    public static final ResourceLocation TYPE_ID = GTCEu.id("item");
    public static final ItemPipeType[] VALUES = values();
    public final String name;
    private final float thickness;
    private final float rateMultiplier;
    private final float resistanceMultiplier;
    private final TagPrefix tagPrefix;

    ItemPipeType(String name, float thickness, TagPrefix orePrefix, float rateMultiplier, float resistanceMultiplier) {
        this.name = name;
        this.thickness = thickness;
        this.tagPrefix = orePrefix;
        this.rateMultiplier = rateMultiplier;
        this.resistanceMultiplier = resistanceMultiplier;
    }

    public boolean isRestrictive() {
        return ordinal() > 3;
    }

    public String getSizeForTexture() {
        if (!isRestrictive()) return name;
         else return name.substring(0, name.length() - 12);
    }

    @Override
    public ItemPipeProperties modifyProperties(ItemPipeProperties baseProperties) {
        return new ItemPipeProperties((int) ((baseProperties.getPriority() * resistanceMultiplier) + 0.5), baseProperties.getTransferRate() * rateMultiplier);
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public ResourceLocation type() {
        return TYPE_ID;
    }

    public PipeModel createPipeModel(Material material) {
        PipeModel model;
        if (material.hasProperty(PropertyKey.WOOD)) {
            model = new PipeModel(thickness, () -> GTCEu.id("block/pipe/pipe_side_wood"), () -> GTCEu.id("block/pipe/pipe_%s_in_wood".formatted(this.isRestrictive() ? values()[this.ordinal() - 4].name : name)), null, null);
        } else {
            model = new PipeModel(thickness, () -> GTCEu.id("block/pipe/pipe_side"), () -> GTCEu.id("block/pipe/pipe_%s_in".formatted(this.isRestrictive() ? values()[this.ordinal() - 4].name : name)), null, null/*
                               * () -> GTCEu.id("block/pipe/pipe_side_secondary"), () ->
                               * GTCEu.id("block/pipe/pipe_%s_in_secondary".formatted(this.isRestrictive() ?
                               * values()[this.ordinal() - 4].name : name)) TODO enable once the textures are added
                               */);
        }
        if (isRestrictive()) {
            model.setSideOverlayTexture(GTCEu.id("block/pipe/pipe_restrictive"));
        }
        return model;
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

    public TagPrefix getTagPrefix() {
        return this.tagPrefix;
    }
}
