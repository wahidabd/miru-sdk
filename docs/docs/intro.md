---
sidebar_position: 1
slug: /
title: Introduction
---

# Miru SDK

**Kotlin Multiplatform SDK for accelerating mobile development.**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.wahidabd/miru-sdk)](https://central.sonatype.com/artifact/io.github.wahidabd/miru-sdk)

Miru SDK provides a modular, configurable foundation for building Android and iOS apps with shared business logic and UI components. Designed as an internal base project for software houses, it handles networking, state management, navigation, theming, and dependency injection out of the box — so your team can focus on features, not boilerplate.

Each module follows **Clean Architecture** principles with clear separation into data, domain, and presentation layers.

## Why Miru SDK?

Building a new mobile project often means spending the first weeks wiring up the same infrastructure: HTTP client, error handling, ViewModel base classes, navigation, theming, DI, local storage. Miru SDK extracts all of that into reusable, tested modules that work across Android and iOS.

- **One dependency** — the `:miru-sdk` umbrella module re-exports everything via `api()`, so your app's `build.gradle.kts` stays clean.
- **Pick and choose** — need only networking and state management? Import `:network` and `:ui-state` individually.
- **Clean Architecture baked in** — domain layer has zero dependencies; data implements domain contracts; presentation consumes domain use cases.
- **Five ViewModel helpers** — from zero-boilerplate `collectResource()` to full state-reducer `execute()`, pick the right level of control for each screen.

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Kotlin | 2.3.0 | Language |
| Compose Multiplatform | 1.10.0 | Shared UI |
| Ktor | 3.4.1 | HTTP Client |
| Koin | 4.1.0 | Dependency Injection |
| Kotlinx Serialization | 1.7.3 | JSON parsing |
| Kotlinx Coroutines | 1.9.0 | Async programming |
| Firebase KMP (GitLive) | 2.1.0 | Remote Config, FCM |
| KMPAuth | 2.5.0-alpha01 | OAuth (Google, Apple, Facebook) |
| Room KMP | 2.8.4 | Local database |
| DataStore KMP | 1.2.1 | Preferences storage |
| Coil | 3.0.4 | Image loading |
| AGP | 9.0.0 | Android build |

## Module Overview

| Module | Description |
|---|---|
| [`:miru-sdk`](/docs/getting-started/installation) | Umbrella module — single dependency that re-exports all modules |
| [`:core`](/docs/modules/core) | Base utilities — `Resource<T>`, `AppException`, extensions, logging |
| [`:network`](/docs/modules/network) | HTTP client — `ApiService`, `safeApiCall`, token management |
| [`:ui-state`](/docs/modules/ui-state) | State management — `BaseViewModel`, `UiState`, EventFlow, pagination |
| [`:ui-components`](/docs/modules/ui-components) | UI library — Buttons, TextFields, Dialogs, Theming, MiruResourceView |
| [`:navigation`](/docs/modules/navigation) | Navigation — safe navigation, transitions, result passing |
| [`:firebase`](/docs/modules/firebase) | Firebase KMP — Remote Config, FCM topic management |
| [`:auth`](/docs/modules/auth) | Social Auth — Google, Apple, Facebook OAuth |
| [`:persistent`](/docs/modules/persistent) | Local storage — Room KMP + DataStore preferences |
| [`:di`](/docs/modules/di) | DI — `MiruSdkInitializer`, Koin modules |
