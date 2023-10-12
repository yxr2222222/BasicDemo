## BasicDemo

基于AndroidX + kotlin + mvvm + 协程的基础框架

### 安装说明

1. 编译环境

   ```groovy
   Gradle:       7.2
   Kotlin:       1.5.21
   JDK           11
   ```

2. 内部包含引用

   ```groovy
   api 'androidx.appcompat:appcompat:1.4.2'
   api 'com.google.android.material:material:1.6.1'
   api 'androidx.legacy:legacy-support-v4:1.0.0'
   api 'androidx.multidex:multidex:2.0.1'
   api 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1'
   api 'androidx.paging:paging-runtime-ktx:3.1.1'
   
   // gson
   api 'com.google.code.gson:gson:2.8.5'
   
   // rxJava+rxAndroid
   api 'io.reactivex.rxjava2:rxandroid:2.1.1'
   api 'io.reactivex.rxjava2:rxjava:2.2.19'
   
   // Glide
   api 'com.github.bumptech.glide:glide:4.9.0'
   
   // retrofit+okhttp
   api 'com.squareup.retrofit2:retrofit:2.6.2'
   api 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'
   api 'com.squareup.retrofit2:converter-gson:2.4.0'
   api 'com.squareup.okhttp3:logging-interceptor:4.9.3'
   
   // kotlin + kotlin 携程
   api 'org.jetbrains.kotlin:kotlin-stdlib:1.5.21'
   api 'org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.21'
   api 'org.jetbrains.kotlin:kotlin-reflect:1.5.21'
   api 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.2'
   api 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2'
   
   // ui
   api 'com.youth.banner:banner:2.1.0'
   api 'com.airbnb.android:lottie:4.1.0'
   api 'com.gyf.immersionbar:immersionbar:3.0.0'
   api 'com.makeramen:roundedimageview:2.3.0'
   api 'com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.6'
   api 'com.scwang.smartrefresh:SmartRefreshLayout:1.1.2'
   // gif图控件
   api 'pl.droidsonroids.gif:android-gif-drawable:1.2.24'
   
   // event
   api 'io.github.jeremyliao:live-event-bus-x:1.8.0'
       
   // mmkv，腾讯本地持久化
   api 'com.tencent:mmkv:1.2.11'
   ```

3. 接入依赖

   在项目根目录中加入添加

   ```groovy
   // Top-level build file where you can add configuration options common to all sub-projects/modules.
   buildscript {
       dependencies {
           classpath "com.android.tools.build:gradle:4.1.0"
           // kotlin插件
           classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21"
       }
       repositories {
           // 阿里云仓库
           maven { url 'https://maven.aliyun.com/repository/google' }
           maven { url 'https://maven.aliyun.com/repository/jcenter' }
           maven { url 'https://maven.aliyun.com/repository/public' }
           // jitpack仓库
           maven { url 'https://jitpack.io' }
       }
   }
   
   allprojects {
       repositories {
           maven { url 'https://maven.aliyun.com/repository/google' }
           maven { url 'https://maven.aliyun.com/repository/jcenter' }
           maven { url 'https://maven.aliyun.com/repository/public' }
           maven { url 'https://jitpack.io' }
       }
   }
   
   task clean(type: Delete) {
       delete rootProject.buildDir
   }
   ```

   添加依赖

   ```groovy
   implementation 'com.github.yxr2222222:BasicDemo:v1.3.2.202309281'
   ```

### 使用说明

#### Module的gradle完善

```groovy
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'

android {
    ...
    buildFeatures {
        viewBinding true
        dataBinding true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    ...
}

```

#### BaseApplication

使用该基础框架时必须继承BaseApplication

```kotlin
class MyApp : BaseApplication() {
    companion object {
        val headers = mutableMapOf("os" to "Android", "version" to "1.0")
    }

    override fun getHttpConfig(): HttpConfig {
        return HttpConfig.Builder()
            // 是否是Debug，Debug模式会添加HttpLoggingInterceptor，默认false
            .isDebug(BuildConfig.DEBUG)
            // 设置BaseUrl，必须设置
            .baseUrl("http://192.168.2.42:7722")
            // 设置每个步骤的超时时间，单位秒，默认10s
            .timeout(10)
            // 设置网络请求失败重试次数，默认0
            .retryNum(0)
            // 设置缓存，不需要可不设置
            .cache(
                CacheConfig(
                    cls = BaseResponse::class.java,
                    directory = PathUtil.getDir("/http-cache"),
                    maxSize = 1024 * 1024 * 1024
                )
            )
            // 添加拦截器，不需要可不设置
            .addInterceptor { chain -> chain.proceed(chain.request()) }
            // 设置网络环境切换配置，不需要可不设置
            .baseUrlReplaceConfig(
                BaseUrlReplaceConfig.Builder()
                    // 测试环境地址
                    .debugBaseUrl("http://192.168.2.42:7722")
                    // 正式环境地址
                    .formalBaseUrl("http://192.168.2.42:7722")
                    // 其他地址，没啥作用，用来切换界面快速切换的，可设置多个
                    .otherBaseUrl("https://www.baidu.com")
                    // 其他地址，没啥作用，用来切换界面快速切换的，可设置多个
                    .otherBaseUrl("https://github.com/")
                    .onBaseUrlReplaceCallback(object : OnBaseUrlReplaceCallback {
                        override fun getCustomFormalBaseUrl(): String? {
                            // 获取自定义的正式环境的BaseUrl，返回空的或者不是网络地址将使用@{@link com.yxr.base.http.BaseUrlReplaceConfig}的formalBaseUrl
                            return null
                        }

                        override fun onBaseUrlReplace(oldHost: String?, newHost: String?) {
                            // BaseUrl被替换了
                            Log.d(
                                HttpManager.TAG,
                                "onBaseUrlReplace oldHost: $oldHost, newHost: $newHost"
                            )
                        }
                    })
                    .build()
            )
            // 设置网络配置回调，不需要可不设置
            .callback(object : IHttpConfigCallback {
                override fun getPublicHeaders(httpUrl: HttpUrl): MutableMap<String, String> {
                    return headers
                }

                override fun onGlobalError(code: Int) {

                }
            })
            .build()
    }
}
```

#### MVVM

1. **BaseActivity、BaseFragment、BaseDialogFragment**
   ：基本的Activity、Fragment、DialogFragment；[Demo](./app/src/main/java/com/yxr/basicdemo/main/MainActivity.kt)
2. **BaseStatusActivity、BaseStatusFragment、BaseStatusDialogFragment**
   ：继承自上诉BaseXXX，是带有多状态UI和TitleBar的基类，其中多状态包括：加载中状态、内容状态、错误状态、空页面状态；[Demo](./app/src/main/java/com/yxr/basicdemo/status/StatusDemoActivity.kt)
3. **BaseViewModel**
   ：继承自AbsViewModel，接入了lifecycle管理生命周期，功能包含了：权限申请、Loading弹框、安全Toast、网络请求、线程切换、单击双击监听登；[Demo](./app/src/main/java/com/yxr/basicdemo/main/MainVM.kt)
4. **BaseStatusViewModel**
   ：继承自BaseViewModel，多了多状态切换、重新加载功能；[Demo](./app/src/main/java/com/yxr/basicdemo/status/StatusDemoVM.kt)
5. **BaseAdapterViewModel**
   ：继承自BaseStatusViewModel，配合BaseMultiItemQuickAdapter，快速实现列表功能；[Demo](./app/src/main/java/com/yxr/basicdemo/adapter)
6. **BasePageAdapterViewModel**
   ：继承自BaseAdapterViewModel，配合SmartRefreshLayout快速实现下拉刷新、下拉加载、分页加载的列表功能；[Demo](./app/src/main/java/com/yxr/basicdemo/refershload)

#### [应用内更新](./app/src/main/java/com/yxr/basicdemo/main/MainVM.kt)

```kotlin
UpdateManager.instance.checkUpdate(
    createApi(UpdaterApi::class.java).checkUpdate(
        machine = MachineUtil.getDeviceId(),
        version = PackageUtil.getVersionName(),
        packageName = PackageUtil.getPackageName()
    ), listener = null
)
```

#### 文件下载

```kotlin
private fun downloadFile(downloadUrl: String) {
    if (downloadUrl.isBlank()) return
    val bibleId = item.id?.toIntOrNull() ?: return

    val targetFile = File(PathUtil.getDir("/Download"), MD5Util.md5(downloadUrl) + ".txt")

    download(
        downloadUrl,
        targetFile,
        deleteExists = true,
        listener = object : OnDownloadListener {
            override fun onDownloadStart(downloadUrl: String, call: Call<ResponseBody>) {
                showLoading(getString(R.string.downloading))
            }

            override fun onDownloadProgress(downloadUrl: String, progress: Long, total: Long) {
            }

            override fun onDownloadSuccess(downloadUrl: String, file: File) {
                // TODO 文件下载成功
                dismissLoading()
            }

            override fun onDownloadFailed(downloadUrl: String, error: String?) {
                showToast(getString(R.string.download_failed) + ": $error")
                dismissLoading()
            }
        })
}
```

#### [K-V本地持久化](./app/src/main/java/com/yxr/basicdemo/config/MMKVConfig.kt)

```kotlin
class MMKVConfig {
    companion object {
        // 是否需要展示隐私政策
        var isNeedShowPrivacy = MMKVUtil.getBoolean("isNeedShowPrivacy", true)
            set(value) {
                field = value
                MMKVUtil.putBoolean("isNeedShowPrivacy", value)
            }

        // 当前正在阅读的书
        var currBook = MMKVUtil.getData("currBook", CurrBook::class.java, null)
            set(value) {
                field = value
                MMKVUtil.putData("currBook", value)
            }
    }
}
```



