# 簡單一鍵鎖屏

[![Android](https://img.shields.io/badge/Android-9%2B-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Release](https://img.shields.io/github/v/release/mark216tw/simple-lock-screen-android?include_prereleases)](https://github.com/mark216tw/simple-lock-screen-android/releases)
[![Status](https://img.shields.io/badge/status-prerelease-orange)](https://github.com/mark216tw/simple-lock-screen-android/releases)

「簡單一鍵鎖屏」是一款專注於一鍵鎖定 Android 螢幕的開源工具。完成一次設定後，點擊桌面 App 圖示即可立即鎖屏。

## 功能

- 點擊桌面 App 圖示立即鎖屏
- 從 Quick Settings 快速設定面板鎖屏
- 長按 App 圖示使用「立即鎖屏」或「設定」捷徑
- 提供權限狀態、鎖屏測試與設定引導
- 支援淺色與深色模式
- 支援繁體中文與英文
- 不使用網路、不顯示廣告、不收集資料

## 系統需求

- Android 9（API 28）以上
- 必須啟用「簡單一鍵鎖屏」無障礙服務

鎖屏功能使用 Android 官方的 `AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN`。無障礙服務不會訂閱操作事件、讀取視窗內容或收集使用者資料。

## 安裝

1. 前往 [Releases](https://github.com/mark216tw/simple-lock-screen-android/releases) 下載最新 APK。
2. 允許瀏覽器或檔案管理員安裝未知來源應用程式。
3. 安裝並開啟「簡單一鍵鎖屏」。
4. 按下「啟用一鍵鎖屏」，在系統設定中啟用服務。

> [!WARNING]
> 目前 GitHub Release 提供的是 Debug 簽章 APK，僅供功能測試與意見回饋，不是正式商店發行版本。未來改用正式簽章時，可能需要先移除 Debug 版本才能安裝。

## 使用方式

- 一般點擊 App 圖示：立即鎖屏。
- 長按 App 圖示：選擇立即鎖屏或開啟設定。
- 快速設定：在 App 設定頁選擇「加入快速設定按鈕」。
- 回到設定頁：長按 App 圖示並選擇「設定」。

## 權限與隱私

本專案不宣告網路權限。唯一核心權限是由使用者在系統設定中主動啟用的無障礙服務，且只用來執行鎖定螢幕動作。

完整內容請參閱[隱私權政策](PRIVACY_POLICY.md)。

## 本機建置

需求：

- JDK 17
- Android SDK Platform 36
- Android SDK Build Tools

Windows：

```powershell
.\gradlew.bat assembleDebug
```

macOS 或 Linux：

```bash
./gradlew assembleDebug
```

產出的 APK 位於：

```text
app/build/outputs/apk/debug/app-debug.apk
```

建置啟用 R8 壓縮並使用 Debug 金鑰簽署的測試發行版本：

```powershell
.\gradlew.bat assemblePrerelease
```

產出的 APK 位於：

```text
app/build/outputs/apk/prerelease/app-prerelease.apk
```

執行靜態檢查：

```powershell
.\gradlew.bat lintDebug
```

## 專案技術

- Kotlin
- Android 原生 View
- `AccessibilityService`
- `TileService`
- Android Static App Shortcuts

專案不使用第三方執行階段函式庫。

## 授權

本專案採用 [MIT License](LICENSE) 授權。
