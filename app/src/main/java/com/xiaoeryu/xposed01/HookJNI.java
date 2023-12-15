package com.xiaoeryu.xposed01;

import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookJNI implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable{
        Log.i("Xposed01", loadPackageParam.packageName);
        XposedBridge.log("Xposed01->app packagename" + loadPackageParam.packageName);
        if (loadPackageParam.packageName.equals("com.kanxue.loaddex")){
            Class MainActivityClass = XposedHelpers.findClass("com.kanxue.loaddex.MainActivity", loadPackageParam.classLoader);

            XposedHelpers.findAndHookMethod(MainActivityClass, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("beforeHookedMethod->onCreate: " + param.args[0]);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("afterHookedMethod->onCreate: " + param.thisObject);
                }
            });
        }
    }
}
