package com.miru.sdk.sample.presentation

/**
 * Navigation route definitions for the sample app.
 */
object SampleRoutes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val BOOKMARKS = "bookmarks"
    const val SETTINGS = "settings"
    const val DETAIL = "article/{articleId}"

    fun detail(articleId: Long) = "article/$articleId"
}
