package com.miru.sdk.core.ext

/**
 * Safely retrieves an element at the given index, returning null if out of bounds.
 *
 * @param index the index to retrieve
 * @return the element at the index or null if out of bounds
 */
fun <T> List<T>.safeGet(index: Int): T? {
    return if (index in 0..<size) get(index) else null
}

/**
 * Returns a new list with the element at the given index replaced.
 *
 * @param index the index of the element to replace
 * @param item the new element
 * @return a new list with the replaced element or the original list if index is invalid
 */
fun <T> List<T>.replace(index: Int, item: T): List<T> {
    return if (index in 0..<size) {
        toMutableList().apply { set(index, item) }
    } else {
        this
    }
}

/**
 * Returns a new list with elements transformed based on a predicate.
 *
 * @param predicate the condition to check
 * @param transform the transformation to apply
 * @return a new list with matching elements transformed
 */
fun <T> List<T>.updateIf(predicate: (T) -> Boolean, transform: (T) -> T): List<T> {
    return map { item ->
        if (predicate(item)) transform(item) else item
    }
}

/**
 * Returns a list of distinct elements based on a selector function.
 *
 * @param selector the function to select the identity of each element
 * @return a list of elements distinct by the selector
 */
fun <T, K> List<T>.distinctById(selector: (T) -> K): List<T> {
    val seen = mutableSetOf<K>()
    return filter { item ->
        val id = selector(item)
        if (id in seen) false else {
            seen.add(id)
            true
        }
    }
}
