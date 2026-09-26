package com.icerockdev.file

import com.icerockdev.file.abstract.IFileType
import com.icerockdev.file.usecase.DeleteFileByEntityUseCase
import com.icerockdev.file.usecase.GetFilesURLUseCase
import com.icerockdev.file.usecase.LinkEntityWithFileUseCase
import com.icerockdev.file.usecase.ShareObjectUseCase
import com.icerockdev.file.usecase.UnlinkEntityFromFileByKeyUseCase
import com.icerockdev.file.usecase.UnlinkEntityFromFileUseCase
import java.time.Duration
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FileService {

    @Autowired
    private lateinit var deleteFileByEntityUseCase: DeleteFileByEntityUseCase

    @Autowired
    private lateinit var linkEntityWithFileUseCase: LinkEntityWithFileUseCase

    @Autowired
    private lateinit var shareObjectUseCase: ShareObjectUseCase

    @Autowired
    private lateinit var unlinkEntityFromFileByKeyUseCase: UnlinkEntityFromFileByKeyUseCase

    @Autowired
    private lateinit var unlinkEntityFromFileUseCase: UnlinkEntityFromFileUseCase

    @Autowired
    private lateinit var getFilesURLUseCase: GetFilesURLUseCase

    fun deleteFileByEntity(
        entityIdList: List<UUID>,
        entityType: IFileType
    ) = deleteFileByEntityUseCase.execute(entityIdList, entityType)

    fun getFilesDto(
        entityIdList: List<UUID>,
        entityType: IFileType
    ) = getFilesURLUseCase.execute(entityIdList, entityType)

    fun linkEntityWithFile(
        key: String,
        entityId: UUID,
        entityType: IFileType
    ) = linkEntityWithFileUseCase.execute(key, entityId, entityType)

    fun shareFile(
        bucketName: String,
        key: String?,
        duration: Duration
    ) = shareObjectUseCase.execute(bucketName, key, duration)

    fun unlinkEntityFromFileByKey(
        keyList: List<String>
    ) = unlinkEntityFromFileByKeyUseCase.execute(keyList)

    fun unlinkEntityFromFile(
        entityId: UUID,
        entityType: IFileType,
    ) = unlinkEntityFromFileUseCase.execute(entityId, entityType)
}
