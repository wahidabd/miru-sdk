package com.miru.sdk.firebase.config

import com.miru.sdk.core.AppException
import com.miru.sdk.core.Resource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig
import dev.gitlive.firebase.remoteconfig.get
import dev.gitlive.firebase.remoteconfig.remoteConfig
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Miru Remote Config — a convenient wrapper around Firebase Remote Config for KMP.
 *
 * Usage:
 * ```
 * val config = MiruRemoteConfig()
 *
 * // Fetch & activate
 * config.fetchAndActivate().collect { resource ->
 *     resource.onSuccess { /* ready */ }
 * }
 *
 * // Get values
 * val apiUrl = config.getString("api_base_url", "https://default.api.com")
 * val featureOn = config.getBoolean("feature_flag_x", false)
 * ```
 */
class MiruRemoteConfig(
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig,
) {

    /**
     * Fetch remote config from server and activate it.
     * Returns [Resource.Success] with `true` if new values were activated.
     */
    fun fetchAndActivate(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val activated = remoteConfig.fetchAndActivate()
            Napier.d("RemoteConfig fetchAndActivate: activated=$activated")
            emit(Resource.Success(activated))
        } catch (e: Exception) {
            Napier.e("RemoteConfig fetchAndActivate failed", e)
            emit(Resource.Error(exception = AppException.UnknownException(throwable = e)))
        }
    }

    /**
     * Set default values for Remote Config.
     * Use this before fetching to ensure fallback values are available.
     */
    suspend fun setDefaults(defaults: Map<String, Any>) {
        try {
            remoteConfig.setDefaults(*defaults.map { (k, v) ->
                when (v) {
                    is String -> k to v
                    is Boolean -> k to v
                    is Long -> k to v
                    is Double -> k to v
                    is Int -> k to v.toLong()
                    else -> k to v.toString()
                }
            }.toTypedArray())
            Napier.d("RemoteConfig defaults set: ${defaults.keys}")
        } catch (e: Exception) {
            Napier.e("RemoteConfig setDefaults failed", e)
        }
    }

    /**
     * Get a string value from Remote Config.
     */
    fun getString(key: String, default: String = ""): String {
        return try {
            remoteConfig[key]
        } catch (e: Exception) {
            Napier.w("RemoteConfig getString($key) failed, using default", e)
            default
        }
    }

    /**
     * Get a boolean value from Remote Config.
     */
    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return try {
            remoteConfig[key]
        } catch (e: Exception) {
            Napier.w("RemoteConfig getBoolean($key) failed, using default", e)
            default
        }
    }

    /**
     * Get a long value from Remote Config.
     */
    fun getLong(key: String, default: Long = 0L): Long {
        return try {
            remoteConfig[key]
        } catch (e: Exception) {
            Napier.w("RemoteConfig getLong($key) failed, using default", e)
            default
        }
    }

    /**
     * Get a double value from Remote Config.
     */
    fun getDouble(key: String, default: Double = 0.0): Double {
        return try {
            remoteConfig[key]
        } catch (e: Exception) {
            Napier.w("RemoteConfig getDouble($key) failed, using default", e)
            default
        }
    }

    /**
     * Get all Remote Config values as a Map.
     */
    fun getAll(): Map<String, String> {
        return try {
            remoteConfig.all.mapValues { it.value.asString() }
        } catch (e: Exception) {
            Napier.e("RemoteConfig getAll failed", e)
            emptyMap()
        }
    }
}
