package com.xiaoeryu.xposed01;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookHHLZ implements IXposedHookLoadPackage {
    public static ClassLoader getClassloader() {
        ClassLoader resultClassloader = null;

        // 使用XposedHelpers.findClass找到ActivityThread类
        Class<?> activityThreadClass = XposedHelpers.findClass("android.app.ActivityThread", null);

        // 调用currentActivityThread静态方法获取当前ActivityThread对象
        Object currentActivityThread = XposedHelpers.callStaticMethod(activityThreadClass, "currentActivityThread");

        // 获取mBoundApplication字段的值
        Object mBoundApplication = XposedHelpers.getObjectField(currentActivityThread, "mBoundApplication");

        // 获取LoadedApk对象
        Object loadedApkInfo = XposedHelpers.getObjectField(mBoundApplication, "info");

        // 获取mApplication字段的值
        Application mApplication = (Application) XposedHelpers.getObjectField(loadedApkInfo, "mApplication");

        // 获取ClassLoader
        resultClassloader = mApplication.getClassLoader();

        return resultClassloader;
    }

    public void GetClassLoaderClasslist(ClassLoader classLoader){
        XposedBridge.log("lebo->start dealwith classLoader: " + classLoader);
        // private final DexPathList pathList;
        Object pathListObj = XposedHelpers.getObjectField(classLoader, "pathList");
        // private Element[] dexElements;
        Object[] dexElementsObj = (Object[]) XposedHelpers.getObjectField(pathListObj,"dexElements");
        for (Object i:dexElementsObj){
            // private final DexFile dexFile;
            Object dexFileObj = XposedHelpers.getObjectField(i,"dexFile");
            // private Object mCookie;
            Object mCookieObj = XposedHelpers.getObjectField(dexFileObj,"mCookie");
            // private static native String[] getClassNameList(Object cookie);
            Class DexFileClass = XposedHelpers.findClass("dalvik.system.DexFile",classLoader);
            String[] classNameList = (String[]) XposedHelpers.callStaticMethod(DexFileClass,"getClassNameList",mCookieObj);
            for (String j:classNameList){
                XposedBridge.log(dexFileObj+ "---" + j);
            }
        }
        XposedBridge.log("lebo->end dealwith classLoader: " + classLoader);
    }
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable{
        Log.i("Xposed01", loadPackageParam.packageName);
        XposedBridge.log("HHLZ->app packagename: " + loadPackageParam.packageName);
        if (loadPackageParam.packageName.equals("jp.inc.nagisa.mangapf")) {
            XposedBridge.log("Xposed01->hook HHLZ: " + loadPackageParam.packageName);
            ClassLoader classLoader = loadPackageParam.classLoader;

            XposedBridge.log("loadPackageParam.classLoader: " + classLoader);
//            GetClassLoaderClasslist(classLoader);
//            ClassLoader parent = classLoader.getParent();
//            while (parent != null) {
//                XposedBridge.log("lebo->parent: " + parent);
//                if (parent.toString().contains("BootClassLoader")){
//
//                }else {
//                    GetClassLoaderClasslist(parent);
//                }
//                parent = parent.getParent();
//            }
            Class StubAppClass = XposedHelpers.findClass("com.stub.StubApp",loadPackageParam.classLoader);
            Method[] methods = StubAppClass.getDeclaredMethods();
            for (Method i:methods){
                XposedBridge.log("com.stub.StubApp: " + i);
            }
            XposedHelpers.findAndHookMethod("com.stub.StubApp", loadPackageParam.classLoader, "onCreate", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("com.stub.StubApp->beforeHookedMethod->onCreate: " + param.thisObject);
                    XposedBridge.log("com.stub.StubApp: " + loadPackageParam.classLoader);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("com.stub.StubApp->afterHookedMethod->onCreate: " + param.thisObject);

                    ClassLoader finalClassLoader = getClassloader();
                    XposedBridge.log("finalClassLoader: " + finalClassLoader);
                    Class MainActivity_Class = XposedHelpers.findClass("com.omyga.app.ui.activity.MainActivity_",finalClassLoader);
                    Method[] methods = MainActivity_Class.getDeclaredMethods();
                    for (Method i:methods){
                        XposedBridge.log("com.omyga.app.ui.activity.MainActivity_: " + i);
                    }
                    XposedHelpers.findAndHookMethod("com.omyga.app.ui.activity.MainActivity_", finalClassLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            XposedBridge.log("beforeHookedMethod com.omyga.app.ui.activity.MainActivity_.onCreate");
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            XposedBridge.log("afterHookedMethod com.omyga.app.ui.activity.MainActivity_.onCreate");
                        }
                    });
                }
            });
        }
    }
}
