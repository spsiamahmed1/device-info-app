# Device Info — ফোনের ডাক্তার

**Device Info - ফোনের ডাক্তার - All In One Android App - Java** is an offline-first Bengali Android device-information and diagnostics app. Pure Java, Android Views/XML, min SDK 24, target SDK 34, Java 17, ViewBinding enabled.

## Build

- JDK 17
- Android SDK Platform 35
- Included Gradle 8.7 wrapper
- Run `./gradlew assembleDebug`

GitHub Actions builds a debug APK on push and manual dispatch. Download the `Device-Info-App` artifact from a successful workflow run.

## Included features

- Phone manufacturer/model, Android release/API, CPU ABI, RAM, internal storage, display metrics, available sensor count, and visible launchable app list.
- Current battery percentage, temperature, health, and charging status from Android system APIs; 40°C foreground-screen notification when notification permission is granted.
- Color display test, vibration test, speaker test tone, camera-app launch, sensor inventory, and opt-in microphone sample-level test.
- Current Wi-Fi SSID (when Android exposes it), signal strength, link speed, and frequency, plus system Wi-Fi settings shortcut.
- Internal storage totals and system settings shortcuts; app-specific system App Info screens.
- Local text report, copy/share, PDF creation through Android's document picker, and a home-screen battery/RAM widget.
- Room database foundation and MPAndroidChart dependency included for local-history/chart expansion.
- Bundled Hind Siliguri typeface (OFL-licensed); the app does not request `INTERNET` permission.

## Platform limitations (not faked)

Android does not allow ordinary third-party apps to silently clear other apps' cache, force-free RAM, cool the CPU, toggle permissions for other apps, or reliably retrieve their private battery drain history. Wi-Fi scans are permission-, location-, and OS-throttled. SIM identifiers are restricted. There is no general public API for a fake-charger verdict or exact remaining battery-time forecast. A persistent background thermal monitor requires a user-approved foreground service and notification permission; this project reports overheat while the battery screen is open. A network speed test requires internet traffic and therefore is not included in this offline build. Installed-app visibility is restricted by Android and this project lists launchable apps rather than requesting broad package access.

Microphone access is requested only when that user-started test is selected. Reports are shared only when the user chooses a share action; no data is uploaded by the app itself.
