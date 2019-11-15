plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.priamm.n1test"

        minSdkVersion(19)
        targetSdkVersion(29)

        versionName = "1.0"
        versionCode = 1

        buildToolsVersion = "29.0.2"


        buildTypes {

            getByName("release") {
                isMinifyEnabled = true

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    file("proguard-rules.pro")
                )
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.60")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0-beta02")

}

