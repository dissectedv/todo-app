plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.listadetarefas"
    compileSdk = 36 // ATUALIZADO DE 34 PARA 36

    defaultConfig {
        applicationId = "com.example.listadetarefas"
        minSdk = 26 // Um valor mais comum que abrange mais dispositivos
        targetSdk = 36 // ATUALIZADO PARA CORRESPONDER AO compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        compose = true
    }
    composeOptions {
        // A versão do compilador do Kotlin para Compose.
        // Verifique a compatibilidade se atualizar o Compose BoM.
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Firebase BoM (Gerencia as versões das bibliotecas do Firebase)
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    // Dependência do Cloud Firestore (essencial para o banco de dados)
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Jetpack Compose e AndroidX (usando o version catalog 'libs')
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Navigation para Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ConstraintLayout para Compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    // ViewModel para Compose (essencial para a arquitetura)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

    // Dependências de Teste
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}