package com.gregtechceu.gtceu.api.fluids.store;

import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

public interface FluidStorage {

    class FluidEntry {
        private Supplier<? extends Fluid> fluid;
        @Nullable
        private FluidBuilder builder;

        public FluidEntry(final Supplier<? extends Fluid> fluid, @Nullable final FluidBuilder builder) {
            this.fluid = fluid;
            this.builder = builder;
        }

        public Supplier<? extends Fluid> getFluid() {
            return this.fluid;
        }

        @Nullable
        public FluidBuilder getBuilder() {
            return this.builder;
        }

        public void setBuilder(@Nullable final FluidBuilder builder) {
            this.builder = builder;
        }
    }

    /**
     * Enqueue a fluid for registration
     *
     * @param key     the key corresponding with the fluid
     * @param builder the FluidBuilder to build
     */
    void enqueueRegistration(@NotNull FluidStorageKey key, @NotNull FluidBuilder builder);

    /**
     * @param key the key corresponding with the FluidBuilder
     * @return the fluid builder queued to be registered
     */
    @Nullable
    FluidBuilder getQueuedBuilder(@NotNull FluidStorageKey key);

    /**
     * @param key the key corresponding with the fluid
     * @return the fluid associated with the key
     */
    @Nullable
    Fluid get(@NotNull FluidStorageKey key);

    @Nullable
    FluidEntry getEntry(@NotNull FluidStorageKey key);

    /**
     * @param key   the key to associate with the fluid
     * @param fluid the fluid to associate with the key
     * @throws IllegalArgumentException if a key is already associated with another fluid
     */
    void store(@NotNull FluidStorageKey key, @NotNull Supplier<? extends Fluid> fluid, @Nullable FluidBuilder builder);
}
