package com.gregtechceu.gtceu.api.recipe.logic;

public final class OCParams {
    private long eut;
    private int duration;
    private int ocAmount;

    public void initialize(long eut, int duration, int ocAmount) {
        this.eut = eut;
        this.duration = duration;
        this.ocAmount = ocAmount;
    }

    public void reset() {
        this.eut = 0L;
        this.duration = 0;
        this.ocAmount = 0;
    }

    public void setEut(final long eut) {
        this.eut = eut;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public void setOcAmount(final int ocAmount) {
        this.ocAmount = ocAmount;
    }

    public long getEut() {
        return this.eut;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getOcAmount() {
        return this.ocAmount;
    }
}
