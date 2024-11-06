package com.gregtechceu.gtceu.common.cover.data;

import com.gregtechceu.gtceu.api.gui.widget.EnumSelectorWidget;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

public enum VoidingMode implements EnumSelectorWidget.SelectableEnum {
    VOID_ANY("cover.voiding.voiding_mode.void_any", "void_any", 1), VOID_OVERFLOW("cover.voiding.voiding_mode.void_overflow", "void_overflow", 1024);
    public final String tooltip;
    public final IGuiTexture icon;
    public final int maxStackSize;

    VoidingMode(String tooltip, String textureName, int maxStackSize) {
        this.tooltip = tooltip;
        this.maxStackSize = maxStackSize;
        this.icon = new ResourceTexture("gtceu:textures/gui/icon/voiding_mode/" + textureName + ".png");
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public IGuiTexture getIcon() {
        return this.icon;
    }
}
