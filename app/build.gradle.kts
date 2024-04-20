plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.project.taskplanner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.taskplanner"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets.getByName("main"){
        res.srcDirs(
            listOf(
                //menu
                "src/main/res/myMenus/bottom_nav_bar",
                "src/main/res/myMenus/pop_up_menu",
                "src/main/res/myMenus/tool_bar",
                // drawable
                "src/main/res/myDrawable/icons",
                "src/main/res/myDrawable/bottom_sheet",
                "src/main/res/myDrawable/icons/bottomNavMenu",
                // layout
                "src/main/res/layouts/activities",
                "src/main/res/layouts/fragments",
                "src/main/res/layouts/bottom_sheets",
                "src/main/res/layouts/dialogs",
                "src/main/res/layouts/items/items_notes_activity",
                "src/main/res/layouts/items/items_category",
                "src/main/res/layouts/items/items_tasks",
                "src/main/res/layouts",
                "src/main/res",
            )
        )
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

    buildFeatures {
        viewBinding = true
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    implementation(project(":domain"))
    implementation(project(":data"))

    // ViewModel
    val lifecycleVersion ="2.7.0"
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    val navVersion = "2.7.6"
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")



    // Koin
    val koinVersion = "3.5.0"
    implementation("io.insert-koin:koin-android:$koinVersion")
    runtimeOnly("io.insert-koin:koin-core:$koinVersion")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}