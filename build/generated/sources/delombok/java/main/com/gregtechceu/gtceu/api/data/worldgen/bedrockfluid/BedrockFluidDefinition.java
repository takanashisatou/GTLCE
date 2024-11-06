package com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid;

import com.gregtechceu.gtceu.api.data.worldgen.BiomeWeightModifier;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.utils.RegistryUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.rhino.util.HideFromJS;
import org.jetbrains.annotations.ApiStatus;
import java.util.*;
import java.util.function.Supplier;

public class BedrockFluidDefinition {
    public static final MapCodec<Pair<Integer, Integer>> YIELD = Codec.mapPair(Codec.INT.fieldOf("min"), Codec.INT.fieldOf("max"));
    public static final Codec<BedrockFluidDefinition> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.INT.fieldOf("weight").forGetter(ft -> ft.weight), YIELD.fieldOf("yield").forGetter(ft -> Pair.of(ft.minimumYield, ft.maximumYield)), Codec.INT.fieldOf("depletion_amount").forGetter(ft -> ft.depletionAmount), Codec.INT.fieldOf("depletion_chance").forGetter(ft -> ft.depletionChance), Codec.INT.fieldOf("depleted_yield").forGetter(ft -> ft.depletedYield), BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(ft -> ft.storedFluid.get()), BiomeWeightModifier.CODEC.listOf().optionalFieldOf("weight_modifier", List.of()).forGetter(ft -> ft.originalModifiers), ResourceKey.codec(Registries.DIMENSION).listOf().fieldOf("dimension_filter").forGetter(ft -> new ArrayList<>(ft.dimensionFilter))).apply(instance, (weight, yield, depletionAmount, depletionChance, depletedYield, storedFluid, biomeWeightModifier, dimensionFilter) -> new BedrockFluidDefinition(weight, yield.getFirst(), yield.getSecond(), depletionAmount, depletionChance, depletedYield, () -> storedFluid, biomeWeightModifier, new HashSet<>(dimensionFilter))));
    private int weight; // weight value for determining which vein will appear
    private int minimumYield;
    private int maximumYield;// the [minimum, maximum) yields
    private int depletionAmount; // amount of fluid the vein gets drained by
    private int depletionChance; // the chance [0, 100] that the vein will deplete by 1
    private int depletedYield; // yield after the vein is depleted
    private Supplier<Fluid> storedFluid; // the fluid which the vein contains
    private BiomeWeightModifier biomeWeightModifier; // weighting of biomes
    private List<BiomeWeightModifier> originalModifiers; // weighting of biomes
    public Set<ResourceKey<Level>> dimensionFilter; // filtering of dimensions

    public BedrockFluidDefinition(ResourceLocation name, int weight, int minimumYield, int maximumYield, int depletionAmount, int depletionChance, int depletedYield, Supplier<Fluid> storedFluid, List<BiomeWeightModifier> originalModifiers, Set<ResourceKey<Level>> dimensionFilter) {
        this(weight, minimumYield, maximumYield, depletionAmount, depletionChance, depletedYield, storedFluid, originalModifiers, dimensionFilter);
        GTRegistries.BEDROCK_FLUID_DEFINITIONS.register(name, this);
    }

    public BedrockFluidDefinition(int weight, int minimumYield, int maximumYield, int depletionAmount, int depletionChance, int depletedYield, Supplier<Fluid> storedFluid, List<BiomeWeightModifier> originalModifiers, Set<ResourceKey<Level>> dimensionFilter) {
        this.weight = weight;
        this.minimumYield = minimumYield;
        this.maximumYield = maximumYield;
        this.depletionAmount = depletionAmount;
        this.depletionChance = depletionChance;
        this.depletedYield = depletedYield;
        this.storedFluid = storedFluid;
        this.originalModifiers = originalModifiers;
        this.biomeWeightModifier = new BiomeWeightModifier(() -> HolderSet.direct(originalModifiers.stream().flatMap(mod -> mod.biomes.get().stream()).toList()), originalModifiers.stream().mapToInt(mod -> mod.addedWeight).sum()) {
            @Override
            public Integer apply(Holder<Biome> biome) {
                int mod = 0;
                for (var modifier : originalModifiers) {
                    if (modifier.biomes.get().contains(biome)) {
                        mod += modifier.apply(biome);
                    }
                }
                return mod;
            }
        };
        this.dimensionFilter = dimensionFilter;
    }

    public void setOriginalModifiers(List<BiomeWeightModifier> modifiers) {
        this.originalModifiers = modifiers;
        this.biomeWeightModifier = new BiomeWeightModifier(() -> HolderSet.direct(originalModifiers.stream().flatMap(mod -> mod.biomes.get().stream()).toList()), originalModifiers.stream().mapToInt(mod -> mod.addedWeight).sum()) {
            @Override
            public Integer apply(Holder<Biome> biome) {
                int mod = 0;
                for (var modifier : originalModifiers) {
                    if (modifier.biomes.get().contains(biome)) {
                        mod += modifier.apply(biome);
                    }
                }
                return mod;
            }
        };
    }

    public static Builder builder(ResourceLocation name) {
        return new Builder(name);
    }


    public static class Builder {
        private final ResourceLocation name;
        private int weight; // weight value for determining which vein will appear
        private int minimumYield;
        private int maximumYield;// the [minimum, maximum) yields
        private int depletionAmount; // amount of fluid the vein gets drained by
        private int depletionChance = 1; // the chance [0, 100] that the vein will deplete by 1
        private int depletedYield; // yield after the vein is depleted
        private Supplier<Fluid> fluid; // the fluid which the vein contains
        private Set<ResourceKey<Level>> dimensions;
        private final List<BiomeWeightModifier> biomes = new LinkedList<>();

        private Builder(ResourceLocation name) {
            this.name = name;
        }

        public Builder copy(ResourceLocation name) {
            var copied = new Builder(name);
            copied.weight = weight;
            copied.minimumYield = minimumYield;
            copied.maximumYield = maximumYield;
            copied.depletionAmount = depletionAmount;
            copied.depletionChance = depletionChance;
            copied.depletedYield = depletedYield;
            copied.fluid = fluid;
            return copied;
        }

        public Builder yield(int min, int max) {
            return minimumYield(min).maximumYield(max);
        }

        public Builder biomes(int weight, TagKey<Biome> biomes) {
            this.biomes.add(new BiomeWeightModifier(() -> GTRegistries.builtinRegistry().registryOrThrow(Registries.BIOME).getOrCreateTag(biomes), weight));
            return this;
        }

        @SafeVarargs
        public final Builder biomes(int weight, ResourceKey<Biome>... biomes) {
            this.biomes.add(new BiomeWeightModifier(() -> HolderSet.direct(GTRegistries.builtinRegistry().registryOrThrow(Registries.BIOME)::getHolderOrThrow, biomes), weight));
            return this;
        }

        public Builder biomes(int weight, HolderSet<Biome> biomes) {
            this.biomes.add(new BiomeWeightModifier(() -> biomes, weight));
            return this;
        }

        @HideFromJS
        public Builder dimensions(Set<ResourceKey<Level>> dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder dimensions(String... dimensions) {
            return this.dimensions(new HashSet<>(RegistryUtil.resolveResourceKeys(Registries.DIMENSION, dimensions)));
        }

        @ApiStatus.Internal
        public BedrockFluidDefinition build() {
            return new BedrockFluidDefinition(weight, minimumYield, maximumYield, depletionAmount, depletionChance, depletedYield, fluid, biomes, dimensions);
        }

        public BedrockFluidDefinition register() {
            var definition = build();
            GTRegistries.BEDROCK_FLUID_DEFINITIONS.registerOrOverride(name, definition);
            return definition;
        }

        /**
         * @return {@code this}.
         */
        public BedrockFluidDefinition.Builder weight(final int weight) {
            this.weight = weight;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public BedrockFluidDefinition.Builder minimumYield(final int minimumYield) {
            this.minimumYield = minimumYield;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public BedrockFluidDefinition.Builder maximumYield(final int maximumYield) {
            this.maximumYield = maximumYield;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public BedrockFluidDefinition.Builder depletionAmount(final int depletionAmount) {
            this.depletionAmount = depletionAmount;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public BedrockFluidDefinition.Builder depletionChance(final int depletionChance) {
            this.depletionChance = depletionChance;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public BedrockFluidDefinition.Builder depletedYield(final int depletedYield) {
            this.depletedYield = depletedYield;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public BedrockFluidDefinition.Builder fluid(final Supplier<Fluid> fluid) {
            this.fluid = fluid;
            return this;
        }
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    public int getMinimumYield() {
        return this.minimumYield;
    }

    public int getMaximumYield() {
        return this.maximumYield;
    }

    public void setMinimumYield(final int minimumYield) {
        this.minimumYield = minimumYield;
    }

    public void setMaximumYield(final int maximumYield) {
        this.maximumYield = maximumYield;
    }

    public int getDepletionAmount() {
        return this.depletionAmount;
    }

    public void setDepletionAmount(final int depletionAmount) {
        this.depletionAmount = depletionAmount;
    }

    public int getDepletionChance() {
        return this.depletionChance;
    }

    public void setDepletionChance(final int depletionChance) {
        this.depletionChance = depletionChance;
    }

    public int getDepletedYield() {
        return this.depletedYield;
    }

    public void setDepletedYield(final int depletedYield) {
        this.depletedYield = depletedYield;
    }

    public Supplier<Fluid> getStoredFluid() {
        return this.storedFluid;
    }

    public void setStoredFluid(final Supplier<Fluid> storedFluid) {
        this.storedFluid = storedFluid;
    }

    public BiomeWeightModifier getBiomeWeightModifier() {
        return this.biomeWeightModifier;
    }

    public Set<ResourceKey<Level>> getDimensionFilter() {
        return this.dimensionFilter;
    }

    public void setDimensionFilter(final Set<ResourceKey<Level>> dimensionFilter) {
        this.dimensionFilter = dimensionFilter;
    }
}
