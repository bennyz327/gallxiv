# Project Gallxiv

[English](README.md) | [中文](README_zh.md)

## 概覽

Gallxiv 是一個完整的全端網頁應用程式，使用 Spring Boot 作為後端和 Vue 3 作為前端開發。專案設計用於處理使用者身份驗證、文章管理和媒體處理等功能。專案採用模組化設計，並透過容器化技術便於部署。

---

## 功能特點

### 後端功能

- **使用者身份驗證**：基於 JWT 的安全登錄機制。
- **文章管理**：提供創建、查詢和管理文章的 API。
- **媒體處理**：支援圖片的上傳與檢索 API。
- **Spring Boot 配置**：透過 `application.yml` 進行靈活的配置管理。

### 前端功能

- **Vue 3 框架**：基於 Composition API 的現代前端開發。
- **狀態管理**：使用 Pinia 進行應用程式狀態管理。
- **API 整合**：自動生成 API hooks，無縫連結後端。
- **響應式設計**：支援行動裝置友好的版面配置。

---

## 專案結構

### 後端

```
src/main/kotlin/com/benjk/gallexiv
├── config          # 安全性與應用程式配置
├── controller      # 提供 REST API 的控制器
├── data
│   ├── dto         # 資料傳輸物件
│   ├── entity      # 資料庫實體
├── repository      # 資料庫操作層
├── service         # 商業邏輯服務層
```

### 前端

```
frontend
├── public          # 靜態資源 (如 favicon)
├── src
│   ├── api         # API hooks 和 schemas
│   ├── components  # 可重複使用的 Vue 組件
│   ├── plugin      # Toastify 和 Vuetify 插件
│   ├── router      # Vue Router 配置
│   ├── stores      # Pinia 狀態管理
│   ├── views       # 應用程式視圖
```

### 關鍵文件

- **後端**
  - `application.yml`：Spring Boot 的主要配置文件。
  - `build.gradle.kts`：Gradle 構建腳本。
- **前端**
  - `vite.config.ts`：Vite 配置文件。
  - `package.json`：Node.js 依賴與腳本。

---

## 安裝步驟

### 必備條件

- **Java 17**
- **Node.js 16+**
- **Docker**

### 後端安裝

1. 進入後端目錄：
   ```bash
   cd gallxiv-main
   ```
2. 使用 Gradle 構建專案：
   ```bash
   ./gradlew build
   ```
3. 啟動應用程式：
   ```bash
   java -jar build/libs/gallexiv.jar
   ```

### 前端安裝

1. 進入前端目錄：
   ```bash
   cd frontend
   ```
2. 安裝依賴：
   ```bash
   npm install
   ```
3. 啟動開發伺服器：
   ```bash
   npm run dev
   ```

### Docker 安裝

1. 啟動資料庫容器：
   ```bash
   docker-compose -f docker-compose-database.yml up
   ```

---
