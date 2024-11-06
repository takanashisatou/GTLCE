package com.gregtechceu.gtceu.api.machine;

import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;

/**
 * @author KilaBash
 * @date 2023/2/28
 * @implNote TieredMachine
 */
public class TieredMachine extends MetaMachine implements ITieredMachine {
    protected final int tier;

    public TieredMachine(IMachineBlockEntity holder, int tier) {
        super(holder);
        this.tier = tier;
    }

    public int getTier() {
        return this.tier;
    }
}
