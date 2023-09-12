## BasicDemo

基于AndroidX + kotlin + mvvm + 协程的基础框架

### 安装说明

1. 编译环境

   ```java
   Gradle:       7.2
   Kotlin:       1.5.21
   JDK           11
   ```

2. 内部包含引用

   ```java
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

   ```java
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

   ```java
   implementation 'com.github.yxr2222222:BasicDemo:v1.3.0.202308311'
   ```

### 使用说明

#### Module的gradle完善

```java
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

```java
class MyApp : BaseApplication() {
    companion object {
        val headers = mutableMapOf("os" to "Android", "version" to "1.0")
    }

    override fun getHttpConfig(): HttpConfig {
        return HttpConfig.Builder()
            // 是否是Debug，Debug模式会添加HttpLoggingInterceptor，默认false
            .isDebug(BuildConfig.DEBUG)
            // 设置BaseUrl，必须设置
            .baseUrl("http://www.baidu.com")
            // 设置每个步骤的超时时间，单位秒，默认10s
            .timeout(10)
            // 设置网络请求失败重试次数，默认3
            .retryNum(3)
            // 设置缓存，不需要可不设置
            .cache(Cache(PathUtil.getDir("/http-cache"), maxSize = 1024 * 1024 * 1024))
            // 添加拦截器，不需要可不设置
            .addInterceptor { chain -> chain.proceed(chain.request()) }
            // 设置网络配置回调，不需要可不设置
            .callback(object : IHttpConfigCallback {
                override fun getPublicHeaders(): MutableMap<String, String> {
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

1. **BaseActivity、BaseFragment、BaseDialogFragment**：基本的Activity、Fragment、DialogFragment；
2. **BaseStatusActivity、BaseStatusFragment、BaseStatusDialogFragment**：继承自上诉BaseXXX，是带有多状态UI和TitleBar的基类，其中多状态包括：加载中状态、内容状态、错误状态、空页面状态；
3. **BaseViewModel**：继承自AbsViewModel，接入了lifecycle管理生命周期，功能包含了：权限申请、Loading弹框、安全Toast、网络请求、线程切换、单击双击监听登；
4. **BaseStatusViewModel**：继承自BaseViewModel，多了多状态切换、重新加载功能；
5. **BaseAdapterViewModel**：继承自BaseStatusViewModel，配合BaseMultiItemQuickAdapter，快速实现列表功能；
6. **BasePageAdapterViewModel**：继承自BaseAdapterViewModel，配合SmartRefreshLayout快速实现下拉刷新、下拉加载、分页加载的列表功能；



