plugins {
    if (ModuleConfig.isApp) {
        alias(libs.plugins.android.application)
    } else {
        alias(libs.plugins.android.library)
    }
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.hi.developer"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        if (ModuleConfig.isApp) {
         //   applicationId=ModuleConfig.MODULE_DEVELOPER
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // consumerProguardFiles("consumer-rules.pro")
    }
    //ARouter
    kapt {
        arguments {
            arg("AROUTER_MODULE_NAME", project.name)
        }
    }
    //动态替换资源
    sourceSets {
        getByName("main") {
            if (ModuleConfig.isApp) {
                manifest.srcFile("src/main/manifest/AndroidManifest.xml")
            } else {
                manifest.srcFile("src/main/AndroidManifest.xml")
            }
        }
    }

    viewBinding {
        enable = true
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.arouter)
    implementation(project(":lib_voice"))
    implementation(project(":lib_network"))
    kapt(libs.arouterCompiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":lib_base"))
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)


}