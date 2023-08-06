import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.PluginDependenciesSpecScope
import org.gradle.plugin.use.PluginDependency

fun PluginDependenciesSpecScope.aliasNoVersion(notation: Provider<PluginDependency>) =
    id(notation.get().pluginId)
