package com.gregtechceu.gtceu.api.machine.fancyconfigurator;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.FluidStorage;
import net.minecraft.network.chat.Component;
import java.util.Collections;
import java.util.List;

public class FancyTankConfigurator implements IFancyConfigurator {
    private final FluidStorage[] tanks;
    private final Component title;
    private List<Component> tooltips = Collections.emptyList();

    public FancyTankConfigurator(FluidStorage[] tanks, Component title) {
        this.tanks = tanks;
        this.title = title;
    }

    @Override
    public IGuiTexture getIcon() {
        return GuiTextures.BUTTON_FLUID_OUTPUT;
    }

    @Override
    public Widget createConfigurator() {
        int rowSize = (int) Math.sqrt(tanks.length);
        int colSize = rowSize;
        if (tanks.length == 8) {
            rowSize = 4;
            colSize = 2;
        }
        var group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * colSize + 16);
        var container = new WidgetGroup(4, 4, 18 * rowSize + 8, 18 * colSize + 8);
        int index = 0;
        for (int y = 0; y < colSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                container.addWidget(new TankWidget(tanks[index++], 4 + x * 18, 4 + y * 18, true, true).setBackground(GuiTextures.FLUID_SLOT));
            }
        }
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(container);
        return group;
    }

    public Component getTitle() {
        return this.title;
    }

    public List<Component> getTooltips() {
        return this.tooltips;
    }

    /**
     * @return {@code this}.
     */
    public FancyTankConfigurator setTooltips(final List<Component> tooltips) {
        this.tooltips = tooltips;
        return this;
    }
}
