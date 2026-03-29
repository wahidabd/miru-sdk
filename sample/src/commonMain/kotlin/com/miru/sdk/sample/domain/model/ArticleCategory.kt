package com.miru.sdk.sample.domain.model

/**
 * Available article categories for filtering.
 */
enum class ArticleCategory(val displayName: String, val apiValue: String) {
    GENERAL("General", "general"),
    TECHNOLOGY("Technology", "technology"),
    BUSINESS("Business", "business"),
    SCIENCE("Science", "science"),
    HEALTH("Health", "health"),
    SPORTS("Sports", "sports"),
    ENTERTAINMENT("Entertainment", "entertainment")
}
