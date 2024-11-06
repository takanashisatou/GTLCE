package com.gregtechceu.gtceu.api.data.chemical.material.properties;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import org.jetbrains.annotations.Nullable;

public class IngotProperty implements IMaterialProperty<IngotProperty> {
    /**
     * Specifies a material into which this material parts turn when heated
     */
    private Material smeltingInto;
    /**
     * Specifies a material into which this material parts turn when heated in arc furnace
     */
    private Material arcSmeltingInto;
    /**
     * Specifies a Material into which this Material Macerates into.
     * <p>
     * Default: this Material.
     */
    private Material macerateInto;
    /**
     * Material which obtained when this material is polarized
     */
    @Nullable
    private Material magneticMaterial;

    @Override
    public void verifyProperty(MaterialProperties properties) {
        properties.ensureSet(PropertyKey.DUST, true);
        if (properties.hasProperty(PropertyKey.GEM)) {
            throw new IllegalStateException("Material " + properties.getMaterial() + " has both Ingot and Gem Property, which is not allowed!");
        }
        if (smeltingInto == null) smeltingInto = properties.getMaterial();
         else smeltingInto.getProperties().ensureSet(PropertyKey.INGOT, true);
        if (arcSmeltingInto == null) arcSmeltingInto = properties.getMaterial();
         else arcSmeltingInto.getProperties().ensureSet(PropertyKey.INGOT, true);
        if (macerateInto == null) macerateInto = properties.getMaterial();
         else macerateInto.getProperties().ensureSet(PropertyKey.INGOT, true);
        if (magneticMaterial != null) magneticMaterial.getProperties().ensureSet(PropertyKey.INGOT, true);
    }

    /**
     * Specifies a material into which this material parts turn when heated
     */
    public Material getSmeltingInto() {
        return this.smeltingInto;
    }

    /**
     * Specifies a material into which this material parts turn when heated
     */
    public void setSmeltingInto(final Material smeltingInto) {
        this.smeltingInto = smeltingInto;
    }

    /**
     * Specifies a material into which this material parts turn when heated in arc furnace
     */
    public Material getArcSmeltingInto() {
        return this.arcSmeltingInto;
    }

    /**
     * Specifies a material into which this material parts turn when heated in arc furnace
     */
    public void setArcSmeltingInto(final Material arcSmeltingInto) {
        this.arcSmeltingInto = arcSmeltingInto;
    }

    /**
     * Specifies a Material into which this Material Macerates into.
     * <p>
     * Default: this Material.
     */
    public Material getMacerateInto() {
        return this.macerateInto;
    }

    /**
     * Specifies a Material into which this Material Macerates into.
     * <p>
     * Default: this Material.
     */
    public void setMacerateInto(final Material macerateInto) {
        this.macerateInto = macerateInto;
    }

    /**
     * Material which obtained when this material is polarized
     */
    @Nullable
    public Material getMagneticMaterial() {
        return this.magneticMaterial;
    }

    /**
     * Material which obtained when this material is polarized
     */
    public void setMagneticMaterial(@Nullable final Material magneticMaterial) {
        this.magneticMaterial = magneticMaterial;
    }
}
