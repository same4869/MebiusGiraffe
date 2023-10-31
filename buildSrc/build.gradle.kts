repositories {
    google()
    mavenCentral()
}
plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}
dependencies {
    implementation("com.android.tools.build:gradle:7.0.3")
    implementation("com.android.tools.build:gradle-api:7.0.3")
    gradleApi()
    localGroovy()
}
gradlePlugin {
    plugins {
        create("GiraffeBuildPlugin") {
            id = "giraffe.build.plugin"
            description = "pre build for Giraffe project"
            implementationClass = "com.pokemon.mebius.framework.buildsrc.GiraffeBuildPlugin"
        }
    }
}