plugins {
    if (ModuleConfig.isApp){
        alias(libs.plugins.android.application)
    }else {
        alias(libs.plugins.android.library)
    }
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.hi.weather"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        if (ModuleConfig.isApp){
          //  applicationId=ModuleConfig.MODULE_WEATHER
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       // consumerProguardFiles("consumer-rules.pro")
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
    //ARouter
    kapt {
        arguments {
            arg("AROUTER_MODULE_NAME", project.name)
        }
    }
    viewBinding {
        enable=true
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
    viewBinding{
        enable=true
    }
}

dependencies {
    implementation(libs.arouter)
    kapt(libs.arouterCompiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
   // implementation(libs.material)
    implementation("com.google.android.material:material:1.0.0")
    implementation(project(":lib_base"))
    implementation(project(":lib_network"))
    implementation(libs.swiperefreshlayout)
    //implementation(libs.livedata)
    implementation(libs.mpAndroidChart)
    implementation(libs.lifecycle)
    implementation(libs.lifecycleextensions)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.gson)
    implementation ("com.squareup.retrofit2:converter-gson:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.8.2")
}