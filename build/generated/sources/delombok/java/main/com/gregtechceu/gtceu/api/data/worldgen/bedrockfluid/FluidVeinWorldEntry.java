package com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid;

import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.config.ConfigHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author KilaBash
 * @date 2023/7/11
 * @implNote FluidVeinWorldEntry
 */
public class FluidVeinWorldEntry {
    @Nullable
    private BedrockFluidDefinition vein;
    private int fluidYield;
    private int operationsRemaining;

    public FluidVeinWorldEntry(@Nullable BedrockFluidDefinition vein, int fluidYield, int operationsRemaining) {
        this.vein = vein;
        this.fluidYield = fluidYield;
        this.operationsRemaining = operationsRemaining;
    }

    private FluidVeinWorldEntry() {
    }

    public BedrockFluidDefinition getDefinition() {
        return this.vein;
    }

    @SuppressWarnings("unused")
    public void setOperationsRemaining(int amount) {
        this.operationsRemaining = amount;
    }

    public void decreaseOperations(int amount) {
        operationsRemaining = ConfigHolder.INSTANCE.worldgen.oreVeins.infiniteBedrockOresFluids ? operationsRemaining : Math.max(0, operationsRemaining - amount);
    }

    public CompoundTag writeToNBT() {
        var tag = new CompoundTag();
        tag.putInt("fluidYield", fluidYield);
        tag.putInt("operationsRemaining", operationsRemaining);
        if (vein != null && GTRegistries.BEDROCK_FLUID_DEFINITIONS.getKey(vein) != null) {
            tag.putString("vein", GTRegistries.BEDROCK_FLUID_DEFINITIONS.getKey(vein).toString());
        }
        return tag;
    }

    @NotNull
    public static FluidVeinWorldEntry readFromNBT(@NotNull CompoundTag tag) {
        FluidVeinWorldEntry info = new FluidVeinWorldEntry();
        info.fluidYield = tag.getInt("fluidYield");
        info.operationsRemaining = tag.getInt("operationsRemaining");
        if (tag.contains("vein")) {
            ResourceLocation id = new ResourceLocation(tag.getString("vein"));
            if (GTRegistries.BEDROCK_FLUID_DEFINITIONS.containKey(id)) {
                info.vein = GTRegistries.BEDROCK_FLUID_DEFINITIONS.get(id);
            }
        }
        return info;
    }

    @Nullable
    public BedrockFluidDefinition getVein() {
        return this.vein;
    }

    public int getFluidYield() {
        return this.fluidYield;
    }

    public int getOperationsRemaining() {
        return this.operationsRemaining;
    }
}
