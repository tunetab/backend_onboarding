package com.icerockdev.domain

import org.springframework.data.jpa.domain.Specification

interface EntitySpecification<ENTITY, FILTER> {
    fun create(filter: FILTER? = null): Specification<ENTITY>
}
