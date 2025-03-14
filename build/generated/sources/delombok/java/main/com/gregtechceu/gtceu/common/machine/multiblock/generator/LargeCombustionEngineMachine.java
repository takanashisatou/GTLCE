package com.gregtechceu.gtceu.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author KilaBash
 * @date 2023/7/9
 * @implNote LargeCombustionEngineMachine
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeCombustionEngineMachine extends WorkableElectricMultiblockMachine implements ITieredMachine {
    private static final FluidStack OXYGEN_STACK = GTMaterials.Oxygen.getFluid(20);
    private static final FluidStack LIQUID_OXYGEN_STACK = GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 80);
    private static final FluidStack LUBRICANT_STACK = GTMaterials.Lubricant.getFluid(1);
    private final int tier;
    // runtime
    private boolean isOxygenBoosted = false;

    public LargeCombustionEngineMachine(IMachineBlockEntity holder, int tier) {
        super(holder);
        this.tier = tier;
    }

    private boolean isIntakesObstructed() {
        var facing = this.getFrontFacing();
        boolean permuteXZ = facing.getAxis() == Direction.Axis.Z;
        var centerPos = this.getPos().relative(facing);
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                // Skip the controller block itself
                if (x == 0 && y == 0) continue;
                var blockPos = centerPos.offset(permuteXZ ? x : 0, y, permuteXZ ? 0 : x);
                var blockState = this.getLevel().getBlockState(blockPos);
                if (!blockState.isAir()) return true;
            }
        }
        return false;
    }

    private boolean isExtreme() {
        return getTier() > GTValues.EV;
    }

    public boolean isBoostAllowed() {
        return getMaxVoltage() >= GTValues.V[getTier() + 1];
    }

    //////////////////////////////////////
    // ****** Recipe Logic *******//
    //////////////////////////////////////
    @Override
    public long getOverclockVoltage() {
        if (isOxygenBoosted) return GTValues.V[tier] * 2;
         else return GTValues.V[tier];
    }

    protected GTRecipe getLubricantRecipe() {
        return GTRecipeBuilder.ofRaw().inputFluids(LUBRICANT_STACK).buildRawRecipe();
    }

    protected GTRecipe getBoostRecipe() {
        return GTRecipeBuilder.ofRaw().inputFluids(isExtreme() ? LIQUID_OXYGEN_STACK : OXYGEN_STACK).buildRawRecipe();
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
        if (machine instanceof LargeCombustionEngineMachine engineMachine) {
            var EUt = RecipeHelper.getOutputEUt(recipe);
            // has lubricant
            if (EUt > 0 && engineMachine.getLubricantRecipe().matchRecipe(engineMachine).isSuccess() && !engineMachine.isIntakesObstructed()) {
                var maxParallel = (int) (engineMachine.getOverclockVoltage() / EUt); // get maximum parallel
                var parallelResult = GTRecipeModifiers.fastParallel(engineMachine, recipe, maxParallel, false);
                if (engineMachine.isOxygenBoosted) {
                    // boost production
                    long eut = (long) (EUt * parallelResult.getSecond() * (engineMachine.isExtreme() ? 2 : 1.5));
                    result.init(-eut, recipe.duration, 1, params.getOcAmount());
                } else {
                    long eut = (long) (EUt * parallelResult.getSecond());
                    result.init(-eut, recipe.duration, 1, params.getOcAmount());
                }
                return recipe;
            }
        }
        return null;
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        // check lubricant
        final var totalContinuousRunningTime = recipeLogic.getTotalContinuousRunningTime();
        if ((totalContinuousRunningTime == 1 || totalContinuousRunningTime % 72 == 0)) {
            // insufficient lubricant
            if (!getLubricantRecipe().handleRecipeIO(IO.IN, this, this.recipeLogic.getChanceCaches())) {
                recipeLogic.interruptRecipe();
                return false;
            }
        }
        // check boost fluid
        if ((totalContinuousRunningTime == 1 || totalContinuousRunningTime % 20 == 0) && isBoostAllowed()) {
            var boosterRecipe = getBoostRecipe();
            this.isOxygenBoosted = boosterRecipe.matchRecipe(this).isSuccess() && boosterRecipe.handleRecipeIO(IO.IN, this, this.recipeLogic.getChanceCaches());
        }
        return value;
    }

    @Override
    public boolean dampingWhenWaiting() {
        return false;
    }

    //////////////////////////////////////
    // ******* GUI ********//
    //////////////////////////////////////
    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            if (isBoostAllowed()) {
                if (!isExtreme()) {
                    if (isOxygenBoosted) {
                        textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.oxygen_boosted"));
                    } else {
                        textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.supply_oxygen_to_boost"));
                    }
                } else {
                    if (isOxygenBoosted) {
                        textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.liquid_oxygen_boosted"));
                    } else {
                        textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.supply_liquid_oxygen_to_boost"));
                    }
                }
            } else {
                textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.boost_disallowed"));
            }
        }
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        super.attachTooltips(tooltipsPanel);
        tooltipsPanel.attachTooltips(new IFancyTooltip.Basic(() -> GuiTextures.INDICATOR_NO_STEAM.get(false), () -> List.of(Component.translatable("gtceu.multiblock.large_combustion_engine.obstructed").setStyle(Style.EMPTY.withColor(ChatFormatting.RED))), this::isIntakesObstructed, () -> null));
    }

    public int getTier() {
        return this.tier;
    }
}
