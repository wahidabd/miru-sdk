# Chucker HTTP Inspector Integration

**Date:** 2026-06-27
**Scope:** Android-only, debug builds. iOS skipped for now.

---

## Goal

Add Chucker as a built-in HTTP inspector in the SDK so that consumer apps automatically get request/response inspection UI on Android debug builds — with zero overhead in release builds and no changes required from the consumer beyond using the Android-specific initializer.

---

## Architecture

Two modules are affected: `network` and `di`. No new modules.

```
Consumer (Android)
    └─ MiruSdkInitializer.initialize(context, config)   ← Android extension (new)
           │
           ├─ startKoin { androidContext(context) }
           │
           ├─ platformModule()  → provides ChuckerInterceptor via androidContext()
           │
           └─ networkModule()   → gets interceptors from Koin → HttpClientFactory
                                        └─ createHttpEngine(interceptors)
                                                  └─ OkHttp.create { addInterceptor(...) }

Consumer (iOS) — unchanged
    └─ MiruSdkInitializer.initialize(config)
           └─ platformModule() → provides emptyList()
           └─ createHttpEngine(interceptors) → Darwin.create() ignores interceptors
```

---

## Components

### `network` module

| File | Change |
|------|--------|
| `HttpEngineFactory.kt` (commonMain) | Add `interceptors: List<Any> = emptyList()` param to expect function |
| `HttpEngineFactory.android.kt` | Cast interceptors to `okhttp3.Interceptor`, inject via `OkHttp.create { config { addInterceptor(...) } }` |
| `HttpEngineFactory.ios.kt` | Accept param, ignore it, return `Darwin.create()` unchanged |
| `HttpClientFactory.kt` | Add `interceptors: List<Any> = emptyList()` param, forward to `createHttpEngine()` |

### `di` module

| File | Change |
|------|--------|
| `build.gradle.kts` | Add `debugImplementation` chucker:library + `releaseImplementation` chucker:library-no-op |
| `PlatformModule.android.kt` | Provide `List<Any>` singleton with `named("networkInterceptors")` — builds `ChuckerInterceptor` using `androidContext()` |
| `PlatformModule.ios.kt` | Provide `emptyList<Any>()` with same qualifier |
| `NetworkModule.kt` | Use `getOrNull<List<Any>>(named("networkInterceptors"))` and pass to `HttpClientFactory.create()` |
| `MiruSdkInitializerAndroid.kt` *(new, androidMain)* | Extension fun `MiruSdkInitializer.initialize(context: Context, config: MiruSdkConfig)` that calls `startKoin { androidContext(context) ... }` |

### Dependency versions

```kotlin
// di/build.gradle.kts — outside sourceSets block
dependencies {
    "debugImplementation"("com.github.chuckerteam.chucker:library:4.0.0")
    "releaseImplementation"("com.github.chuckerteam.chucker:library-no-op:4.0.0")
}
```

---

## Data Flow

```
1. Koin resolve HttpClient singleton (lazy, on first use)
2. NetworkModule: getOrNull<List<Any>>(named("networkInterceptors"))
   ├─ Android debug:   [ChuckerInterceptor]        from PlatformModule
   ├─ Android release: [ChuckerInterceptor no-op]  from PlatformModule
   └─ iOS:             []                           from PlatformModule
3. HttpClientFactory.create(config, tokenProvider, interceptors)
4. createHttpEngine(interceptors)
   ├─ Android: OkHttp.create { addInterceptor(each) }
   └─ iOS:     Darwin.create()
5. HttpClient ready
```

---

## Consumer Usage

**Android (Application class):**
```kotlin
MiruSdkInitializer.initialize(applicationContext, config)
```

**iOS (unchanged):**
```kotlin
MiruSdkInitializer.initialize(config)
```

---

## Error Handling

- Consumer calls `initialize(config)` without context on Android → `KoinIllegalStateException` from Koin with a clear message. Fail-fast, easy to diagnose.
- `getOrNull` (not `get`) in `NetworkModule` → no crash if `platformModule` is missing for any reason; interceptors just default to empty.
- Release no-op: zero UI, zero overhead, same API. No conditional logic needed in SDK code.

---

## Out of Scope

- iOS HTTP inspection (deferred — no direct Chucker equivalent in KMP)
- Configurable Chucker options (max content length, redact headers, etc.) — can be added later by extending `PlatformModule.android.kt`
