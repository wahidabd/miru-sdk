package com.miru.sdk.core.ext

/**
 * Returns the string if not null, otherwise an empty string.
 */
fun String?.orEmpty(): String = this ?: ""

/**
 * Checks if the string is a valid email address.
 *
 * Uses a basic regex pattern for email validation.
 */
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return this.matches(emailRegex)
}

/**
 * Checks if the string is a valid phone number.
 *
 * Accepts strings containing only digits, spaces, hyphens, and plus signs.
 * Minimum 10 digits required.
 */
fun String.isValidPhone(): Boolean {
    val phoneRegex = "^[+]?[0-9\\s-()]{10,}$".toRegex()
    return this.matches(phoneRegex) && this.filter { it.isDigit() }.length >= 10
}

/**
 * Checks if the string is a valid URL.
 *
 * Validates common URL patterns.
 */
fun String.isValidUrl(): Boolean {
    val urlRegex = "^(https?://)?([\\w-]+\\.)+[\\w-]{2,}(/.*)?$".toRegex()
    return this.matches(urlRegex)
}

/**
 * Capitalizes the first character of the string.
 *
 * @return the string with the first character capitalized
 */
fun String.capitalizeFirst(): String {
    return if (this.isEmpty()) this else this[0].uppercaseChar() + this.substring(1)
}

/**
 * Converts the string to a URL-friendly slug.
 *
 * Removes special characters, converts to lowercase, and replaces spaces with hyphens.
 */
fun String.toSlug(): String {
    return this
        .lowercase()
        .replace(Regex("[^a-z0-9]+"), "-")
        .trim('-')
}

/**
 * Truncates the string to a maximum length and appends ellipsis if truncated.
 *
 * @param maxLength the maximum length of the result including ellipsis
 * @return the truncated string
 */
fun String.truncate(maxLength: Int): String {
    return if (this.length <= maxLength) {
        this
    } else {
        this.take(maxLength - 3) + "..."
    }
}

/**
 * Removes all whitespace from the string.
 *
 * @return the string without whitespace
 */
fun String.removeWhitespace(): String {
    return this.replace(Regex("\\s+"), "")
}
