package com.gregtechceu.gtceu.integration.kjs.builders.prefix;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.core.mixins.BlockBehaviourAccessor;
import com.gregtechceu.gtceu.integration.kjs.built.KJSTagPrefix;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Supplier;
import static com.gregtechceu.gtceu.integration.kjs.Validator.*;

public class OreTagPrefixBuilder extends TagPrefixBuilder {
    public transient Supplier<BlockState> stateSupplier;
    public transient Supplier<Material> materialSupplier;
    public transient ResourceLocation baseModelLocation;
    public transient Supplier<BlockBehaviour.Properties> templateProperties;
    public transient boolean doubleDrops = false;
    public transient boolean isSand = false;
    public transient boolean shouldDropAsItem = false;

    public OreTagPrefixBuilder(ResourceLocation id, Object... args) {
        super(id, args);
    }

    @Override
    public KJSTagPrefix create(String id) {
        return KJSTagPrefix.oreTagPrefix(id);
    }

    @Override
    public TagPrefix register() {
        validate(this.id, errorIfNull(stateSupplier, "stateSupplier"), onlySetDefault(templateProperties, () -> {
            templateProperties = () -> GTBlocks.copy(((BlockBehaviourAccessor) stateSupplier.get().getBlock()).getBlockProperties(), BlockBehaviour.Properties.of());
        }), errorIfNull(baseModelLocation, "baseModelLocation"));
        return value = base.registerOre(stateSupplier, materialSupplier, templateProperties, baseModelLocation, doubleDrops, isSand, shouldDropAsItem);
    }

    /**
     * @return {@code this}.
     */
    public OreTagPrefixBuilder stateSupplier(final Supplier<BlockState> stateSupplier) {
        this.stateSupplier = stateSupplier;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public OreTagPrefixBuilder materialSupplier(final Supplier<Material> materialSupplier) {
        this.materialSupplier = materialSupplier;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public OreTagPrefixBuilder baseModelLocation(final ResourceLocation baseModelLocation) {
        this.baseModelLocation = baseModelLocation;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public OreTagPrefixBuilder templateProperties(final Supplier<BlockBehaviour.Properties> templateProperties) {
        this.templateProperties = templateProperties;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public OreTagPrefixBuilder doubleDrops(final boolean doubleDrops) {
        this.doubleDrops = doubleDrops;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public OreTagPrefixBuilder isSand(final boolean isSand) {
        this.isSand = isSand;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public OreTagPrefixBuilder shouldDropAsItem(final boolean shouldDropAsItem) {
        this.shouldDropAsItem = shouldDropAsItem;
        return this;
    }
}
