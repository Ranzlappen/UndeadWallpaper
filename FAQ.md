### 🧟‍♂️ The Horde's Survival Guide: Troubleshooting & FAQ

**Q: My wallpaper freezes, stops playing, or reverts to a static background. Why?**

**A:** Your phone’s OS is acting like a ruthless zombie hunter. Aggressive battery optimizations are silently killing the wallpaper service in the background.

* **The Fix:** UndeadWallpaper has a built-in "Allow Background Performance" card. Tap "Fix" and follow the instructions to jump into your settings.
* **Crucial Step:** Do _not_ just flip the main background toggle. You must actively tap into the app's battery settings and select **Unrestricted** (or "Don't Optimize"). Once you untether it, the warning card will disappear.

**Q: Can I set a different video for the Lock Screen and the Home Screen?**

**A:** Yes! Open **Settings → Lock screen**, flip on *"Different video on lock screen"*, and pick a video from your playlist. While your phone is locked the engine plays that clip, then snaps back to your home wallpaper the instant you unlock.

* **One engine, not two:** Early on this was a hard "no", because the naive approach needs two separate background services (double the video decoders, double the playlists, double the battery drain, and a one-way ticket to getting your app assassinated by the memory manager). We avoided all of that: there is still a *single* optimized pipeline that simply swaps which video it plays based on the keyguard state. No second service, no doubled memory.
* **The big caveat (Xiaomi/POCO/Redmi/HyperOS, and some others):** Some manufacturers render their *own* separate wallpaper on the lock screen and never hand it to third-party live wallpapers. On those devices the feature can't take effect no matter what an app does. See the Xiaomi lock-screen workaround below — if you can get UndeadWallpaper showing on the lock screen at all, this feature will work; if the OS refuses to show live wallpapers there, it won't.

**Q: I enabled Home Screen Gestures (Double/Triple Tap), but nothing happens when I tap!**

**A:** If your taps are being ignored, one of these three system rules is blocking them from reaching the wallpaper:

* **The Lock Screen Blockade:** Gestures will *never* work on the lock screen. Android natively blocks live wallpapers from receiving touch inputs on the lock screen for security reasons. This is a hard, unbypassable OS rule.
* **Greedy Custom Launchers:** If you use a custom launcher (like **Nova**, **Smart Launcher**, **Niagara**, etc.), it is likely stealing your taps. For example, if your launcher has a "Double tap to turn off screen" feature enabled, it intercepts your fingers before the wallpaper ever feels them. You must disable the launcher's gesture in its own settings to let the taps pass through to the horde.
* **The Preview Screen:** Gestures are intentionally disabled while you are looking at the system's "Apply Wallpaper" preview screen to prevent hardware bugs on certain manufacturer interfaces (like Vivo, Oppo, or Xiaomi). Apply the wallpaper first, then tap your actual Home Screen.

**Q: I have a Xiaomi/POCO/Redmi phone. The video applies to my Home Screen, but my Lock Screen is still static.**

**A:** Xiaomi's MIUI/HyperOS actively blocks third-party live wallpapers on the lock screen. To bypass this:

1. Open your phone's default **Themes** app.
2. Find any default **Live Wallpaper** and apply it to BOTH your Home and Lock screens.
3. Open UndeadWallpaper and apply your video. The OS will now allow the app to override the stock wallpaper.

**Q: Why do my status bar icons turn dark/gray instead of matching the wallpaper colors?**

**A:** UndeadWallpaper actively extracts Material You colors and sends a direct suggestion to your system. If your icons look wrong, your OS is ignoring the app.

* **Samsung Users:** The One UI 8.5 update broke compatibility and ignores standard Android color codes. Instead, it forcefully scans the screen and tries to guess the colors on its own. This still works flawlessly on Pixels, vanilla Android, and older One UI versions, but for modern Samsung devices, it is an unfixable OS quirk for now.

**Q: I change the Accent Color setting, but my system theme color never updates. (Xiaomi/HyperOS)**

**A:** This is an OS-side caching quirk, not a missing feature. When you tap an accent option the app immediately saves it and tells the system its colors changed (the standard Android `notifyColorsChanged` call). On Pixels and vanilla Android the system re-reads the palette right away. HyperOS (and a few other heavy skins) **cache the wallpaper palette** and ignore that live notification — they only re-read colors when a wallpaper is freshly *applied*. There is no public Android API for an app to force that re-read.

* **The Workaround:** Pick your accent, then re-apply the wallpaper (re-select UndeadWallpaper from the wallpaper picker) so the OS re-reads the palette.
* **A note on "Off":** Because the system caches the last palette, turning the accent *Off* can leave the previously-pushed color in place until something else re-themes your system — there's no app-side way to shove the manufacturer's original default back in.

**Q: The file picker doesn't show video thumbnails, or the picker is missing entirely!**

**A:** UndeadWallpaper strictly uses Android's native system file picker to guarantee security and privacy.

* If thumbnails are missing (grey icons), your specific custom ROM likely stripped out the system media indexer.
* If the file picker crashes or is completely missing, your custom ROM is missing core Android components. Try installing the official **Files by Google** app from the Play Store, or ensure you have properly flashed GApps.

**Q: Why doesn't the app support GIFs or other image sequences?**

**A:** Because a GIF is not really a video... it is an image sequence.

* **The Battery Killer:** Playing a GIF forces your phone's CPU to manually decode and draw every single frame continuously. Apps that allow this cause massive, silent battery drain.
* **The Video Advantage:** Actual video files (`.mp4`, `.mkv`) use native hardware acceleration. Your phone has a dedicated silicon chip just for decoding video, which is incredibly efficient and uses almost zero extra battery (perfect for handheld gaming devices and power users!).
* **The Fix:** If you have a GIF you love, use a free online converter to change it into an `.mp4` file first. Your battery will thank you.

**Q: When I open my app drawer, the background blurs a static image of Zombillie instead of my video. Why?**

**A:** Your phone’s manufacturer is taking a lazy shortcut to save battery.

* **The Technical Reason:** Blurring live video in real-time requires constant GPU power. Aggressive custom interfaces (like Infinix's XOS or certain Xiaomi builds) refuse to do this. Instead, their launcher asks the OS for the app's default, hardcoded static thumbnail—our mascot, Zombillie—and just blurs that image instead.
* **The Fix:** Because Android requires that fallback thumbnail to be permanently baked into the app's installation file, third-party apps cannot dynamically change it to match your video. To get a true, live video blur, you either have to switch to a well-behaved custom launcher (like **Nova** or **Smart Launcher**) or just embrace your new zombie app drawer companion!