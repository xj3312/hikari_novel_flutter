plugins {
    id("com.android.application")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "pers.cyh128.hikari_novel"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "pers.cyh128.hikari_novel"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    // ================= 【1. 新增签名配置】 =================
    signingConfigs {
        create("release") {
            // 读取 GitHub Actions 传入的环境变量
            val keystorePath = System.getenv("KEYSTORE_FILE_PATH")
            val storePasswordEnv = System.getenv("RELEASE_STORE_PASSWORD")
            val keyAliasEnv = System.getenv("RELEASE_KEY_ALIAS")
            val keyPasswordEnv = System.getenv("RELEASE_KEY_PASSWORD")

            // 只有当签名文件和环境变量同时存在时才配置签名，否则降级使用默认设置
            if (!keystorePath.isNullOrEmpty() && file(keystorePath).exists()) {
                storeFile = file(keystorePath)
                storePassword = storePasswordEnv
                keyAlias = keyAliasEnv
                keyPassword = keyPasswordEnv
            }
        }
    }

    buildTypes {
        release {
            // ================= 【2. 修改此处绑定】 =================
            // 判断是否成功加载了 release 签名配置，加载成功则使用 release，否则保留 debug 签名
            val releaseConfig = signingConfigs.getByName("release")
            signingConfig = if (releaseConfig.storeFile != null) {
                releaseConfig
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

flutter {
    source = "../.."
}
