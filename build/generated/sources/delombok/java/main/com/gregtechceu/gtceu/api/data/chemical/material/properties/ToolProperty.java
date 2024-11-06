package com.gregtechceu.gtceu.api.data.chemical.material.properties;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.MaterialToolTier;
import com.gregtechceu.gtceu.config.ConfigHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import static com.gregtechceu.gtceu.api.item.tool.GTToolType.*;

public class ToolProperty implements IMaterialProperty<ToolProperty> {
    /**
     * Harvest speed of tools made from this Material.
     * <p>
     * Default: 1.0F
     */
    private float harvestSpeed;
    /**
     * Attack damage of tools made from this Material
     * <p>
     * Default: 1.0F
     */
    private float attackDamage;
    /**
     * Attack speed of tools made from this Material
     * <p>
     * Default: 0.0F
     */
    private float attackSpeed;
    /**
     * Durability of tools made from this Material.
     * <p>
     * Default: 100
     */
    private int durability;
    /**
     * Harvest level of tools made of this Material.
     * <p>
     * Default: 2 (Iron).
     */
    private int harvestLevel;
    /**
     * Enchantability of tools made from this Material.
     * <p>
     * Default: 10
     */
    private int enchantability = 10;
    /**
     * If crafting tools should not be made from this material
     */
    private boolean ignoreCraftingTools;
    /**
     * If tools made of this material should be unbreakable and ignore durability checks.
     */
    private boolean isUnbreakable;
    /**
     * If tools made of this material should be "magnetic," meaning items go
     * directly into the player's inventory instead of dropping on the ground.
     */
    private boolean isMagnetic;
    /**
     * A multiplier to the base durability for this material
     * Mostly for modpack makers
     */
    private int durabilityMultiplier = 1;
    private MaterialToolTier toolTier;
    /**
     * Gen for given type
     */
    private GTToolType[] types;
    /**
     * Enchantment to be applied to tools made from this Material.
     */
    private final Object2IntMap<Enchantment> enchantments = new Object2IntArrayMap<>();

    public ToolProperty(float harvestSpeed, float attackDamage, int durability, int harvestLevel, GTToolType[] types) {
        this.harvestSpeed = harvestSpeed;
        this.attackDamage = attackDamage;
        this.durability = durability;
        this.harvestLevel = harvestLevel;
        this.types = types;
    }

    public ToolProperty() {
        this(1.0F, 1.0F, 100, 2, GTToolType.getTypes().values().toArray(GTToolType[]::new));
    }

    public Object2IntMap<Enchantment> getEnchantments() {
        return enchantments;
    }

    @Override
    public void verifyProperty(MaterialProperties properties) {
        if (!properties.hasProperty(PropertyKey.WOOD)) {
            if (!properties.hasProperty(PropertyKey.GEM)) properties.ensureSet(PropertyKey.INGOT, true);
        }
    }

    public void addEnchantmentForTools(Enchantment enchantment, int level) {
        if (ConfigHolder.INSTANCE.recipes.enchantedTools) {
            enchantments.put(enchantment, level);
        }
    }

    public MaterialToolTier getTier(Material material) {
        if (toolTier == null) {
            toolTier = new MaterialToolTier(material);
        }
        return toolTier;
    }

    public boolean hasType(GTToolType toolType) {
        return ArrayUtils.contains(types, toolType);
    }

    public ToolProperty addTypes(GTToolType... types) {
        this.types = ArrayUtils.addAll(this.types, types);
        return this;
    }

    public ToolProperty removeTypes(GTToolType... types) {
        this.types = Arrays.stream(this.types).filter(type -> !ArrayUtils.contains(types, type)).toArray(GTToolType[]::new);
        return this;
    }


    public static class Builder {
        private final ToolProperty toolProperty;

        public static Builder of(float harvestSpeed, float attackDamage, int durability, int harvestLevel) {
            return new Builder(harvestSpeed, attackDamage, durability, harvestLevel, new GTToolType[] {SWORD, PICKAXE, SHOVEL, AXE, HOE, MINING_HAMMER, SPADE, SAW, HARD_HAMMER, 
            // SOFT_MALLET,
            WRENCH, FILE, CROWBAR, SCREWDRIVER, 
            // MORTAR,
            WIRE_CUTTER, SCYTHE, KNIFE, BUTCHERY_KNIFE, 
            // PLUNGER,
            DRILL_LV, DRILL_MV, DRILL_HV, DRILL_EV, DRILL_IV, CHAINSAW_LV, WRENCH_LV, WRENCH_HV, WRENCH_IV, BUZZSAW, SCREWDRIVER_LV, WIRE_CUTTER_LV, WIRE_CUTTER_HV, WIRE_CUTTER_IV});
        }

        public static Builder of(float harvestSpeed, float attackDamage, int durability, int harvestLevel, GTToolType... types) {
            return new Builder(harvestSpeed, attackDamage, durability, harvestLevel, types);
        }

        private Builder(float harvestSpeed, float attackDamage, int durability, int harvestLevel, GTToolType[] types) {
            toolProperty = new ToolProperty(harvestSpeed, attackDamage, durability, harvestLevel, types);
        }

        public Builder enchantability(int enchantability) {
            toolProperty.enchantability = enchantability;
            return this;
        }

        public Builder attackSpeed(float attackSpeed) {
            toolProperty.attackSpeed = attackSpeed;
            return this;
        }

        public Builder ignoreCraftingTools() {
            toolProperty.ignoreCraftingTools = true;
            return this;
        }

        public Builder unbreakable() {
            toolProperty.isUnbreakable = true;
            return this;
        }

        public Builder types(GTToolType... types) {
            toolProperty.types = types;
            return this;
        }

        public Builder addTypes(GTToolType... types) {
            toolProperty.types = ArrayUtils.addAll(toolProperty.types, types);
            return this;
        }

        public Builder enchantment(Enchantment enchantment, int level) {
            toolProperty.addEnchantmentForTools(enchantment, level);
            return this;
        }

        public Builder magnetic() {
            toolProperty.isMagnetic = true;
            return this;
        }

        public Builder durabilityMultiplier(int multiplier) {
            toolProperty.durabilityMultiplier = multiplier;
            return this;
        }

        public ToolProperty build() {
            return toolProperty;
        }
    }

    /**
     * Harvest speed of tools made from this Material.
     * <p>
     * Default: 1.0F
     */
    public float getHarvestSpeed() {
        return this.harvestSpeed;
    }

    /**
     * Harvest speed of tools made from this Material.
     * <p>
     * Default: 1.0F
     */
    public void setHarvestSpeed(final float harvestSpeed) {
        this.harvestSpeed = harvestSpeed;
    }

    /**
     * Attack damage of tools made from this Material
     * <p>
     * Default: 1.0F
     */
    public float getAttackDamage() {
        return this.attackDamage;
    }

    /**
     * Attack damage of tools made from this Material
     * <p>
     * Default: 1.0F
     */
    public void setAttackDamage(final float attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Attack speed of tools made from this Material
     * <p>
     * Default: 0.0F
     */
    public float getAttackSpeed() {
        return this.attackSpeed;
    }

    /**
     * Attack speed of tools made from this Material
     * <p>
     * Default: 0.0F
     */
    public void setAttackSpeed(final float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    /**
     * Durability of tools made from this Material.
     * <p>
     * Default: 100
     */
    public int getDurability() {
        return this.durability;
    }

    /**
     * Durability of tools made from this Material.
     * <p>
     * Default: 100
     */
    public void setDurability(final int durability) {
        this.durability = durability;
    }

    /**
     * Harvest level of tools made of this Material.
     * <p>
     * Default: 2 (Iron).
     */
    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    /**
     * Harvest level of tools made of this Material.
     * <p>
     * Default: 2 (Iron).
     */
    public void setHarvestLevel(final int harvestLevel) {
        this.harvestLevel = harvestLevel;
    }

    /**
     * Enchantability of tools made from this Material.
     * <p>
     * Default: 10
     */
    public int getEnchantability() {
        return this.enchantability;
    }

    /**
     * Enchantability of tools made from this Material.
     * <p>
     * Default: 10
     */
    public void setEnchantability(final int enchantability) {
        this.enchantability = enchantability;
    }

    /**
     * If crafting tools should not be made from this material
     */
    public boolean isIgnoreCraftingTools() {
        return this.ignoreCraftingTools;
    }

    /**
     * If crafting tools should not be made from this material
     */
    public void setIgnoreCraftingTools(final boolean ignoreCraftingTools) {
        this.ignoreCraftingTools = ignoreCraftingTools;
    }

    /**
     * If tools made of this material should be unbreakable and ignore durability checks.
     */
    public boolean isUnbreakable() {
        return this.isUnbreakable;
    }

    /**
     * If tools made of this material should be unbreakable and ignore durability checks.
     */
    public void setUnbreakable(final boolean isUnbreakable) {
        this.isUnbreakable = isUnbreakable;
    }

    /**
     * If tools made of this material should be "magnetic," meaning items go
     * directly into the player's inventory instead of dropping on the ground.
     */
    public boolean isMagnetic() {
        return this.isMagnetic;
    }

    /**
     * If tools made of this material should be "magnetic," meaning items go
     * directly into the player's inventory instead of dropping on the ground.
     */
    public void setMagnetic(final boolean isMagnetic) {
        this.isMagnetic = isMagnetic;
    }

    /**
     * A multiplier to the base durability for this material
     * Mostly for modpack makers
     */
    public int getDurabilityMultiplier() {
        return this.durabilityMultiplier;
    }

    /**
     * A multiplier to the base durability for this material
     * Mostly for modpack makers
     */
    public void setDurabilityMultiplier(final int durabilityMultiplier) {
        this.durabilityMultiplier = durabilityMultiplier;
    }

    /**
     * Gen for given type
     */
    public GTToolType[] getTypes() {
        return this.types;
    }

    /**
     * Gen for given type
     */
    public void setTypes(final GTToolType[] types) {
        this.types = types;
    }
}
