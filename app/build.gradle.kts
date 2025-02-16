plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("cash.bdo.scalroid")
}

val natives = configurations.create("natives")

task("copyAndroidNatives") {
    doFirst {
        // WTF, src/main??? WTF
        val libs = "src/main/jniLibs" // layout.buildDirectory.dir("lib").get()
        file("$libs/armeabi").mkdirs()
        file("$libs/armeabi-v7a").mkdirs()
        file("$libs/arm64-v8a").mkdirs()
        file("$libs/x86_64").mkdirs()
        file("$libs/x86").mkdirs()

        configurations["natives"].files.forEach { jar ->
            val match = ".*natives-(.*)\\.jar".toRegex().matchEntire(jar.name)
            if (match != null) {
                val outputDir = file("$libs/${match.groupValues.get(1)}")
                copy {
                    from(zipTree(jar))
                    into(outputDir)
                    include("*.so")
                }
            }
        }
    }

}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = true
            proguardFiles("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
    }
    scalroid {
        scala.zincVersion = "1.10.7"
    }
}

tasks.configureEach {
    if (name.contains("package")) {
        dependsOn("copyAndroidNatives")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.scala.library)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.gdx.gdx)
    implementation(libs.gdx.oboe)
    api(libs.gdx.backend.android)
    natives(variantOf(libs.gdx.platform) { classifier("natives-armeabi-v7a") })
    natives(variantOf(libs.gdx.platform) { classifier("natives-arm64-v8a") })
    implementation(libs.gdx.freetype)
    natives(variantOf(libs.gdx.freetype.platform) { classifier("natives-armeabi-v7a") })
    natives(variantOf(libs.gdx.freetype.platform) { classifier("natives-arm64-v8a") })
}
