plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.hi.base"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    //ARouter
    kapt {
        arguments {
            arg("AROUTER_MODULE_NAME", project.name)
        }
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
    buildFeatures {
        buildConfig = true

    }
    viewBinding {
        enable=true
    }

}

dependencies {
    kapt(libs.arouterCompiler)
    implementation(libs.arouter)
    implementation(libs.eventBus)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.workRuntime)
    implementation(libs.worktesting)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.refreshfooter)
    implementation(libs.refreshheader)
    implementation(libs.refreshLayoutKernel)
    implementation(libs.mpAndroidChart)
    implementation(libs.gson)
    implementation(project(":lib_network"))
    implementation(project(":lib_voice"))
    implementation("com.github.getActivity:XXPermissions:18.63")

}