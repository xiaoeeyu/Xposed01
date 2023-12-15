package com.xiaoeryu.xposed01;

import android.util.Log;

import dalvik.system.DexClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookDexClassLoader implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        System.out.println("HookDexClassLoader");
        Log.i("HookDexClassLoader", loadPackageParam.packageName);
        XposedBridge.log("HookDexClassLoader->app packagename" + loadPackageParam.packageName);
        if (loadPackageParam.packageName.equals("com.kanxue.loaddex")) {
            //    public DexClassLoader(String dexPath, String optimizedDirectory,
            //            String librarySearchPath, ClassLoader parent) {
            //        super(dexPath, null, librarySearchPath, parent);
            //    }
            XposedHelpers.findAndHookConstructor(DexClassLoader.class, String.class, String.class, String.class, ClassLoader.class, new XC_MethodHook() {
                @Override
                // 		public Object thisObject;
                //		public Object[] args;
                //		private Object result = null;
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object args[] = param.args;
                    String dexPath = (String) args[0];
                    String optimizedDirectory = (String) args[1];
                    String librarySearchPath = (String) args[2];
                    ClassLoader parent = (ClassLoader) args[3];
                    XposedBridge.log("HookDexClassLoader->beforeHookedMethod: " + dexPath + "---" + optimizedDirectory + "---" + librarySearchPath + "---" + parent);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    DexClassLoader dexClassLoader = (DexClassLoader) param.thisObject;
                    XposedBridge.log("HookDexClassLoader->afterHookedMethod: " + dexClassLoader);
                }
            });
        }
    }
}
