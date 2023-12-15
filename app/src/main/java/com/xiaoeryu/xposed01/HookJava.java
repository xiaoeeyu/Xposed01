package com.xiaoeryu.xposed01;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookJava implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable{
        Log.i("Xposed01", loadPackageParam.packageName);
        XposedBridge.log("Xposed01->app packagename" + loadPackageParam.packageName);
        if (loadPackageParam.packageName.equals("com.xiaoeryu.xposedhook01")) {
/*            ClassLoader classLoader = loadPackageParam.classLoader;
            Class StuClass = classLoader.loadClass("com.xiaoeryu.xposedhook01.Student");

            XposedHelpers.findAndHookMethod(StuClass, "privateFunc", String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] args = param.args;
                    String arg0 = (String) args[0];
                    int arg1 = (int) args[1];
                    args[0] = "changeByXposedJava";
                    args[1] = 666;
                    XposedBridge.log("beforeHookedMethod->" + "arg0: " + arg0 + "---" + "arg1: " + arg1);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    String result = (String) param.getResult();
                    param.setResult("changeByXposedJava->afterHookedMethod");
                    XposedBridge.log("afterHookedMethod->" + "result: " + result);
                }
            });*/
            XposedHelpers.findAndHookMethod("com.xiaoeryu.xposedhook01.Student",loadPackageParam.classLoader,"privateFunc",String.class,int.class,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] args = param.args;
                    String arg0 = (String) args[0];
                    int arg1 = (int) args[1];
                    args[0] = "changeByXposedJava";
                    args[1] = 666;
                    XposedBridge.log("beforeHookedMethod->" + "arg0: " + arg0 + "---" + "arg1: " + arg1);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    String result = (String) param.getResult();
                    param.setResult("changeByXposedJava->afterHookedMethod");
                    XposedBridge.log("afterHookedMethod->" + "result: " + result);
                }
            });
            Class personClass = XposedHelpers.findClass("com.xiaoeryu.xposedhook01.Student$person",loadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(personClass, "getPersonName", String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("beforeHookedMethod->" + "getPersonName: " + param.args[0]);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("afterHookedMethod->" + "getPersonName: " + param.getResult());
                }
            });
        }
    }
}
