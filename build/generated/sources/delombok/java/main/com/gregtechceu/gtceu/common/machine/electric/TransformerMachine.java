package com.gregtechceu.gtceu.common.machine.electric;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.UpdateListener;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author KilaBash
 * @date 2023/3/10
 * @implNote TransformerMachine
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TransformerMachine extends TieredEnergyMachine implements IControllable {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(TransformerMachine.class, TieredEnergyMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    @DescSynced
    @UpdateListener(methodName = "onTransformUpdated")
    private boolean isTransformUp;
    @Persisted
    private boolean isWorkingEnabled;
    private final int baseAmp;

    public TransformerMachine(IMachineBlockEntity holder, int tier, int baseAmp, Object... args) {
        super(holder, tier, baseAmp, args);
        this.isWorkingEnabled = true;
        this.baseAmp = baseAmp;
    }

    //////////////////////////////////////
    // ***** Initialization ******//
    //////////////////////////////////////
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @SuppressWarnings("unused")
    private void onTransformUpdated(boolean newValue, boolean oldValue) {
        scheduleRenderUpdate();
        updateEnergyContainer(newValue);
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        var amp = (args.length > 0 && args[0] instanceof Integer a) ? a : 1;
        NotifiableEnergyContainer energyContainer;
        long tierVoltage = GTValues.V[getTier()];
        // Since this.baseAmp is not yet initialized, we substitute with 1A as default
        energyContainer = new NotifiableEnergyContainer(this, tierVoltage * 8L, tierVoltage * 4, amp, tierVoltage, 4L * amp);
        energyContainer.setSideInputCondition(s -> s == getFrontFacing() && isWorkingEnabled());
        energyContainer.setSideOutputCondition(s -> s != getFrontFacing() && isWorkingEnabled());
        return energyContainer;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateEnergyContainer(isTransformUp);
    }

    public void updateEnergyContainer(boolean isTransformUp) {
        long tierVoltage = GTValues.V[getTier()];
        int lowAmperage = baseAmp * 4;
        if (isTransformUp) {
            // storage = n amp high; input = tier / 4; amperage = 4n; output = tier; amperage = n
            this.energyContainer.resetBasicInfo(tierVoltage * 8L * lowAmperage, tierVoltage, lowAmperage, tierVoltage * 4, baseAmp);
            energyContainer.setSideInputCondition(s -> s != getFrontFacing() && isWorkingEnabled());
            energyContainer.setSideOutputCondition(s -> s == getFrontFacing() && isWorkingEnabled());
        } else {
            // storage = n amp high; input = tier; amperage = n; output = tier / 4; amperage = 4n
            this.energyContainer.resetBasicInfo(tierVoltage * 8L * lowAmperage, tierVoltage * 4, baseAmp, tierVoltage, lowAmperage);
            energyContainer.setSideInputCondition(s -> s == getFrontFacing() && isWorkingEnabled());
            energyContainer.setSideOutputCondition(s -> s != getFrontFacing() && isWorkingEnabled());
        }
    }

    @Override
    public int tintColor(int index) {
        if (index == 2) {
            // frontTexture
            return GTValues.VC[getTier() + 1];
        } else if (index == 3) {
            // otherTexture
            return GTValues.VC[getTier()];
        }
        return super.tintColor(index);
    }

    //////////////////////////////////////
    // ****** Interaction *******//
    //////////////////////////////////////
    public void setTransformUp(boolean isTransformUp) {
        if (this.isTransformUp != isTransformUp && !isRemote()) {
            this.isTransformUp = isTransformUp;
            updateEnergyContainer(isTransformUp);
        }
    }

    @Override
    protected InteractionResult onScrewdriverClick(Player playerIn, InteractionHand hand, Direction gridSide, BlockHitResult hitResult) {
        if (!isRemote()) {
            setTransformUp(!isTransformUp());
            playerIn.sendSystemMessage(Component.translatable(isTransformUp() ? "gtceu.machine.transformer.message_transform_up" : "gtceu.machine.transformer.message_transform_down", energyContainer.getInputVoltage(), energyContainer.getInputAmperage(), energyContainer.getOutputVoltage(), energyContainer.getOutputAmperage()));
        }
        return InteractionResult.CONSUME;
    }

    public boolean isTransformUp() {
        return this.isTransformUp;
    }

    public boolean isWorkingEnabled() {
        return this.isWorkingEnabled;
    }

    public void setWorkingEnabled(final boolean isWorkingEnabled) {
        this.isWorkingEnabled = isWorkingEnabled;
    }

    public int getBaseAmp() {
        return this.baseAmp;
    }
}
