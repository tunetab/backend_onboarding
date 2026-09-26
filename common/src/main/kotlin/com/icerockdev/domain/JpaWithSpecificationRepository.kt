package com.icerockdev.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface JpaWithSpecificationRepository<ENTITY, ID> : JpaRepository<ENTITY, ID>, JpaSpecificationExecutor<ENTITY>
