package com.icerockdev.domain

import java.util.stream.Stream
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification


interface StreamableJpaSpecificationRepository<ENTITY> {
    /**
     * Use this function only with try-with-resource (kotlin.use function)
     */
    fun stream(specification: Specification<ENTITY>? = null, sort: Sort? = null, clazz: Class<ENTITY>): Stream<ENTITY>
}
