package org.maocide.undeadwallpaper.model

/**
 * Controls how the live wallpaper influences the system accent / Material You theme color
 * via the WallpaperColors it reports to the OS.
 */
enum class AccentColorMode {
    AUTO,    // Accent follows the colors extracted from the active video (default)
    OFF,     // Wallpaper does not tint the system accent
    CUSTOM   // Accent follows a user-picked color
}
