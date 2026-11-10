# Device Info — ফোনের ডাক্তার

**Device Info - ফোনের ডাক্তার - All In One Android App - Java** is an offline-first Bengali Android device-information and diagnostics app. It is pure Java with Android Views/XML, min SDK 24, target SDK 34, Java 17, and ViewBinding enabled.

## Build

Use JDK 17, Android SDK Platform 35, and the included Gradle 8.7 wrapper. Run `./gradlew assembleDebug`. GitHub Actions builds a debug APK on push and manual dispatch; download the `Device-Info-App` artifact from a successful workflow run.

## Included in this build

This build reads phone manufacturer/model, Android release/API, CPU ABI, RAM, internal storage, display metrics, and available sensor count. It shows the current battery percentage, temperature, health, and charging status; the battery screen can issue a 40°C notification while open if notifications are allowed. The diagnostics screen includes a color display test, vibration test, speaker test tone, camera-app launch, sensor inventory, and an opt-in microphone sample-level test. The network page shows available current Wi-Fi details and a system settings shortcut. Other included functions are a visible/launchable app list, system App Info shortcuts, storage totals, local text report/copy/share, PDF creation using Android's document picker, and a home-screen battery/RAM widget. Room database scaffolding and MPAndroidChart are included for future local history/chart features. Hind Siliguri is bundled under its SIL Open Font License. The app does not request `INTERNET` permission.

## Not in this APK yet

This first build does not include charging-history or per-app battery-drain charts, exact remaining-time prediction, touch-grid or left/right speaker tests, fingerprint enrollment diagnostics, Wi-Fi scanning/history, internet speed tests, data-usage alerts, detailed SIM information, duplicate-photo detection, phone-to-phone comparison, onboarding screens, a full dark theme, or chart screens. It also does not perform junk/cache cleaning, force-free RAM, CPU cooling, or silent permission toggles. These functions are omitted rather than represented by fake readings or inactive controls.

## Android and privacy limits

Ordinary Android apps cannot silently clear other apps' private cache, force-free their RAM, cool their CPU, or toggle their permissions. Access to other apps' battery history and broad installed-app lists is restricted. Wi-Fi scans are permission-, location-, and OS-throttled; SIM identifiers are restricted. There is no general public API for a fake-charger verdict or exact remaining battery-time forecast. A persistent background thermal monitor needs a user-approved foreground service and notification permission; this build reports an overheat while the battery screen is open. A network speed test uses internet traffic and is intentionally absent from this offline build.

Microphone permission is requested only after the user starts that test. Reports leave the device only if the user explicitly chooses a share action; the app itself does not upload data.
