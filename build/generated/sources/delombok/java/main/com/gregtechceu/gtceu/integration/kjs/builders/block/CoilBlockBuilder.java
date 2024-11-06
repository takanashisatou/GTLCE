package com.gregtechceu.gtceu.integration.kjs.builders.block;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.block.SimpleCoilType;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.integration.kjs.builders.RendererBlockItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

public class CoilBlockBuilder extends BlockBuilder {
    public transient int temperature = 0;
    public transient int level = 0;
    public transient int energyDiscount = 1;
    public transient int tier = 0;
    @NotNull
    public transient Supplier<@Nullable Material> material = () -> null;
    public transient ResourceLocation texture = new ResourceLocation("missingno");

    public CoilBlockBuilder(ResourceLocation i) {
        super(i);
    }

    public CoilBlockBuilder coilMaterial(@NotNull Supplier<@Nullable Material> material) {
        this.material = material;
        return this;
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
    }

    @Override
    protected BlockItemBuilder getOrCreateItemBuilder() {
        return itemBuilder == null ? (itemBuilder = new RendererBlockItemBuilder(id)) : itemBuilder;
    }

    @Override
    public Block createObject() {
        SimpleCoilType coilType = new SimpleCoilType(this.id.getPath(), temperature, level, energyDiscount, tier, material, texture);
        CoilBlock result = new CoilBlock(this.createProperties(), coilType);
        GTCEuAPI.HEATING_COILS.put(coilType, () -> result);
        return result;
    }

    /**
     * @return {@code this}.
     */
    public CoilBlockBuilder temperature(final int temperature) {
        this.temperature = temperature;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public CoilBlockBuilder level(final int level) {
        this.level = level;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public CoilBlockBuilder energyDiscount(final int energyDiscount) {
        this.energyDiscount = energyDiscount;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public CoilBlockBuilder tier(final int tier) {
        this.tier = tier;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public CoilBlockBuilder texture(final ResourceLocation texture) {
        this.texture = texture;
        return this;
    }
}
