package com.gregtechceu.gtceu.common.cover.data;

import com.gregtechceu.gtceu.api.gui.widget.EnumSelectorWidget;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

public enum BucketMode implements EnumSelectorWidget.SelectableEnum {
    BUCKET("cover.bucket.mode.bucket", "minecraft:textures/item/water_bucket", 1000), MILLI_BUCKET("cover.bucket.mode.milli_bucket", "gtceu:textures/gui/icon/bucket_mode/water_drop", 1);
    public final String tooltip;
    public final IGuiTexture icon;
    public final long multiplier;

    BucketMode(String tooltip, String textureName, long multiplier) {
        this.tooltip = tooltip;
        this.icon = new ResourceTexture(textureName + ".png").scale(16.0F / 20.0F);
        this.multiplier = multiplier;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public IGuiTexture getIcon() {
        return this.icon;
    }
}
