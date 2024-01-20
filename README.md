### DQLive

#### Android直播客户项目

#### 直播应用实战！欢迎Star，持续更新。


基于声网SDK实现的推拉流，项目虽小，五脏俱全。
特色：优良的直播间的架构设计，观众端直播间和主播端，模块复用、可扩展性强，自定义生命周期感知的controller组件，实现按钮粒度的逻辑分离、自由组合。

Github项目地址：[https://github.com/duqian291902259/DQLive](https://github.com/duqian291902259/DQLive)

仅供学习交流技术，应用的sdk、资源和图片素材，如有侵权请联系删除。

### 项目截图
![DQLive_Main.png](https://github.com/duqian291902259/DQLive/blob/main/screenshot/DQLive_Main.png)

视频效果：[DQLive.mp4](https://github.com/duqian291902259/DQLive/blob/main/screenshot/DQLive.mp4)

### 直播技术选型

1. 主播端：推流基于声网SDK，然后旁路推流到CDN，所以独特的地方是开播的流程和主播端的音视频处理。其他常规的功能跟观众端类似。
1. 观众端：拉流播放，可选的播放器有ijkplayer，exo，或者声网MPK；其他主播资料卡，IM功能，打赏功能。
1. UI架构：Activity+Fragment+ViewModle+LiveData
1. 自定义的controller（支持热插拔的模块化，生命周期感知）
1. 组件通信：目前EventBus，会新增LiveDataBus。 
1. 模块化、组件化：AutoService。 
1. 依赖注入：Hilt。 
1. 网络相关：Retrofit + Kotlin Coroutine + Gson。 
1. 持久化：Room，MMKV。 
1. 动画：前期SVGA，后期会+Lottie、Alpha MP4。
1. 直播使用哪种技术，需要结合各自项目的技术特点，考虑项目同学的开发习惯和技术掌握程度。我也会逐步引入一些项目中没有使用过的，但是很Nice的技术。

### 注意事项

* 直播间的层级，很重要！

* 直播间的功能可以很多，设计、新增每一个模块，都必须考虑的尽可能兼顾高可用、稳定性、可拓展性、可复用性。

* 基础组件下沉，做到面向接口编程，尽量模块化、组件化。

* 方便项目功能模块热插拔，新项目快速复用或者删减。 

* 目前直播间涉及的Modules，很多在直播间和主播端复用。

* 推拉流的模块LivePusher+MediaPlayer，都是做成单独的module工程，可以打aar，对外提供Service。

### 开发的原则和规范

规范，可以避免很多没有必要发生的问题，细节不一定决定成败，但可能会影响效率和心情。

#### 命名规范

* 各种文件、资源文件命名规范，能方便的检索到想要的内容，也方便以后模块化分离或者重构。

*  比如图片，一般前缀ic_ ，bg_或者img_. +大模块名+功能名+状态(如果有)
* 。比如ic_live_room_chat_normal.png，ic_push_room_gift_selected.png，大图bg_live_room_loading_default.png。

* 通用的资源，cm_
* 布局常规的就还按照常规的来：activity_,fragment_,dialog_,layout_,item_

* 加上模块名+功能名，表意明确。

* 一般做过插件化的话，资源名太普通或者太短都比较容易冲突。

* 而规范的命名，做组件化、模块化的时候也比较容易分离资源。

* 一般room表示直播间和开播端通用资源，pushroom才是开播端独有的。


#### 开发约定

1. 直播间是一个单Activity的复杂业务载体，附加在里面的功能，一般都要继承直播间的基类开发，方便统一管理。

1. 属于直播间内部的新创建的弹窗，满屏或者半屏幕页面，不要使用Activity，继承BaseFragment，或者BaseModule开发。

1. 弹窗推荐使用BaseDataBindingDialog（DialogFragment），这样的话，方便子模块的统一管理，也不会因为activity的生命周期变化，影响了直播间的推拉流的逻辑。

1. 大的UI，请使用ViewStub懒加载布局。

### 其他
推拉流是正常的，只需要修改推拉流地址即可测试。

其实还有很多可以加入的功能，如AI主播，换脸等。后续持续更新。

duqian2010@gmail.com
