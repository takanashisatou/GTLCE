package com.gregtechceu.gtceu.common.cover.data;

import com.gregtechceu.gtceu.api.gui.widget.EnumSelectorWidget;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

public enum TransferMode implements EnumSelectorWidget.SelectableEnum {
    TRANSFER_ANY("cover.robotic_arm.transfer_mode.transfer_any", "transfer_any", 1), TRANSFER_EXACT("cover.robotic_arm.transfer_mode.transfer_exact", "transfer_exact", 1024), KEEP_EXACT("cover.robotic_arm.transfer_mode.keep_exact", "keep_exact", 1024);
    public final String tooltip;
    public final IGuiTexture icon;
    public final int maxStackSize;

    TransferMode(String tooltip, String textureName, int maxStackSize) {
        this.tooltip = tooltip;
        this.maxStackSize = maxStackSize;
        this.icon = new ResourceTexture("gtceu:textures/gui/icon/transfer_mode/" + textureName + ".png");
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public IGuiTexture getIcon() {
        return this.icon;
    }
}
