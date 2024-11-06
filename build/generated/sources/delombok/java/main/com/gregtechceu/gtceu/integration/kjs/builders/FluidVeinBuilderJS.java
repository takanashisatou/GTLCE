package com.gregtechceu.gtceu.integration.kjs.builders;

import com.gregtechceu.gtceu.api.data.worldgen.BiomeWeightModifier;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class FluidVeinBuilderJS {
    private final ResourceLocation id;
    private int weight; // weight value for determining which vein will appear
    private int minimumYield;
    private int maximumYield;// the [minimum, maximum) yields
    private int depletionAmount; // amount of fluid the vein gets drained by
    private int depletionChance = 1; // the chance [0, 100] that the vein will deplete by 1
    private int depletedYield; // yield after the vein is depleted
    private Supplier<Fluid> fluid; // the fluid which the vein contains
    private final List<BiomeWeightModifier> biomes = new LinkedList<>();
    private final transient Set<ResourceKey<Level>> dimensions = new HashSet<>();

    public FluidVeinBuilderJS(ResourceLocation id) {
        this.id = id;
    }

    public FluidVeinBuilderJS yield(int min, int max) {
        return minimumYield(min).maximumYield(max);
    }

    public FluidVeinBuilderJS addSpawnDimension(ResourceLocation... dimensions) {
        for (ResourceLocation dimension : dimensions) {
            this.dimensions.add(ResourceKey.create(Registries.DIMENSION, dimension));
        }
        return this;
    }

    public FluidVeinBuilderJS biomes(int weight, String biomes) {
        Registry<Biome> registry = GTRegistries.builtinRegistry().registry(Registries.BIOME).get();
        this.biomes.add(new BiomeWeightModifier(() -> biomes.startsWith("#") ? registry.getOrCreateTag(TagKey.create(Registries.BIOME, new ResourceLocation(biomes.substring(1)))) : (HolderSet.direct(registry.getHolderOrThrow(ResourceKey.create(Registries.BIOME, new ResourceLocation(biomes))))), weight));
        return this;
    }

    public FluidVeinBuilderJS biomes(int weight, String... biomes) {
        Registry<Biome> registry = GTRegistries.builtinRegistry().registry(Registries.BIOME).get();
        List<HolderSet<Biome>> biomeKeys = new LinkedList<>();
        for (String biome : biomes) {
            biomeKeys.add(biome.startsWith("#") ? registry.getOrCreateTag(TagKey.create(Registries.BIOME, new ResourceLocation(biome.substring(1)))) : HolderSet.direct(registry.getHolderOrThrow(ResourceKey.create(Registries.BIOME, new ResourceLocation(biome)))));
        }
        this.biomes.add(new BiomeWeightModifier(() -> HolderSet.direct(biomeKeys.stream().flatMap(HolderSet::stream).toList()), weight));
        return this;
    }

    @HideFromJS
    public BedrockFluidDefinition build() {
        return new BedrockFluidDefinition(id, weight, minimumYield, maximumYield, depletionAmount, depletionChance, depletedYield, fluid, biomes, dimensions);
    }

    /**
     * @return {@code this}.
     */
    public FluidVeinBuilderJS weight(final int weight) {
        this.weight = weight;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public FluidVeinBuilderJS minimumYield(final int minimumYield) {
        this.minimumYield = minimumYield;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public FluidVeinBuilderJS maximumYield(final int maximumYield) {
        this.maximumYield = maximumYield;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public FluidVeinBuilderJS depletionAmount(final int depletionAmount) {
        this.depletionAmount = depletionAmount;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public FluidVeinBuilderJS depletionChance(final int depletionChance) {
        this.depletionChance = depletionChance;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public FluidVeinBuilderJS depletedYield(final int depletedYield) {
        this.depletedYield = depletedYield;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public FluidVeinBuilderJS fluid(final Supplier<Fluid> fluid) {
        this.fluid = fluid;
        return this;
    }
}
