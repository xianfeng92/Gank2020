package com.xforg.g2020.base;

/**
 * Created By apple on 2019/4/13
 * github: https://github.com/xianfeng92
 */
public class Platform {
    public static final boolean DEPENDENCY_AUTO_LAYOUT;
    public static final boolean DEPENDENCY_SUPPORT_DESIGN;
    public static final boolean DEPENDENCY_GLIDE;
    public static final boolean DEPENDENCY_ANDROID_EVENTBUS;
    public static final boolean DEPENDENCY_EVENTBUS;

    static {
        DEPENDENCY_AUTO_LAYOUT = findClassByClassName("com.zhy.autolayout.AutoLayoutInfo");
        DEPENDENCY_SUPPORT_DESIGN = findClassByClassName("android.support.design.widget.Snackbar");
        DEPENDENCY_GLIDE = findClassByClassName("com.bumptech.glide.Glide");
        DEPENDENCY_ANDROID_EVENTBUS = findClassByClassName("org.simple.eventbus.EventBus");
        DEPENDENCY_EVENTBUS = findClassByClassName("org.greenrobot.eventbus.EventBus");
    }

    private static boolean findClassByClassName(String className) {
        boolean hasDependency;
        try {
            Class.forName(className);
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }
        return hasDependency;
    }
}
