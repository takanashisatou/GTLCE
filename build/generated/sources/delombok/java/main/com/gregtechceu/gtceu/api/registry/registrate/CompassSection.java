package com.gregtechceu.gtceu.api.registry.registrate;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.json.SimpleIGuiTextureJsonUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author KilaBash
 * @date 2023/7/31
 * @implNote CompassSectionBuilder
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CompassSection {
    private final ResourceLocation sectionID;
    private Supplier<IGuiTexture> icon = () -> IGuiTexture.EMPTY;
    @Nullable
    private Supplier<IGuiTexture> background = null;
    private int priority = 99;
    private String lang;

    private CompassSection(String section) {
        this.sectionID = GTCEu.id(section);
        lang = FormattingUtil.toEnglishName(section);
    }

    public static CompassSection create(String section) {
        return new CompassSection(section);
    }

    public CompassSection register() {
        GTRegistries.COMPASS_SECTIONS.register(sectionID, this);
        return this;
    }

    public String getUnlocalizedKey() {
        return sectionID.toLanguageKey("compass.section");
    }


    public static class CompassSectionProvider implements DataProvider {
        private final PackOutput output;
        private final ExistingFileHelper existingHelper;

        public CompassSectionProvider(PackOutput output, ExistingFileHelper existingHelper) {
            this.output = output;
            this.existingHelper = existingHelper;
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cache) {
            return generate(output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(GTCEu.MOD_ID), cache);
        }

        @Override
        public String getName() {
            return "GTCEU\'s Compass Sections";
        }

        public CompletableFuture<?> generate(Path path, CachedOutput cache) {
            return CompletableFuture.allOf(GTRegistries.COMPASS_SECTIONS.values().stream().map(section -> {
                var resourcePath = "compass/sections/" + section.sectionID.getPath() + ".json";
                if (existingHelper.exists(GTCEu.id(resourcePath), PackType.CLIENT_RESOURCES)) {
                    return null;
                }
                JsonObject json = new JsonObject();
                json.add("button_texture", SimpleIGuiTextureJsonUtils.toJson(section.icon.get()));
                if (section.background != null) {
                    json.add("background_texture", SimpleIGuiTextureJsonUtils.toJson(section.background.get()));
                }
                json.addProperty("priority", section.priority);
                return DataProvider.saveStable(cache, json, path.resolve(resourcePath));
            }).filter(Objects::nonNull).toArray(CompletableFuture[]::new));
        }
    }

    public ResourceLocation sectionID() {
        return this.sectionID;
    }

    /**
     * @return {@code this}.
     */
    public CompassSection icon(final Supplier<IGuiTexture> icon) {
        this.icon = icon;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public CompassSection background(@Nullable final Supplier<IGuiTexture> background) {
        this.background = background;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public CompassSection priority(final int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * @return {@code this}.
     */
    public CompassSection lang(final String lang) {
        this.lang = lang;
        return this;
    }

    public String lang() {
        return this.lang;
    }
}
