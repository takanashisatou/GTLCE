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
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibsInPluginsBlock extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibsInPluginsBlock(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Dependency provider for <b>minecraft</b> with <b>com.mojang:minecraft</b> coordinates and
     * with version reference <b>minecraft</b>
     * <p>
     * This dependency was declared in settings file 'settings.gradle'
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getMinecraft() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("minecraft");
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

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>lombok</b> with value <b>8.7.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getLombok() { return getVersion("lombok"); }

        /**
         * Version alias <b>machete</b> with value <b>1.+</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getMachete() { return getVersion("machete"); }

        /**
         * Version alias <b>minecraft</b> with value <b>1.20.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getMinecraft() { return getVersion("minecraft"); }

        /**
         * Version alias <b>modDevGradleLegacy</b> with value <b>2.0.61-beta-pr-118-legacy</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getModDevGradleLegacy() { return getVersion("modDevGradleLegacy"); }

        /**
         * Version alias <b>parchment</b> with value <b>2023.09.03</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getParchment() { return getVersion("parchment"); }

        /**
         * Version alias <b>quiltMappings</b> with value <b>5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getQuiltMappings() { return getVersion("quiltMappings"); }

        /**
         * Version alias <b>shadow</b> with value <b>7.1.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getShadow() { return getVersion("shadow"); }

        /**
         * Version alias <b>spotless</b> with value <b>6.25.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in settings file 'settings.gradle'
         */
        public Provider<String> getSpotless() { return getVersion("spotless"); }

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

        /**
         * Plugin provider for <b>lombok</b> with plugin id <b>io.freefair.lombok</b> and
         * with version reference <b>lombok</b>
         * <p>
         * This plugin was declared in settings file 'settings.gradle'
         */
        public Provider<PluginDependency> getLombok() { return createPlugin("lombok"); }

        /**
         * Plugin provider for <b>machete</b> with plugin id <b>io.github.p03w.machete</b> and
         * with version reference <b>machete</b>
         * <p>
         * This plugin was declared in settings file 'settings.gradle'
         */
        public Provider<PluginDependency> getMachete() { return createPlugin("machete"); }

        /**
         * Plugin provider for <b>modDevGradle</b> with plugin id <b>net.neoforged.moddev.legacy</b> and
         * with version reference <b>modDevGradleLegacy</b>
         * <p>
         * This plugin was declared in settings file 'settings.gradle'
         */
        public Provider<PluginDependency> getModDevGradle() { return createPlugin("modDevGradle"); }

        /**
         * Plugin provider for <b>shadow</b> with plugin id <b>com.github.johnrengelman.shadow</b> and
         * with version reference <b>shadow</b>
         * <p>
         * This plugin was declared in settings file 'settings.gradle'
         */
        public Provider<PluginDependency> getShadow() { return createPlugin("shadow"); }

        /**
         * Plugin provider for <b>spotless</b> with plugin id <b>com.diffplug.spotless</b> and
         * with version reference <b>spotless</b>
         * <p>
         * This plugin was declared in settings file 'settings.gradle'
         */
        public Provider<PluginDependency> getSpotless() { return createPlugin("spotless"); }

    }

}
