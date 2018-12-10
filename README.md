# Force Dark Mode for Android Oreo (8.1)

Google [introduced](https://developer.android.com/about/versions/oreo/android-8.1#wallpaper) Android's new WallpaperColors API for Android 8.1 in December 2017. 

This module is for bypassing the API to force system wide dark mode by returning always black color values. And so your eyes will be saved from light theme UI. Dark theme is automatically enabled when using dark wallpapers, but the problem is usually live wallpapers which must implement the API as they are dedicated applications and in control of the wallpaper rendering, otherwise system defaults to using light theme.

## Requirements
* Android 8.1 (API 27) or newer
* [Xposed Framework version 90 or newer](https://forum.xda-developers.com/showthread.php?t=3034811)

## Usage

1. Download and enable module.
2. Reboot.
3. Embrace the darkness.

# Links
* [Xposed repository](http://repo.xposed.info/module/xyz.joas.forcedarkmodeoreo)
* [XDA-developers thread](https://forum.xda-developers.com/xposed/modules/mod-forcedarkmodeoreo-t3786614)
