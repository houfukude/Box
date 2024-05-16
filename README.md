# Box

=== Source Code - Editing the app default settings ===
/src/main/java/com/github/tvbox/osc/base/App.java

    private void initParams() {

        putDefault(HawkConfig.HOME_REC, 2);       // Home Rec 0=豆瓣, 1=推荐, 2=历史
        putDefault(HawkConfig.PLAY_TYPE, 1);      // Player   0=系统, 1=IJK, 2=Exo
        putDefault(HawkConfig.IJK_CODEC, "硬解码");// IJK Render 软解码, 硬解码
        putDefault(HawkConfig.HOME_SHOW_SOURCE, true);  // true=Show, false=Not show
        putDefault(HawkConfig.HOME_NUM, 2);       // History Number
        putDefault(HawkConfig.DOH_URL, 2);        // DNS
        putDefault(HawkConfig.SEARCH_VIEW, 2);    // Text or Picture

    }

## Houfukude 修改

#### 20240515 首页按钮逻辑变化

为了在平板等没有实体 **菜单** 按钮的设备上使用

* 首页左上角点击图标 _换源_ 功能
* 原来的 _重载_ 功能放到了 **样式** 的长按上了

#### 20240516 新增 自更新功能 

配合 github actions 可以实现编译后发布到 GitHub release 

然后通过软件内的点击 `关于` 进行更新查询并进行自动下载安装

不依赖特定仓库，默认编译仓库即为自动更新的源路径

PS 极度依赖 **BUILD_TIME** 这个参数进行全局判断 包括在 workflow 和 更新模块