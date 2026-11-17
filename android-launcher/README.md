# Liquid Launcher (Android)

Eigenständiger Android-Home-Screen-Launcher im Liquid-Glass-Stil (Kotlin, Jetpack Compose).
Keine Apple-Assets, keine Root-Rechte.

## Stand: Phase 1 (Architektur und Grundfunktion)

- Gradle-Projekt mit Kotlin 2.0.21, AGP 8.7.3, Compose BOM 2024.12.01, minSdk 31 (Android 12, wegen Blur)
- Registrierung als Home-App (`HOME`-Intent-Filter), damit er als Standard-Launcher wählbar ist
- Liste aller startbaren Apps über `PackageManager` (alphabetisch) mit Icons in abgerundeten Kacheln
- Tippen startet die App
- Designtokens zentral in `theme/LiquidColors.kt`

Noch nicht enthalten (kommen in den nächsten Phasen): Dock, Seitenwechsel, Ordner, Gesten,
App-Bibliothek, Control Center, Benachrichtigungen, Widgets, Einstellungen.

## Bauen

Benötigt JDK 21 und das Android SDK (Plattform 35, Build-Tools 35.0.0). Pfad in `local.properties`:

```
sdk.dir=/pfad/zum/android-sdk
```

```bash
./gradlew :app:assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

Als Standard-Launcher wählen: Einstellungen → Apps → Standard-Apps → Startbildschirm-App.
