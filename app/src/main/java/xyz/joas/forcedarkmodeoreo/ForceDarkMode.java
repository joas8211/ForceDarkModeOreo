package xyz.joas.forcedarkmodeoreo;

import android.app.WallpaperColors;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.*;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

/**
 * Hook methods that handle WallpaperColors and override the value
 * with one that should trigger dark mode, always.
 *
 * Embrace the darkness.
 *
 * Hooked methods:
 *  - android.service.wallpaper.WallpaperService.Engine.onComputeColors()
 *  - android.app.WallpaperManager.Globals.getWallpaperColors()
 *  - android.app.WallpaperManager.Globals.onWallpaperColorsChanged()
 */
public class ForceDarkMode implements IXposedHookLoadPackage {

    /**
     * Create WallpaperColors with dark color hints without using internal API.
     *
     * @see WallpaperColors#calculateDarkHints
     * @see WallpaperColors#setColorHints
     *
     * @return WallpaperColors which should trigger dark mode.
     */
    private WallpaperColors getBlackWallpaperColors() {
        // Draw 1x1 (pixel) black bitmap.
        Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawPoint(0, 0, paint);

        return WallpaperColors.fromBitmap(bitmap);
    }

    /**
     * Add Xposed log message of the override.
     *
     * @param packageName Package name of overridden method.
     * @param methodName Name of overridden method.
     */
    private void logOverride(String packageName, String methodName) {
        log(String.format("ForceDarkModeOreo: %s called at %s, overriding color values...",
                methodName,
                packageName));
    }

    /**
     * Replace method and pass the colors as a return value.
     */
    private class MethodReplacementHook extends XC_MethodReplacement {
        private String packageName;

        public MethodReplacementHook(String packageName) {
            this.packageName = packageName;
        }

        @Override
        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
            logOverride(packageName, param.method.getName());
            return getBlackWallpaperColors();

        }
    }

    /**
     * Inject the colors as the first argument.
     */
    private class ArgumentInjectionHook extends XC_MethodHook {
        private String packageName;

        public ArgumentInjectionHook(String packageName) {
            this.packageName = packageName;
        }

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            logOverride(packageName, param.method.getName());
            param.args[0] = getBlackWallpaperColors();
            super.beforeHookedMethod(param);
        }
    }

    /**
     * Hook the methods on package load.
     *
     * @param lpparam Parameters of loaded package.
     */
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        findAndHookMethod("android.app.WallpaperManager.Globals",
                lpparam.classLoader,
                "getWallpaperColors",
                int.class,
                int.class,
                new MethodReplacementHook(lpparam.packageName));

        findAndHookMethod("android.app.WallpaperManager.Globals",
                lpparam.classLoader,
                "onWallpaperColorsChanged",
                WallpaperColors.class,
                int.class,
                int.class,
                new ArgumentInjectionHook(lpparam.packageName));

        findAndHookMethod("android.service.wallpaper.WallpaperService.Engine",
                lpparam.classLoader,
                "onComputeColors",
                new MethodReplacementHook(lpparam.packageName));
    }
}
