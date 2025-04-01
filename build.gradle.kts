import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.modulegraph)
    alias(libs.plugins.ktlint)
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        android = true
        ignoreFailures = false
        reporters {
            reporter(ReporterType.CHECKSTYLE)
        }
    }

    tasks.named("ktlintCheck") {
        dependsOn(subprojects.mapNotNull { it.tasks.findByName("ktlintCheck") })
    }
}
