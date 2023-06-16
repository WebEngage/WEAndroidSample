import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    //android ui
    private val appcompat = "androidx.appcompat:appcompat:${Versions.AppCompat}"
    private val coreKtx = "androidx.core:core-ktx:${Versions.KotlinCore}"
    private val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.ConstraintLayout}"

    //test libs
    private val junit = "junit:junit:${Versions.JUnit}"
    private val extJUnit = "androidx.test.ext:junit:${Versions.EXTJunit}"
    private val espressoCore = "androidx.test.espresso:espresso-core:${Versions.EspressoCore}"

    //WebEngage
    private val WebEngage = "com.webengage:android-sdk:${Versions.WebEngageSDKCore}"
    private val WebEngagePersonalization = "com.webengage:we-personalization:${Versions.WEPersonalization}"
    private val Material = "com.google.android.material:material:${Versions.MaterialComponents}"
    public val FirebaseBOM = "com.google.firebase:firebase-bom:${Versions.FirebaseBOM}"
    private val FirebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    private val FirebaseMessaging = "com.google.firebase:firebase-messaging-ktx"

    private val HTMLCleaner = "net.sourceforge.htmlcleaner:htmlcleaner:${Versions.HTMLCleaner}"

    private val MultiDex = "com.android.support:multidex:${Versions.MultiDex}"
    private val GSon = "com.google.code.gson:gson:${Versions.Gson}"


    val appLibraries = arrayListOf<String>().apply {
        add(coreKtx)
        add(appcompat)
        add(constraintLayout)
        add(GSon)
        add(HTMLCleaner)
        add(MultiDex)
        add(Material)
        add(WebEngage)
        add(WebEngagePersonalization)
        add(FirebaseAnalytics)
        add(FirebaseMessaging)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(extJUnit)
        add(espressoCore)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
    }

}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

