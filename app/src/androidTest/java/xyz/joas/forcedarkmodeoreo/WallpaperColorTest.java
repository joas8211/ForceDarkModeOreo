package xyz.joas.forcedarkmodeoreo;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.robv.android.xposed.XposedBridge;

@RunWith(AndroidJUnit4.class)
public class WallpaperColorTest {
    private String colorToString(Color color) {
        return String.format("#%02x", Float.floatToIntBits(color.toArgb()));
    }

    private void logColors(WallpaperColors colors) {
        XposedBridge.log(String.format("%s %s %s",
                colorToString(colors.getPrimaryColor()),
                colorToString(colors.getPrimaryColor()),
                colorToString(colors.getPrimaryColor())));
    }

    @Test
    public void logWallpaperColor() {
        Context context = InstrumentationRegistry.getTargetContext();
        WallpaperManager wpManager = context.getSystemService(WallpaperManager.class);
        try {
            WallpaperColors wallpaperColors = wpManager.getWallpaperColors(WallpaperManager.FLAG_SYSTEM);
            if (wallpaperColors == null) {
                Log.d("FDM: Test: Color", "Null");
            } else {
                Log.d("FDM: Test: Color: Primary", colorToString(wallpaperColors.getPrimaryColor()));
                Log.d("FDM: Test: Color: Secondary", colorToString(wallpaperColors.getSecondaryColor()));
                Log.d("FDM: Test: Color: Tertiary", colorToString(wallpaperColors.getTertiaryColor()));
            }
        } catch (NullPointerException e) {
            Log.d("FDM: Test: Color", "Null", e);
        }
    }
}
