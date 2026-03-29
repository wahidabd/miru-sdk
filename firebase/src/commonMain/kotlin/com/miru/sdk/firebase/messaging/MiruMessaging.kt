package com.miru.sdk.firebase.messaging

import com.miru.sdk.core.AppException
import com.miru.sdk.core.Resource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.FirebaseMessaging
import dev.gitlive.firebase.messaging.messaging
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Miru Messaging — a convenient wrapper around Firebase Cloud Messaging for KMP.
 *
 * Provides topic subscribe/unsubscribe management with built-in logging
 * and [Resource] state handling.
 *
 * Usage:
 * ```
 * val messaging = MiruMessaging()
 *
 * // Subscribe to a topic
 * messaging.subscribeToTopic("promo").collect { resource ->
 *     resource.onSuccess { println("Subscribed!") }
 * }
 *
 * // Unsubscribe from a topic
 * messaging.unsubscribeFromTopic("promo").collect { resource ->
 *     resource.onSuccess { println("Unsubscribed!") }
 * }
 *
 * // Manage multiple topics at once
 * messaging.subscribeToTopics(listOf("news", "updates", "promo")).collect { ... }
 * ```
 */
class MiruMessaging(
    private val messaging: FirebaseMessaging = Firebase.messaging,
) {

    /**
     * Subscribe to a single FCM topic.
     */
    fun subscribeToTopic(topic: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            messaging.subscribeToTopic(topic)
            Napier.d("FCM subscribed to topic: $topic")
            emit(Resource.Success(topic))
        } catch (e: Exception) {
            Napier.e("FCM subscribe to topic '$topic' failed", e)
            emit(Resource.Error(exception = AppException.UnknownException(throwable = e)))
        }
    }

    /**
     * Unsubscribe from a single FCM topic.
     */
    fun unsubscribeFromTopic(topic: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            messaging.unsubscribeFromTopic(topic)
            Napier.d("FCM unsubscribed from topic: $topic")
            emit(Resource.Success(topic))
        } catch (e: Exception) {
            Napier.e("FCM unsubscribe from topic '$topic' failed", e)
            emit(Resource.Error(exception = AppException.UnknownException(throwable = e)))
        }
    }

    /**
     * Subscribe to multiple FCM topics at once.
     * Returns a map of topic → success/failure.
     */
    fun subscribeToTopics(topics: List<String>): Flow<Resource<Map<String, Boolean>>> = flow {
        emit(Resource.Loading())
        try {
            val results = mutableMapOf<String, Boolean>()
            topics.forEach { topic ->
                try {
                    messaging.subscribeToTopic(topic)
                    results[topic] = true
                    Napier.d("FCM subscribed to topic: $topic")
                } catch (e: Exception) {
                    results[topic] = false
                    Napier.w("FCM subscribe to topic '$topic' failed", e)
                }
            }
            emit(Resource.Success(results.toMap()))
        } catch (e: Exception) {
            Napier.e("FCM subscribeToTopics failed", e)
            emit(Resource.Error(exception = AppException.UnknownException(throwable = e)))
        }
    }

    /**
     * Unsubscribe from multiple FCM topics at once.
     * Returns a map of topic → success/failure.
     */
    fun unsubscribeFromTopics(topics: List<String>): Flow<Resource<Map<String, Boolean>>> = flow {
        emit(Resource.Loading())
        try {
            val results = mutableMapOf<String, Boolean>()
            topics.forEach { topic ->
                try {
                    messaging.unsubscribeFromTopic(topic)
                    results[topic] = true
                    Napier.d("FCM unsubscribed from topic: $topic")
                } catch (e: Exception) {
                    results[topic] = false
                    Napier.w("FCM unsubscribe from topic '$topic' failed", e)
                }
            }
            emit(Resource.Success(results.toMap()))
        } catch (e: Exception) {
            Napier.e("FCM unsubscribeFromTopics failed", e)
            emit(Resource.Error(exception = AppException.UnknownException(throwable = e)))
        }
    }
}
