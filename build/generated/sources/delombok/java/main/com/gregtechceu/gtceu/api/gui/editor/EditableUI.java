package com.gregtechceu.gtceu.api.gui.editor;

import com.gregtechceu.gtceu.api.gui.WidgetUtils;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/7/4
 * @implNote EditableUI
 */
public class EditableUI<W extends Widget, T> implements IEditableUI<W, T> {
    final String id;
    final Class<W> clazz;
    final Supplier<W> widgetSupplier;
    final BiConsumer<W, T> binder;

    public EditableUI(String id, Class<W> clazz, Supplier<W> widgetSupplier, BiConsumer<W, T> binder) {
        this.id = id;
        this.clazz = clazz;
        this.widgetSupplier = widgetSupplier;
        this.binder = binder;
    }

    public W createDefault() {
        var widget = widgetSupplier.get();
        widget.setId(id);
        return widget;
    }

    public void setupUI(WidgetGroup template, T instance) {
        WidgetUtils.widgetByIdForEach(template, "^" + id + "$", clazz, w -> binder.accept(w, instance));
    }

    public String getId() {
        return this.id;
    }

    public Supplier<W> getWidgetSupplier() {
        return this.widgetSupplier;
    }

    public BiConsumer<W, T> getBinder() {
        return this.binder;
    }
}
