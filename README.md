# TeamBuild

TeamBuild is a chat-first AI team workspace. This repository contains a native
Android app (`androidApp/`) and a Next.js web client (`web/`).

## Android

Open the repository in Android Studio or build a debug APK with:

```sh
gradle :androidApp:assembleDebug
```

The APK is written to
`androidApp/build/outputs/apk/debug/androidApp-debug.apk`.
The GitHub Actions workflow builds this APK and publishes it as the
`teambuild-debug-apk` artifact.

## Web

```sh
cd web
npm install
npm run dev
```

The current clients provide an interactive UI prototype for chat, agent
progress, approvals, settings, and provider setup. Authentication, model
requests, agent execution, and secure provider-key storage require a backend;
the prototype does not send or persist API keys.