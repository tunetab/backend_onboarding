package com.icerockdev.file.repository

import com.icerockdev.file.model.File
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
internal interface FileRepository : JpaRepository<File, UUID> {

    fun findAllByEntityIdAndEntityType(entityId: UUID, entityType: Int): List<File>

    fun findAllByKeyIn(keys: List<String>): List<File>

    fun findByKey(key: String): File?

    fun findAllByEntityIdInAndEntityType(entityIdList: List<UUID>, entityType: Int): List<File>

    fun deleteAllByKeyIn(keys: List<String>): Int

    fun deleteByKey(key: String)

    @Query("SELECT f FROM File f WHERE f.entityId is null AND f.updatedAt < :dateTime ")
    fun getAllUnlinked(
        @Param("dateTime")
        dateTime: LocalDateTime
    ): List<File>
}
