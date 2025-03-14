package com.gregtechceu.gtceu.api.recipe;

import com.gregtechceu.gtceu.GTCEu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class ResearchData implements Iterable<ResearchData.ResearchEntry> {
    private final Collection<ResearchEntry> entries;

    public ResearchData() {
        entries = new ArrayList<>();
    }

    /**
     * @param entry the entry to add
     */
    public void add(@NotNull ResearchEntry entry) {
        this.entries.add(entry);
    }

    @NotNull
    @Override
    public Iterator<ResearchEntry> iterator() {
        return this.entries.iterator();
    }

    public static ResearchData fromJson(JsonArray array) {
        List<ResearchEntry> entries = new ArrayList<>();
        for (int i = 0; i < array.size(); ++i) {
            entries.add(ResearchEntry.fromJson(array.get(i).getAsJsonObject()));
        }
        return new ResearchData(entries);
    }

    public JsonArray toJson() {
        JsonArray json = new JsonArray();
        this.entries.forEach(entry -> json.add(entry.toJson()));
        return json;
    }

    public static ResearchData fromNetwork(FriendlyByteBuf buf) {
        List<ResearchEntry> entries = new ArrayList<>();
        int size = buf.readVarInt();
        for (int i = 0; i < size; ++i) {
            entries.add(ResearchEntry.fromNetwork(buf));
        }
        return new ResearchData(entries);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeVarInt(this.entries.size());
        this.entries.forEach(entry -> entry.toNetwork(buf));
    }


    /**
     * An entry containing information about a researchable recipe.
     * <p>
     * Used for internal research storage and JEI integration.
     */
    public static final class ResearchEntry {
        @NotNull
        private final String researchId;
        @NotNull
        private final ItemStack dataItem;

        /**
         * @param researchId the id of the research
         * @param dataItem   the item allowed to contain the research
         */
        public ResearchEntry(@NotNull String researchId, @NotNull ItemStack dataItem) {
            this.researchId = researchId;
            this.dataItem = dataItem;
        }

        public static ResearchEntry fromJson(JsonObject tag) {
            return new ResearchEntry(tag.get("researchId").getAsString(), ItemStack.CODEC.parse(JsonOps.INSTANCE, tag.get("dataItem")).getOrThrow(false, GTCEu.LOGGER::error));
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("researchId", researchId);
            json.add("dataItem", ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, dataItem).getOrThrow(false, GTCEu.LOGGER::error));
            return json;
        }

        public static ResearchEntry fromNetwork(FriendlyByteBuf buf) {
            String researchId = buf.readUtf();
            ItemStack dataItem = buf.readItem();
            return new ResearchEntry(researchId, dataItem);
        }

        public void toNetwork(FriendlyByteBuf buf) {
            buf.writeUtf(this.researchId);
            buf.writeItem(this.dataItem);
        }

        @NotNull
        public String getResearchId() {
            return this.researchId;
        }

        @NotNull
        public ItemStack getDataItem() {
            return this.dataItem;
        }
    }

    public ResearchData(final Collection<ResearchEntry> entries) {
        this.entries = entries;
    }
}
