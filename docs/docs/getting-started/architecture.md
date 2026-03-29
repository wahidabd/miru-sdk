---
sidebar_position: 3
title: Architecture
---

# Architecture

## Clean Architecture Per Module

Each module follows Clean Architecture with three layers:

```
┌──────────────────────────────────────────────┐
│              Presentation Layer               │
│  (UI Composables, ViewModels, UiState)        │
│                     │                         │
│                     ▼                         │
│               Domain Layer                    │
│  (Use Cases, Repository Interfaces, Models)   │
│                     │                         │
│                     ▼                         │
│                Data Layer                     │
│  (Repository Impl, API, DAO, DataSource)      │
└──────────────────────────────────────────────┘
```

The dependency rule flows **inward only**: `presentation → domain ← data`. Domain never depends on data or presentation.

- **Domain** is the innermost layer — defines interfaces (repository contracts) and business models with zero external dependencies.
- **Data** implements domain interfaces with concrete data sources (API, Room, DataStore).
- **Presentation** consumes domain use cases and exposes UI state.

## Module Dependency Graph

```mermaid
graph TD
    APP[Your App] --> SDK[":miru-sdk"]

    SDK --> DI[":di"]
    SDK --> UI[":ui-components"]
    SDK --> STATE[":ui-state"]
    SDK --> NAV[":navigation"]
    SDK --> FB[":firebase"]
    SDK --> AUTH[":auth"]
    SDK --> PERSIST[":persistent"]

    DI --> NETWORK[":network"]
    DI --> CORE[":core"]
    AUTH --> CORE
    PERSIST --> CORE
    FB --> CORE
    UI --> CORE
    STATE --> CORE
    NAV --> CORE
    NETWORK --> CORE

    style APP fill:#4CAF50,color:#fff,stroke:none,rx:8
    style SDK fill:#00BCD4,color:#fff,stroke:none,rx:8
    style DI fill:#FF9800,color:#fff,stroke:none,rx:8
    style AUTH fill:#E91E63,color:#fff,stroke:none,rx:8
    style FB fill:#FF5722,color:#fff,stroke:none,rx:8
    style UI fill:#2196F3,color:#fff,stroke:none,rx:8
    style STATE fill:#2196F3,color:#fff,stroke:none,rx:8
    style NAV fill:#2196F3,color:#fff,stroke:none,rx:8
    style PERSIST fill:#795548,color:#fff,stroke:none,rx:8
    style NETWORK fill:#9C27B0,color:#fff,stroke:none,rx:8
    style CORE fill:#607D8B,color:#fff,stroke:none,rx:8
```

The `:miru-sdk` umbrella module uses `api()` dependencies so everything is transitively available to your app with a single import.

## Data Flow

```mermaid
sequenceDiagram
    participant UI as Composable
    participant VM as ViewModel
    participant UC as UseCase
    participant REPO as Repository Interface
    participant IMPL as RepositoryImpl
    participant API as ApiService / DAO

    UI->>VM: User action
    VM->>UC: execute()
    UC->>REPO: getUsers()
    REPO->>IMPL: (injected impl)
    IMPL->>API: Ktor request / Room query
    API-->>IMPL: Response / Exception
    IMPL-->>IMPL: Map DTO → Domain Model
    IMPL-->>UC: Resource<DomainModel>
    UC-->>VM: Resource<DomainModel>
    VM->>VM: setState { ... }
    VM-->>UI: StateFlow emission
    UI->>UI: Recompose
```

## Internal Module Structure

Each feature module follows this folder pattern:

```
:feature-module/
└── src/commonMain/kotlin/
    └── com/miru/sdk/feature/
        ├── data/                  # Data Layer
        │   ├── repository/        #   Repository implementations
        │   ├── source/            #   Remote/Local data sources
        │   ├── model/             #   DTOs, entities, API models
        │   └── mapper/            #   Data ↔ Domain mappers
        ├── domain/                # Domain Layer
        │   ├── repository/        #   Repository interfaces (contracts)
        │   ├── model/             #   Business/domain models
        │   └── usecase/           #   Use cases (business logic)
        └── presentation/          # Presentation Layer (if applicable)
            ├── ui/                #   Composable screens/components
            └── viewmodel/         #   ViewModels + UiState
```

:::note
Not all modules have all three layers. Foundation modules like `:core` and `:network` primarily provide domain and data abstractions. UI-only modules like `:ui-components` are purely presentation.
:::

## Layer Mapping Summary

| Module | Domain | Data | Presentation |
|---|---|---|---|
| `:core` | Resource, AppException, Mapper, Extensions | Logger, DispatcherProvider | — |
| `:network` | TokenProvider, TokenEvent | ApiService, SafeApiCall, HttpClient | — |
| `:ui-state` | — | — | BaseViewModel, UiState, EventFlow |
| `:navigation` | NavigationManager, NavigationResult | — | MiruNavHost, Transitions |
| `:ui-components` | — | — | Theme, Buttons, Cards, Dialogs |
| `:auth` | AuthResult, MiruAuthManager | Google/Apple/Facebook Auth | Sign-in buttons |
| `:firebase` | RemoteConfig/Messaging interfaces | Firebase impl, TopicManager | — |
| `:persistent` | Preferences/Database interfaces | Room, DataStore | — |
| `:di` | — | Koin module wiring | — |
