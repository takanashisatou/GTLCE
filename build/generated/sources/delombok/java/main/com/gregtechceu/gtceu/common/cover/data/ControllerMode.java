package com.gregtechceu.gtceu.common.cover.data;

import net.minecraft.core.Direction;

public enum ControllerMode {
    MACHINE("cover.machine_controller.mode.machine", null), COVER_UP("cover.machine_controller.mode.cover_up", Direction.UP), COVER_DOWN("cover.machine_controller.mode.cover_down", Direction.DOWN), COVER_NORTH("cover.machine_controller.mode.cover_north", Direction.NORTH), COVER_EAST("cover.machine_controller.mode.cover_east", Direction.EAST), COVER_SOUTH("cover.machine_controller.mode.cover_south", Direction.SOUTH), COVER_WEST("cover.machine_controller.mode.cover_west", Direction.WEST);
    public final String localeName;
    public final Direction side;

    ControllerMode(String localeName, Direction side) {
        this.localeName = localeName;
        this.side = side;
    }

    public String getLocaleName() {
        return this.localeName;
    }
}
