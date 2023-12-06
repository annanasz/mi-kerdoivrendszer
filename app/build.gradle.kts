import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.application")
    id("com.google.gms.google-services")
    id ("org.jetbrains.kotlin.kapt")
    id ("dagger.hilt.android.plugin")
}


android {
    namespace = "com.bme.surveysystemsupportedbyai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bme.surveysystemsupportedbyai"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField ("String", "OPENAI_API_KEY", "\"${System.getenv("OPENAI_THESIS_API_KEY")}\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        android.buildFeatures.buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.compose.material:material-icons-extended:1.0.0")
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.leanback:leanback:1.0.0")
    implementation("androidx.compose.ui:ui-android:1.5.2")
    implementation ("androidx.compose.material3:material3:1.0.0")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-common:19.0.0")
    implementation("androidx.security:security-crypto-ktx:1.0.0")
    implementation("com.google.firebase:firebase-ml-common:22.1.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    //Dagger-Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    //CameraX
    implementation ("androidx.camera:camera-camera2:1.4.0-alpha01")
    implementation ("androidx.camera:camera-lifecycle:1.4.0-alpha01")
    implementation ("androidx.camera:camera-view:1.4.0-alpha01")
    implementation ("androidx.camera:camera-extensions:1.4.0-alpha01")
    //Permission
    implementation ("com.google.accompanist:accompanist-permissions:0.31.6-rc")
    //Text recognition
    implementation ("com.google.mlkit:text-recognition:16.0.0")
    //OpenAI
    implementation (platform("com.aallam.openai:openai-client-bom:3.5.0"))
    implementation ("com.aallam.openai:openai-client")
    runtimeOnly ("io.ktor:ktor-client-okhttp")
    //gson
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.4")

    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")

}