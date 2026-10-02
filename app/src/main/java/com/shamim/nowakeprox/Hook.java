package com.shamim.nowakeprox;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!"android".equals(lpparam.packageName))
            return;
        try {
            Class<?> c = XposedHelpers.findClass(
                "com.android.server.policy.BaseMiuiPhoneWindowManager",
                lpparam.classLoader);
            XposedBridge.hookAllMethods(c,
                "registerProximitySensor",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) {
                        return null; // skip registering the wake key proximity listener
                    }
                });
            XposedBridge.log("NoWakeProximity: hooked registerProximitySensor");
        } catch (Throwable t) {
            XposedBridge.log("NoWakeProximity: " + t);
        }
    }
}
