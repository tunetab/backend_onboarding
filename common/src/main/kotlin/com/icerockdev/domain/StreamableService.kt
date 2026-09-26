package com.icerockdev.domain


import java.util.stream.Stream
import org.springframework.data.domain.Sort

interface StreamableService<ENTITY, FILTER> {
    fun stream(filterDto: FILTER? = null, sort: Sort? = null): Stream<ENTITY>
}
