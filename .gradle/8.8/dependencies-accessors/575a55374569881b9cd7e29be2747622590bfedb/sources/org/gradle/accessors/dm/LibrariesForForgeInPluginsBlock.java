package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code forge} extension.
 */
@NonNullApi
public class LibrariesForForgeInPluginsBlock extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AlmostUnifiedLibraryAccessors laccForAlmostUnifiedLibraryAccessors = new AlmostUnifiedLibraryAccessors(owner);
    private final JeiLibraryAccessors laccForJeiLibraryAccessors = new JeiLibraryAccessors(owner);
    private final LdlibLibraryAccessors laccForLdlibLibraryAccessors = new LdlibLibraryAccessors(owner);
    private final MixinextrasLibraryAccessors laccForMixinextrasLibraryAccessors = new MixinextrasLibraryAccessors(owner);
    private final ReiLibraryAccessors laccForReiLibraryAccessors = new ReiLibraryAccessors(owner);
    private final ShimmerLibraryAccessors laccForShimmerLibraryAccessors = new ShimmerLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForForgeInPluginsBlock(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Dependency provider for <b>ae2</b> with <b>appeng:appliedenergistics2-forge</b> coordinates and
     * with version reference <b>ae2</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getAe2() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("ae2");
    }

    /**
     * Dependency provider for <b>architectury</b> with <b>dev.architectury:architectury-forge</b> coordinates and
     * with version reference <b>architectury</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getArchitectury() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("architectury");
    }

    /**
     * Dependency provider for <b>configuration</b> with <b>dev.toma.configuration:configuration-forge-1.20.1</b> coordinates and
     * with version reference <b>configuration</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getConfiguration() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("configuration");
    }

    /**
     * Dependency provider for <b>createForge</b> with <b>com.simibubi.create:create-1.20.1</b> coordinates and
     * with version reference <b>createForge</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getCreateForge() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("createForge");
    }

    /**
     * Dependency provider for <b>curios</b> with <b>top.theillusivec4.curios:curios-forge</b> coordinates and
     * with version reference <b>curios</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getCurios() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("curios");
    }

    /**
     * Dependency provider for <b>emi</b> with <b>dev.emi:emi-forge</b> coordinates and
     * with version reference <b>emi</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getEmi() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("emi");
    }

    /**
     * Dependency provider for <b>flywheel</b> with <b>com.jozufozu.flywheel:flywheel-forge-1.20.1</b> coordinates and
     * with version reference <b>flywheel</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getFlywheel() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("flywheel");
    }

    /**
     * Dependency provider for <b>jade</b> with <b>maven.modrinth:jade</b> coordinates and
     * with version reference <b>jade</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getJade() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("jade");
    }

    /**
     * Dependency provider for <b>javd</b> with <b>curse.maven:javd-370890</b> coordinates and
     * with version reference <b>javd</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getJavd() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("javd");
    }

    /**
     * Dependency provider for <b>kubejs</b> with <b>dev.latvian.mods:kubejs-forge</b> coordinates and
     * with version reference <b>kubejs</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getKubejs() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("kubejs");
    }

    /**
     * Dependency provider for <b>minecraftForge</b> with <b>net.minecraftforge:forge</b> coordinates and
     * with version reference <b>minecraftForge</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getMinecraftForge() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("minecraftForge");
    }

    /**
     * Dependency provider for <b>registrate</b> with <b>com.tterrag.registrate:Registrate</b> coordinates and
     * with version reference <b>registrate</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getRegistrate() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("registrate");
    }

    /**
     * Dependency provider for <b>rhino</b> with <b>dev.latvian.mods:rhino-forge</b> coordinates and
     * with version reference <b>rhino</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getRhino() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("rhino");
    }

    /**
     * Dependency provider for <b>theoneprobe</b> with <b>mcjty.theoneprobe:theoneprobe</b> coordinates and
     * with version reference <b>theoneprobe</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getTheoneprobe() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("theoneprobe");
    }

    /**
     * Dependency provider for <b>worldStripper</b> with <b>curse.maven:worldStripper-250603</b> coordinates and
     * with version reference <b>worldStripper</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getWorldStripper() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("worldStripper");
    }

    /**
     * Group of libraries at <b>almostUnified</b>
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public AlmostUnifiedLibraryAccessors getAlmostUnified() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return laccForAlmostUnifiedLibraryAccessors;
    }

    /**
     * Group of libraries at <b>jei</b>
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public JeiLibraryAccessors getJei() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return laccForJeiLibraryAccessors;
    }

    /**
     * Group of libraries at <b>ldlib</b>
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public LdlibLibraryAccessors getLdlib() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return laccForLdlibLibraryAccessors;
    }

    /**
     * Group of libraries at <b>mixinextras</b>
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public MixinextrasLibraryAccessors getMixinextras() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return laccForMixinextrasLibraryAccessors;
    }

    /**
     * Group of libraries at <b>rei</b>
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public ReiLibraryAccessors getRei() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return laccForReiLibraryAccessors;
    }

    /**
     * Group of libraries at <b>shimmer</b>
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public ShimmerLibraryAccessors getShimmer() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return laccForShimmerLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public BundleAccessors getBundles() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class AlmostUnifiedLibraryAccessors extends SubDependencyFactory {

        public AlmostUnifiedLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>forge</b> with <b>com.almostreliable.mods:almostunified-forge</b> coordinates and
         * with version reference <b>au</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getForge() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("almostUnified.forge");
        }

    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class JeiLibraryAccessors extends SubDependencyFactory {
        private final JeiCommonLibraryAccessors laccForJeiCommonLibraryAccessors = new JeiCommonLibraryAccessors(owner);
        private final JeiForgeLibraryAccessors laccForJeiForgeLibraryAccessors = new JeiForgeLibraryAccessors(owner);

        public JeiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jei.common</b>
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public JeiCommonLibraryAccessors getCommon() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return laccForJeiCommonLibraryAccessors;
        }

        /**
         * Group of libraries at <b>jei.forge</b>
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public JeiForgeLibraryAccessors getForge() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return laccForJeiForgeLibraryAccessors;
        }

    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class JeiCommonLibraryAccessors extends SubDependencyFactory {

        public JeiCommonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>mezz.jei:jei-1.20.1-common-api</b> coordinates and
         * with version reference <b>jei</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getApi() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("jei.common.api");
        }

    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class JeiForgeLibraryAccessors extends SubDependencyFactory {

        public JeiForgeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>mezz.jei:jei-1.20.1-forge-api</b> coordinates and
         * with version reference <b>jei</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getApi() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("jei.forge.api");
        }

        /**
         * Dependency provider for <b>impl</b> with <b>mezz.jei:jei-1.20.1-forge</b> coordinates and
         * with version reference <b>jei</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getImpl() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("jei.forge.impl");
        }

    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class LdlibLibraryAccessors extends SubDependencyFactory {

        public LdlibLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>forge</b> with <b>com.lowdragmc.ldlib:ldlib-forge-1.20.1</b> coordinates and
         * with version reference <b>ldlib</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getForge() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("ldlib.forge");
        }

    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class MixinextrasLibraryAccessors extends SubDependencyFactory {

        public MixinextrasLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>forge</b> with <b>io.github.llamalad7:mixinextras-forge</b> coordinates and
         * with version reference <b>mixinextras</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getForge() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("mixinextras.forge");
        }

    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class ReiLibraryAccessors extends SubDependencyFactory {

        public ReiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>me.shedaniel:RoughlyEnoughItems-api-forge</b> coordinates and
         * with version reference <b>rei</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getApi() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("rei.api");
        }

        /**
         * Dependency provider for <b>forge</b> with <b>me.shedaniel:RoughlyEnoughItems-forge</b> coordinates and
         * with version reference <b>rei</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getForge() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("rei.forge");
        }

        /**
         * Dependency provider for <b>plugin</b> with <b>me.shedaniel:RoughlyEnoughItems-default-plugin-forge</b> coordinates and
         * with version reference <b>rei</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getPlugin() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("rei.plugin");
        }

    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class ShimmerLibraryAccessors extends SubDependencyFactory {

        public ShimmerLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>forge</b> with <b>com.lowdragmc.shimmer:Shimmer-forge</b> coordinates and
         * with version reference <b>shimmer</b>
         * <p>
         * This dependency was declared in settings file 'settings.gradle'
         *
         * @deprecated Will be removed in Gradle 9.0.
         */
        @Deprecated
        public Provider<MinimalExternalModuleDependency> getForge() {
            org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
            return create("shimmer.forge");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ae2</b> with value <b>15.0.18</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getAe2() { return getVersion("ae2"); }

        /**
         * Version alias <b>architectury</b> with value <b>9.2.14</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getArchitectury() { return getVersion("architectury"); }

        /**
         * Version alias <b>au</b> with value <b>1.20.1-0.6.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getAu() { return getVersion("au"); }

        /**
         * Version alias <b>configuration</b> with value <b>2.2.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getConfiguration() { return getVersion("configuration"); }

        /**
         * Version alias <b>createForge</b> with value <b>0.5.1.f-33</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getCreateForge() { return getVersion("createForge"); }

        /**
         * Version alias <b>curios</b> with value <b>5.9.1+1.20.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getCurios() { return getVersion("curios"); }

        /**
         * Version alias <b>emi</b> with value <b>1.1.13+1.20.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getEmi() { return getVersion("emi"); }

        /**
         * Version alias <b>flywheel</b> with value <b>0.6.10-10</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getFlywheel() { return getVersion("flywheel"); }

        /**
         * Version alias <b>forgeShortVersion</b> with value <b>47.1.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getForgeShortVersion() { return getVersion("forgeShortVersion"); }

        /**
         * Version alias <b>jade</b> with value <b>11.6.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getJade() { return getVersion("jade"); }

        /**
         * Version alias <b>javd</b> with value <b>4803995</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getJavd() { return getVersion("javd"); }

        /**
         * Version alias <b>jei</b> with value <b>15.12.1.46</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getJei() { return getVersion("jei"); }

        /**
         * Version alias <b>kubejs</b> with value <b>2001.6.4-build.120</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getKubejs() { return getVersion("kubejs"); }

        /**
         * Version alias <b>ldlib</b> with value <b>1.0.28.a</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getLdlib() { return getVersion("ldlib"); }

        /**
         * Version alias <b>minecraftForge</b> with value <b>1.20.1-47.1.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getMinecraftForge() { return getVersion("minecraftForge"); }

        /**
         * Version alias <b>mixinextras</b> with value <b>0.2.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getMixinextras() { return getVersion("mixinextras"); }

        /**
         * Version alias <b>registrate</b> with value <b>MC1.20-1.3.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getRegistrate() { return getVersion("registrate"); }

        /**
         * Version alias <b>rei</b> with value <b>12.1.725</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getRei() { return getVersion("rei"); }

        /**
         * Version alias <b>rhino</b> with value <b>2001.2.3-build.6</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getRhino() { return getVersion("rhino"); }

        /**
         * Version alias <b>shimmer</b> with value <b>1.20.1-0.2.4</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getShimmer() { return getVersion("shimmer"); }

        /**
         * Version alias <b>theoneprobe</b> with value <b>1.20.1-10.0.1-3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getTheoneprobe() { return getVersion("theoneprobe"); }

        /**
         * Version alias <b>worldStripper</b> with value <b>4578579</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getWorldStripper() { return getVersion("worldStripper"); }

    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
