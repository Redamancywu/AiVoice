plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.hi.voices.lib_voice"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

    }
    sourceSets {
        getByName("main") {
            jniLibs.srcDir("src/main/jniLibs")
        }
    }

    packaging {
        jniLibs.keepDebugSymbols.add("*/libvad.dnn.so")
        jniLibs.keepDebugSymbols.add("*/libbd_easr_s1_merge_normal_20151216.dat.so")
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
    // 添加SO库文件的依赖
    implementation(files("src/main/jniLibs/armeabi-v7a/libvad.dnn.so"))
    implementation(files("src/main/jniLibs/arm64-v8a/libbd_easr_s1_merge_normal_20151216.dat.so"))
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(files("libs/com.baidu.tts_2.6.2.2.20200629_44818d4.jar"))
    api(files("libs/bdasr_V3_20210628_cfe8c44.jar"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}