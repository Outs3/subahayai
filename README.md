# Subahayai

以更快的开发速度为目标的Android MVVM框架，最终目标是成为陆行鸟  
  
[![](https://jitpack.io/v/Outs3/subahayai.svg)](https://jitpack.io/#Outs3/subahayai)  
  
## 简单用例
#### 1.在 应用级 build.gradle 中添加
```
allprojects {  
    repositories {  
        maven { url "https://jitpack.io" }  
    }  
}
```

   
#### 2.在 模块级 build.gradle 中添加具体依赖
```
//如果使用DataBinding  
implementation 'com.github.Outs3.subahayai:sbhyi-databinding:0.0.9'
  
//如果使用Jetpack Compose  
implementation 'com.github.Outs3.subahayai:sbhyi-compose:0.0.9'
  
//如果仅使用扩展工具  
implementation 'com.github.Outs3.subahayai:sbhyi-kts:0.0.9'
implementation 'com.github.Outs3.subahayai:sbhyi-akts:0.0.9'
  
//所有可依赖项（databinding、compose已经依赖了kts、akts、acore，不需要重复依赖）  
implementation 'com.github.Outs3.subahayai:sbhyi-kts:0.0.9'			//Kotlin Extension
implementation 'com.github.Outs3.subahayai:sbhyi-akts:0.0.9'			//Android Kotlin Extension
implementation 'com.github.Outs3.subahayai:sbhyi-acore:0.0.9'			//Android Framework Core
implementation 'com.github.Outs3.subahayai:sbhyi-databinding:0.0.9'		//Android DataBinding Framework
implementation 'com.github.Outs3.subahayai:sbhyi-compose:0.0.9'			//Android Compose Framework
```
