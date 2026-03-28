# Research App

Небольшое Android-приложение для экспериментов с **списком товаров** и **разными режимами пагинации**: бесшовная подгрузка, с прогресс-баром и постраничная. Есть экран **настроек** (сохранение режима в DataStore), **карточка товара** и **детальный экран** по `id`.

Данные читаются из **`assets/products.json`** (~200 позиций, русские названия и описания, цены в рублях). В коде также есть слой под сетевой API (Ktor + DTO), но в DI подключён **`LocalProductRepository`**.

## Сборка и варианты

- **production** — `com.arekalov.researchapp`
- **dev** — `com.arekalov.researchapp.dev`, суффикс версии `-dev`, через `BuildConfig.HIGHLIGHTED_CARDS` задаются индексы подсвечиваемых карточек (для исследований UI)

```bash
./gradlew :app:assembleDebug
./gradlew :app:assembleProductionDebug   # или assembleDevDebug
```

## Стек

| Область | Технологии |
|--------|------------|
| Язык, UI | Kotlin, Jetpack Compose, Material 3 |
| Архитектура | presentation / domain / data, ViewModel, State + Intent |
| DI | Hilt |
| Навигация | Navigation Compose |
| Сериализация | Kotlinx Serialization (JSON) |
| Сеть (заготовка) | Ktor Client |
| Изображения | Glide (Compose) |
| Настройки | DataStore Preferences |
| Сборка | Gradle Kotlin DSL, Version Catalog (`libs.versions.toml`) |

`minSdk` 26, `compileSdk` / `targetSdk` 36, JVM 11.
