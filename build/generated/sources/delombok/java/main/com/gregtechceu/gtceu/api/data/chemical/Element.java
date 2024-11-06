package com.gregtechceu.gtceu.api.data.chemical;

/**
 * This is some kind of Periodic Table, which can be used to determine "Properties" of the Materials.
 */
public class Element {
    /**
     * Amount of Protons
     */
    private long protons;
    /**
     * Amount of Neutrons
     */
    private long neutrons;
    /**
     * Amount of Half Life this Material has in Seconds. -1 for stable Materials
     */
    private long halfLifeSeconds;
    /**
     * String representing the Elements this element decays to. Separated by an '&' Character
     */
    private String decayTo;
    /**
     * Name of the Element
     */
    private String name;
    /**
     * Symbol of the Element
     */
    private String symbol;
    /**
     * Is this element an isotope?
     */
    private boolean isIsotope;

    public long mass() {
        return protons + neutrons;
    }

    public Element(long protons, long neutrons, long halfLifeSeconds, String decayTo, String name, String symbol, boolean isIsotope) {
        this.protons = protons;
        this.neutrons = neutrons;
        this.halfLifeSeconds = halfLifeSeconds;
        this.decayTo = decayTo;
        this.name = name;
        this.symbol = symbol;
        this.isIsotope = isIsotope;
    }

    /**
     * Amount of Protons
     */
    public long protons() {
        return this.protons;
    }

    /**
     * Amount of Protons
     */
    public void protons(final long protons) {
        this.protons = protons;
    }

    /**
     * Amount of Neutrons
     */
    public long neutrons() {
        return this.neutrons;
    }

    /**
     * Amount of Neutrons
     */
    public void neutrons(final long neutrons) {
        this.neutrons = neutrons;
    }

    /**
     * Amount of Half Life this Material has in Seconds. -1 for stable Materials
     */
    public long halfLifeSeconds() {
        return this.halfLifeSeconds;
    }

    /**
     * Amount of Half Life this Material has in Seconds. -1 for stable Materials
     */
    public void halfLifeSeconds(final long halfLifeSeconds) {
        this.halfLifeSeconds = halfLifeSeconds;
    }

    /**
     * String representing the Elements this element decays to. Separated by an '&' Character
     */
    public String decayTo() {
        return this.decayTo;
    }

    /**
     * String representing the Elements this element decays to. Separated by an '&' Character
     */
    public void decayTo(final String decayTo) {
        this.decayTo = decayTo;
    }

    /**
     * Name of the Element
     */
    public String name() {
        return this.name;
    }

    /**
     * Name of the Element
     */
    public void name(final String name) {
        this.name = name;
    }

    /**
     * Symbol of the Element
     */
    public String symbol() {
        return this.symbol;
    }

    /**
     * Symbol of the Element
     */
    public void symbol(final String symbol) {
        this.symbol = symbol;
    }

    /**
     * Is this element an isotope?
     */
    public boolean isIsotope() {
        return this.isIsotope;
    }

    /**
     * Is this element an isotope?
     */
    public void isIsotope(final boolean isIsotope) {
        this.isIsotope = isIsotope;
    }
}
