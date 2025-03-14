package com.gregtechceu.gtceu.api.machine.multiblock.part;

import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author KilaBash
 * @date 2023/3/4
 * @implNote TieredIOPartMachine
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TieredIOPartMachine extends TieredPartMachine implements IControllable {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(TieredIOPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);
    protected final IO io;
    /**
     * AUTO IO working?
     */
    @Persisted
    @DescSynced
    @RequireRerender
    protected boolean workingEnabled;

    public TieredIOPartMachine(IMachineBlockEntity holder, int tier, IO io) {
        super(holder, tier);
        this.io = io;
        this.workingEnabled = true;
    }

    //////////////////////////////////////
    // ***** Initialization ******//
    //////////////////////////////////////
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Nullable
    @Override
    public PageGroupingData getPageGroupingData() {
        return switch (this.io) {
            case IN -> new PageGroupingData("gtceu.multiblock.page_switcher.io.import", 1);
            case OUT -> new PageGroupingData("gtceu.multiblock.page_switcher.io.export", 2);
            case BOTH -> new PageGroupingData("gtceu.multiblock.page_switcher.io.both", 3);
            case NONE -> null;
        };
    }

    /**
     * AUTO IO working?
     */
    public boolean isWorkingEnabled() {
        return this.workingEnabled;
    }

    /**
     * AUTO IO working?
     */
    public void setWorkingEnabled(final boolean workingEnabled) {
        this.workingEnabled = workingEnabled;
    }
}
