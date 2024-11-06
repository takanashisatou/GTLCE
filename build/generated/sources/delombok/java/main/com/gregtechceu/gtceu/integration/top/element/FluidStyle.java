package com.gregtechceu.gtceu.integration.top.element;

public class FluidStyle implements IFluidStyle {
    private int width = 20;
    private int height = 20;

    public FluidStyle() {
        /**/}

    public IFluidStyle copy() {
        return new FluidStyle().bounds(this.width, this.height);
    }

    public IFluidStyle width(int w) {
        this.width = w;
        return this;
    }

    public IFluidStyle height(int h) {
        this.height = h;
        return this;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
