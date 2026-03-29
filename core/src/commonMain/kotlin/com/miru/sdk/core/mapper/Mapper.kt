package com.miru.sdk.core.mapper

/**
 * Base interface for mapping one type to another.
 *
 * @param From the source type
 * @param To the target type
 */
interface Mapper<in From, out To> {
    /**
     * Maps an instance from the source type to the target type.
     *
     * @param from the source instance
     * @return the mapped target instance
     */
    fun map(from: From): To
}

/**
 * Interface for bidirectional mapping between two types.
 *
 * @param From the first type
 * @param To the second type
 */
interface BidirectionalMapper<From, To> : Mapper<From, To> {
    /**
     * Maps an instance from the target type back to the source type.
     *
     * @param to the target instance
     * @return the mapped source instance
     */
    fun reverseMap(to: To): From
}

/**
 * Maps a list of items using the provided mapper.
 *
 * @param mapper the mapper to use for each item
 * @return a new list with all items mapped
 */
fun <From, To> List<From>.mapWith(mapper: Mapper<From, To>): List<To> {
    return map(mapper::map)
}
