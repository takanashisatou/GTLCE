package com.gregtechceu.gtceu.api.recipe.logic;

public final class OCResult {
    private long eut;
    private long parallelEUt;
    private int duration;
    private int parallel;
    private int ocLevel;

    public void init(long eut, int duration, int ocLevel) {
        init(eut, duration, 0, ocLevel);
    }

    public void init(long eut, int duration, int parallel, int ocLevel) {
        init(eut, duration, parallel, parallel == 0 ? eut : eut * parallel, ocLevel);
    }

    public void init(long eut, int duration, int parallel, long parallelEUt, int ocLevel) {
        this.eut = eut;
        this.duration = duration;
        this.parallel = parallel;
        this.parallelEUt = parallelEUt;
        this.ocLevel = ocLevel;
    }

    public void reset() {
        this.eut = 0L;
        this.duration = 0;
        this.parallel = 0;
        this.parallelEUt = 0L;
        this.ocLevel = 0;
    }

    @Override
    public String toString() {
        return "OCResult[" + "EUt=" + eut + ", " + "duration=" + duration + ", " + "ocLevel=" + ocLevel + ", " + "parallel=" + parallel + ", " + "parallelEUt=" + parallelEUt + ']';
    }

    public void setEut(final long eut) {
        this.eut = eut;
    }

    public void setParallelEUt(final long parallelEUt) {
        this.parallelEUt = parallelEUt;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public void setParallel(final int parallel) {
        this.parallel = parallel;
    }

    public void setOcLevel(final int ocLevel) {
        this.ocLevel = ocLevel;
    }

    public long getEut() {
        return this.eut;
    }

    public long getParallelEUt() {
        return this.parallelEUt;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getParallel() {
        return this.parallel;
    }

    public int getOcLevel() {
        return this.ocLevel;
    }
}
