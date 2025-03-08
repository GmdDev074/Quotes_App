plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.quotes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quotes"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.gms:play-services-ads:22.6.0")

    //lottie animation
    implementation("com.airbnb.android:lottie:6.4.0")

    //shimmer effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")


    //paper db
    implementation("io.github.pilgr:paperdb:2.7.2")

    //google ads
    implementation("com.google.android.gms:play-services-ads:24.0.0")
}