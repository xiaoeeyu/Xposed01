package com.xiaoeryu.xposed01;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookConstructors implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.xiaoeryu.xposedhook01")) {
//            XposedHook.hookConstructors(loadPackageParam);

            ClassLoader classLoader = loadPackageParam.classLoader;
            Class StudentClass = classLoader.loadClass("com.xiaoeryu.xposedhook01.Student");
            XposedHelpers.findAndHookConstructor(StudentClass, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("xiaoeryu->beforeHookedMethod");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("xiaoeryu->afterHookedMethod");
                }
            });
//     public Student(String name){
//        this.name = name;
//        id = "default";
//    }

// 		public Object thisObject;
//
//		public Object[] args;
//
//		private Object result = null;
            XposedHelpers.findAndHookConstructor(StudentClass, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] args = param.args;
                    String name = (String) args[0];
                    XposedBridge.log("com.xiaoeryu.xposedhook01.Student(String)->beforeHookedMethod: " + name);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("com.xiaoeryu.xposedhook01.Student(String)->afterHookedMethod");
                }
            });

//     public Student(String name, String id){
//        this.name = name;
//        this.id = id;
//    }
            XposedHelpers.findAndHookConstructor(StudentClass, String.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] args = param.args;
                    String name = (String) args[0];
                    String id = (String) args[1];
                    XposedBridge.log("com.xiaoeryu.xposedhook01.Student(String, String)->beforeHookedMethod: " + name + "--" + id);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("com.xiaoeryu.xposedhook01.Student(String, String)->afterHookedMethod");
                }
            });
// public Student(String name, String id, int age)
/*            XposedHelpers.findAndHookConstructor(StudentClass, String.class, String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] args = param.args;
                    String name = (String) args[0];
                    String id = (String) args[1];
                    int age = (int) args[2];
                    XposedBridge.log("com.xiaoeryu.xposedhook01.Student(String, String, int)->beforeHookedMethod" + name + "--" + id + "--" + age);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("com.xiaoeryu.xposedhook01.Student(String, String, int)->afterHookedMethod");
                }
            });*/
            XposedHelpers.findAndHookConstructor("com.xiaoeryu.xposedhook01.Student", loadPackageParam.classLoader, String.class, String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] args = param.args;
                    String name = (String) args[0];
                    args[1] = "2050";
                    args[2] = 200;
                    String id = (String) args[1];
                    int age = (int) args[2];
                    XposedBridge.log("com.xiaoeryu.xposedhook01.Student(String, String, int)->beforeHookedMethod: " + name + "--" + id + "--" + age);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object thisObj = param.thisObject;
                    Object returnObj = param.getResult();
                    XposedBridge.log( thisObj + "---" + returnObj);
                    XposedBridge.log("com.xiaoeryu.xposedhook01.Student(String, String, int)->afterHookedMethod");
                }
            });
        }
    }
}
