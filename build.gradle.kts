import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
    id("com.android.library") version "8.2.1" apply false
    id("dev.iurysouza.modulegraph") version "0.12.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
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
