package com.gregtechceu.gtceu.api.data.chemical.material.properties;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import net.minecraft.util.Mth;
import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OreProperty implements IMaterialProperty<OreProperty> {
    /**
     * List of Ore byproducts.
     * <p>
     * Default: none, meaning only this property's Material.
     */
    private final List<Material> oreByProducts = new ArrayList<>();
    /**
     * Crushed Ore output amount multiplier during Maceration.
     * <p>
     * Default: 1 (no multiplier).
     */
    private int oreMultiplier;
    /**
     * Byproducts output amount multiplier during Maceration.
     * <p>
     * Default: 1 (no multiplier).
     */
    private int byProductMultiplier;
    /**
     * Should ore block use the emissive texture.
     * <p>
     * Default: false.
     */
    private boolean emissive;
    /**
     * Material to which smelting of this Ore will result.
     * <p>
     * Material will have a Dust Property.
     * Default: none.
     */
    @Nullable
    private Material directSmeltResult;
    /**
     * Material in which this Ore should be washed to give additional output.
     * <p>
     * Material will have a Fluid Property.
     * Default: none.
     */
    @Nullable
    private Material washedIn;
    /**
     * The amount of Material that the ore should be washed in
     * in the Chemical Bath.
     * <p>
     * Default 100 mb
     */
    private int washedAmount = 100;
    /**
     * During Electromagnetic Separation, this Ore will be separated
     * into this Material and the Material specified by this field.
     * Limit 2 Materials
     * <p>
     * Material will have a Dust Property.
     * Default: none.
     */
    private final List<Material> separatedInto = new ArrayList<>();

    public OreProperty(int oreMultiplier, int byProductMultiplier) {
        this.oreMultiplier = oreMultiplier;
        this.byProductMultiplier = byProductMultiplier;
        this.emissive = false;
    }

    public OreProperty(int oreMultiplier, int byProductMultiplier, boolean emissive) {
        this.oreMultiplier = oreMultiplier;
        this.byProductMultiplier = byProductMultiplier;
        this.emissive = emissive;
    }

    /**
     * Default values constructor.
     */
    public OreProperty() {
        this(1, 1);
    }

    public void setWashedIn(@Nullable Material m, int washedAmount) {
        this.washedIn = m;
        this.washedAmount = washedAmount;
    }

    public Pair<Material, Integer> getWashedIn() {
        return Pair.of(this.washedIn, this.washedAmount);
    }

    public void setSeparatedInto(Material... materials) {
        this.separatedInto.addAll(Arrays.asList(materials));
    }

    public void setOreByProducts(Material... materials) {
        this.oreByProducts.addAll(Arrays.asList(materials));
    }

    @Nullable
    public final Material getOreByProduct(int index) {
        if (this.oreByProducts.isEmpty()) return null;
        return this.oreByProducts.get(Mth.clamp(index, 0, this.oreByProducts.size() - 1));
    }

    @NotNull
    public final Material getOreByProduct(int index, @NotNull Material fallback) {
        Material material = getOreByProduct(index);
        return material != null ? material : fallback;
    }

    @Override
    public void verifyProperty(MaterialProperties properties) {
        properties.ensureSet(PropertyKey.DUST, true);
        if (directSmeltResult != null) directSmeltResult.getProperties().ensureSet(PropertyKey.DUST, true);
        if (washedIn != null) washedIn.getProperties().ensureSet(PropertyKey.FLUID, true);
        separatedInto.forEach(m -> m.getProperties().ensureSet(PropertyKey.DUST, true));
        oreByProducts.forEach(m -> m.getProperties().ensureSet(PropertyKey.DUST, true));
    }

    /**
     * List of Ore byproducts.
     * <p>
     * Default: none, meaning only this property's Material.
     */
    public List<Material> getOreByProducts() {
        return this.oreByProducts;
    }

    /**
     * Crushed Ore output amount multiplier during Maceration.
     * <p>
     * Default: 1 (no multiplier).
     */
    public int getOreMultiplier() {
        return this.oreMultiplier;
    }

    /**
     * Crushed Ore output amount multiplier during Maceration.
     * <p>
     * Default: 1 (no multiplier).
     */
    public void setOreMultiplier(final int oreMultiplier) {
        this.oreMultiplier = oreMultiplier;
    }

    /**
     * Byproducts output amount multiplier during Maceration.
     * <p>
     * Default: 1 (no multiplier).
     */
    public int getByProductMultiplier() {
        return this.byProductMultiplier;
    }

    /**
     * Byproducts output amount multiplier during Maceration.
     * <p>
     * Default: 1 (no multiplier).
     */
    public void setByProductMultiplier(final int byProductMultiplier) {
        this.byProductMultiplier = byProductMultiplier;
    }

    /**
     * Should ore block use the emissive texture.
     * <p>
     * Default: false.
     */
    public boolean isEmissive() {
        return this.emissive;
    }

    /**
     * Should ore block use the emissive texture.
     * <p>
     * Default: false.
     */
    public void setEmissive(final boolean emissive) {
        this.emissive = emissive;
    }

    /**
     * Material to which smelting of this Ore will result.
     * <p>
     * Material will have a Dust Property.
     * Default: none.
     */
    @Nullable
    public Material getDirectSmeltResult() {
        return this.directSmeltResult;
    }

    /**
     * Material to which smelting of this Ore will result.
     * <p>
     * Material will have a Dust Property.
     * Default: none.
     */
    public void setDirectSmeltResult(@Nullable final Material directSmeltResult) {
        this.directSmeltResult = directSmeltResult;
    }

    /**
     * Material in which this Ore should be washed to give additional output.
     * <p>
     * Material will have a Fluid Property.
     * Default: none.
     */
    public void setWashedIn(@Nullable final Material washedIn) {
        this.washedIn = washedIn;
    }

    /**
     * During Electromagnetic Separation, this Ore will be separated
     * into this Material and the Material specified by this field.
     * Limit 2 Materials
     * <p>
     * Material will have a Dust Property.
     * Default: none.
     */
    public List<Material> getSeparatedInto() {
        return this.separatedInto;
    }
}
