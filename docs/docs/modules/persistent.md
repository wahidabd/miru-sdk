---
sidebar_position: 9
title: Persistent
---

# Persistent Module

Local storage through Room KMP (SQLite database) and DataStore (key-value preferences).

```kotlin
implementation("io.github.wahidabd:miru-sdk-persistent:<version>")
```

## Setup

Initialize in your `Application.onCreate()` (Android):

```kotlin
MiruPreferencesInitializer.init(applicationContext)
MiruDatabaseInitializer.init(applicationContext)
```

## DataStore Preferences

`MiruPreferences` wraps DataStore with a convenient API:

```kotlin
val prefs: MiruPreferences = get() // via Koin

// Write
prefs.putString("user_name", "Wahid")
prefs.putBoolean("dark_mode", true)
prefs.putInt("login_count", 5)

// Read (one-shot suspend)
val name = prefs.getString("user_name", "Guest")

// Read (reactive Flow)
prefs.observeBoolean("dark_mode", false).collect { isDark ->
    // react to changes
}

// Remove / Clear
prefs.remove("user_name")
prefs.clear()
```

### Supported Types

| Method | Type |
|---|---|
| `putString` / `getString` | `String` |
| `putInt` / `getInt` | `Int` |
| `putLong` / `getLong` | `Long` |
| `putFloat` / `getFloat` | `Float` |
| `putDouble` / `getDouble` | `Double` |
| `putBoolean` / `getBoolean` | `Boolean` |

Each type also has an `observe*` variant returning `Flow<T>`.

## Room KMP Database

### Define Entities & DAO

```kotlin
// commonMain
@Entity
data class UserEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val email: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE id = :id")
    suspend fun getById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)
}
```

### Define Database

```kotlin
@Database(entities = [UserEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
```

### Build Per Platform

```kotlin
// Android
val db = Room.databaseBuilder<AppDatabase>(context, databasePath("app.db"))
    .miruBuild()

// iOS
val db = Room.databaseBuilder<AppDatabase>(databasePath("app.db"))
    .miruBuild()
```

`miruBuild()` is an extension that pre-configures `BundledSQLiteDriver` and `Dispatchers.IO`.

:::tip
If you see expect/actual class warnings during compilation, add `-Xexpect-actual-classes` to your compiler options:

```kotlin
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
```
:::

## Components

| Component | Description |
|---|---|
| `MiruPreferences` | DataStore wrapper with type-safe get/put/observe |
| `MiruDatabase` | Room builder helper with pre-configured driver |
| `databasePath()` | Platform-specific database file path resolver |
| `persistentModule` | Koin module providing MiruPreferences |
