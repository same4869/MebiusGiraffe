package com.pokemon.mebius.framework.buildsrc

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.BuildConfigField
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * @brief
 * @author mistletoe
 * @date 2022/4/2
 **/
class GiraffeBuildPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class)
        androidComponents.onVariants {
            it.buildConfigFields.apply {
                put("VERSION_CODE", BuildConfigField("String",
                    String.format("\"%s\"", project.properties["LIB_VERSION"]), "sora wolf version code"))
            }
        }
    }
}