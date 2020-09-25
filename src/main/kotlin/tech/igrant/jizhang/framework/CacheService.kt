package tech.igrant.jizhang.framework

interface CacheService<T, ID> {
    fun save(t: T): T
    fun get(id: ID): T
    fun list(): List<T>
}