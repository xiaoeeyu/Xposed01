package com.xiaoeryu.xposed01;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookActiveInvoke implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable{
        Log.i("Xposed01", loadPackageParam.packageName);
        XposedBridge.log("Xposed01->app packagename" + loadPackageParam.packageName);
        if(loadPackageParam.packageName.equals("com.xiaoeryu.xposedhook01")){
            XposedBridge.log("Xposed01->app packagename" + loadPackageParam.packageName);

//            public static String publicStaticFunc(String arg1, int arg2){
//                String result = privateStaticFunc("privateStaticFunc", 200);
//                Log.i("LSPosed", "publicStaticFunc is called!");
//                return arg1 + "---" + arg2 + "---" + result;
//            }
            ClassLoader classLoader = loadPackageParam.classLoader;
            Class StuClass = classLoader.loadClass("com.xiaoeryu.xposedhook01.Student");
            Method publicStaticFunc_method = StuClass.getDeclaredMethod("publicStaticFunc", String.class, int.class);
            publicStaticFunc_method.invoke(null,"InvokedByXposed", 1000);

//            private static String privateStaticFunc(String arg1, int arg2){
//                Log.i("LSPosed", "privateStaticFunc is called!");
//                return arg1 + "---" + arg2;
//            }
            Method privateStaticFunc_method = StuClass.getDeclaredMethod("privateStaticFunc", String.class, int.class);
            privateStaticFunc_method.setAccessible(true);
            String result = (String) privateStaticFunc_method.invoke(null,"InvokedByXposed", 2000);
            XposedBridge.log("privateStaticFuncIsInvokeedByLSPosed->result: " + result);

//            public String publicFunc(String arg1, int arg2){
//                String result = privateFunc("privateFunc", 300);
//                Log.i("LSPosed", "publicFunc is called!" + arg1 + "---" + arg2);
//                person tmp = new person();
//                String tmpName = tmp.getPersonName("lisi");
//                return arg1 + "---" + arg2 + "---" + result + "---" + tmpName;
//            }

            Method publicFunc_method = StuClass.getDeclaredMethod("publicFunc", String.class, int.class);
//            public Student(String name, String id){
//                this.name = name;
//                this.id = id;
//            }
            Constructor StuCon = StuClass.getDeclaredConstructor(String.class, String.class);
            Object StuObj = StuCon.newInstance("InstanceByXposed", "3000");
            publicFunc_method.invoke(StuObj,"publicFuncinvokeedByLSPosed", 1111);

//            private String privateFunc(String arg1, int arg2){
//                Log.i("LSPosed", "privateFunc is called!" + arg1 + "---" + arg2);
//                return arg1 + "---" + arg2;
//            }
            Method privateFunc_method = StuClass.getDeclaredMethod("privateFunc", String.class, int.class);
            privateFunc_method.setAccessible(true);
            String ret = (String) privateFunc_method.invoke(StuObj, "privateFuncInvokeedByLSPosed", 2222);
            XposedBridge.log("privateFuncIsInvokeByLSPosed->ret: " + ret);

            XposedHelpers.callStaticMethod(StuClass, "publicStaticFunc", "publicStaticFuncInvokeByLSPosed", 3333);
            XposedHelpers.callStaticMethod(StuClass, "privateStaticFunc", "privateStaticFuncInvokeByLSPosed", 4444);
            XposedHelpers.callMethod(StuObj, "publicFunc", "publicFuncInvokeByLSPosed", 5555);
            XposedHelpers.callMethod(StuObj, "privateFunc", "privateFuncInvokeByLSPosed", 6666);

//            Student cStudent = new Student("xiaohua", "2023");
            XposedHelpers.findAndHookConstructor(StuClass, String.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("beforeHookedMethod->" + "name: " + param.args[0] + "---" + "id: " + param.args[1]);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object cStudent = param.thisObject;
                    XposedHelpers.callMethod(cStudent,  "publicFunc", "publicFuncInvokeByLSPosed", 5050);
                    XposedHelpers.callMethod(cStudent, "privateFunc", "privateFuncInvokeByLSPosed", 6060);
                }
            });
//            public String getNickname() {
//                return nickname;
//            }
            XposedHelpers.findAndHookMethod(StuClass, "getNickname", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object obj=param.thisObject;
                    XposedHelpers.callMethod(obj,"publicFunc","beforeHookedMethod publicfunc is called XposedHelpers.callMethod",444);
                    XposedHelpers.callMethod(obj,"privateFunc","beforeHookedMethod privatefunc is called XposedHelpers.callMethod",333);

                    XposedBridge.log("getNickname is called beforeHookedMethod->"+obj);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object obj=param.thisObject;
                    XposedHelpers.callMethod(obj,"publicFunc","afterHookedMethod publicfunc is called XposedHelpers.callMethod",222);
                    XposedHelpers.callMethod(obj,"privateFunc","afterHookedMethod privatefunc is called XposedHelpers.callMethod",111);

                    XposedBridge.log("getNickname is called afterHookedMethod->"+param.thisObject);
                }
            });
        }
    }
}
