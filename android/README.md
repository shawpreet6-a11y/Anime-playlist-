# Android APK wrapper for Anime Playlist

This project wraps the existing HTML app inside an Android WebView so it can be packaged as an APK.

## Open in Android Studio

1. Open Android Studio.
2. Choose `Open` and select the `android` folder.
3. Let Gradle sync complete.
4. Select `Build > Build Bundle(s) / APK(s) > Build APK`.

## Important note

The app currently loads the live HTML from:
`https://raw.githubusercontent.com/shawpreet6-a11y/Anime-playlist-/main/index.html`

This lets the APK work immediately without copying the web app into a local asset folder.

## If your GitHub repo changes

Update the URL in:
`android/app/src/main/java/com/animeplaylist/MainActivity.kt`
