package com.miru.sdk.firebase.messaging

import com.miru.sdk.core.Resource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Topic Manager — tracks which FCM topics the user is currently subscribed to.
 *
 * Works alongside [MiruMessaging] to provide a reactive state of active subscriptions.
 *
 * Usage:
 * ```
 * val topicManager = TopicManager(messaging)
 *
 * // Subscribe and track
 * topicManager.subscribe("promo")
 *
 * // Check subscribed topics
 * topicManager.subscribedTopics.collect { topics ->
 *     println("Active topics: $topics")
 * }
 *
 * // Check if subscribed
 * val isSubscribed = topicManager.isSubscribed("promo")
 * ```
 */
class TopicManager(
    private val messaging: MiruMessaging,
) {
    private val _subscribedTopics = MutableStateFlow<Set<String>>(emptySet())

    /**
     * Reactive state of currently subscribed topics.
     */
    val subscribedTopics: StateFlow<Set<String>> = _subscribedTopics.asStateFlow()

    /**
     * Subscribe to a topic and track it.
     */
    suspend fun subscribe(topic: String) {
        messaging.subscribeToTopic(topic).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _subscribedTopics.value += topic
                    Napier.d("TopicManager: added '$topic' — active: ${_subscribedTopics.value}")
                }
                else -> { /* Loading or Error — no-op */ }
            }
        }
    }

    /**
     * Unsubscribe from a topic and remove tracking.
     */
    suspend fun unsubscribe(topic: String) {
        messaging.unsubscribeFromTopic(topic).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _subscribedTopics.value -= topic
                    Napier.d("TopicManager: removed '$topic' — active: ${_subscribedTopics.value}")
                }
                else -> { /* Loading or Error — no-op */ }
            }
        }
    }

    /**
     * Subscribe to multiple topics and track them.
     */
    suspend fun subscribeAll(topics: List<String>) {
        messaging.subscribeToTopics(topics).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val succeeded = resource.data.filter { it.value }.keys
                    _subscribedTopics.value += succeeded
                    Napier.d("TopicManager: subscribed to ${succeeded.size}/${topics.size} topics")
                }
                else -> { /* Loading or Error — no-op */ }
            }
        }
    }

    /**
     * Unsubscribe from multiple topics and remove tracking.
     */
    suspend fun unsubscribeAll(topics: List<String>) {
        messaging.unsubscribeFromTopics(topics).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val succeeded = resource.data.filter { it.value }.keys
                    _subscribedTopics.value -= succeeded
                    Napier.d("TopicManager: unsubscribed from ${succeeded.size}/${topics.size} topics")
                }
                else -> { /* Loading or Error — no-op */ }
            }
        }
    }

    /**
     * Check if currently subscribed to a topic.
     */
    fun isSubscribed(topic: String): Boolean = topic in _subscribedTopics.value

    /**
     * Clear all tracked topics (does NOT unsubscribe from FCM).
     */
    fun clearTracking() {
        _subscribedTopics.value = emptySet()
    }
}
