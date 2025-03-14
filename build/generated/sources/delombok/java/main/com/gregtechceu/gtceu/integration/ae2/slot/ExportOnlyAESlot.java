package com.gregtechceu.gtceu.integration.ae2.slot;

import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import net.minecraft.nbt.CompoundTag;
import appeng.api.stacks.GenericStack;
import org.jetbrains.annotations.Nullable;

/**
 * @author GlodBlock
 * @Description An export only slot to hold {@link appeng.api.stacks.GenericStack}
 * @date 2023/4/22-13:42
 */
public abstract class ExportOnlyAESlot implements IConfigurableSlot, ITagSerializable<CompoundTag>, IContentChangeAware {
    protected static final String CONFIG_TAG = "config";
    protected static final String STOCK_TAG = "stock";
    protected Runnable onContentsChanged = () -> {
    };
    @Nullable
    protected GenericStack config;
    @Nullable
    protected GenericStack stock;

    public ExportOnlyAESlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
        this.config = config;
        this.stock = stock;
    }

    public ExportOnlyAESlot() {
        this(null, null);
    }

    @Nullable
    public GenericStack requestStack() {
        if (this.stock != null && this.stock.amount() <= 0) {
            this.stock = null;
        }
        if (this.config == null || (this.stock != null && !this.config.what().matches(this.stock))) {
            return null;
        }
        if (this.stock == null) {
            return copy(this.config);
        }
        if (this.stock.amount() <= this.config.amount()) {
            return copy(this.config, this.config.amount() - this.stock.amount());
        }
        return null;
    }

    @Nullable
    public GenericStack exceedStack() {
        if (this.stock != null && this.stock.amount() <= 0) {
            this.stock = null;
        }
        if (this.config == null && this.stock != null) {
            return copy(this.stock);
        }
        if (this.config != null && this.stock != null) {
            if (this.config.what().matches(this.stock) && this.config.amount() < this.stock.amount()) {
                return copy(this.stock, this.stock.amount() - this.config.amount());
            }
            if (!this.config.what().matches(this.stock)) {
                return copy(this.stock);
            }
        }
        return null;
    }

    protected abstract void addStack(GenericStack stack);

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (this.config != null) {
            CompoundTag configTag = GenericStack.writeTag(this.config);
            tag.put(CONFIG_TAG, configTag);
        }
        if (this.stock != null) {
            CompoundTag stockTag = GenericStack.writeTag(this.stock);
            tag.put(STOCK_TAG, stockTag);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains(CONFIG_TAG)) {
            this.config = GenericStack.readTag(tag.getCompound(CONFIG_TAG));
        }
        if (tag.contains(STOCK_TAG)) {
            this.stock = GenericStack.readTag(tag.getCompound(STOCK_TAG));
        }
    }

    public static GenericStack copy(GenericStack stack) {
        return new GenericStack(stack.what(), stack.amount());
    }

    public static GenericStack copy(GenericStack stack, long amount) {
        return new GenericStack(stack.what(), amount);
    }

    public Runnable getOnContentsChanged() {
        return this.onContentsChanged;
    }

    public void setOnContentsChanged(final Runnable onContentsChanged) {
        this.onContentsChanged = onContentsChanged;
    }

    @Nullable
    public GenericStack getConfig() {
        return this.config;
    }

    public void setConfig(@Nullable final GenericStack config) {
        this.config = config;
    }

    @Nullable
    public GenericStack getStock() {
        return this.stock;
    }

    public void setStock(@Nullable final GenericStack stock) {
        this.stock = stock;
    }
}
