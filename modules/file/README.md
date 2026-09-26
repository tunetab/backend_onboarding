# File storage module

## Remove module without install

1. Remove string `include(":modules:file")` in `settings.gradle.kts` file
2. Remove path to `openapi.yaml` for this module in `docs/openapi/openapi-merge-settings.json` file
3. Remove tests for file module in path `tests/src/test/kotlin` package `com.icerockdev.application.tests.modules.file`
4. Remove configuration for file module in `build.gradle.kts` file in `tests`
   ```kotlin
   // File module
   implementation(projects.modules.notification)
   testImplementation(project(projects.modules.file.dependencyProject.path, "testArchive"))
   ```
5. Remove tests configuration in `application.yaml` (`tests/src/test/resources`) file:
   ```yaml
    s3storage:
      bucketName: test
      accessKeyId: 1234
      secretAccessKey: 1234
      endpoint: http://0.0.0.0
      region: test
      allowedMimeTypes: image/png,image/jpg,image/jpeg  
     ```
6. Remove `file` directory in directory `modules`
7. Remove `rustfs` service in docker-compose.yaml

## Install

1. Add `implementation(projects.modules.file)` to `build.gradle.kts` file in `application`
2. Add configuration to `application.yaml` (`application/main/resources`) file:
    ```yaml
    s3storage:
      bucketName: ${S3_BUCKET}
      accessKeyId: ${S3_ACCESS_KEY_ID}
      secretAccessKey: ${S3_SECRET_ACCESS_KEY}
      endpoint: ${S3_ENDPOINT}
      region: ${S3_REGION}
      allowedMimeTypes: ${S3_ALLOWED_MIME_TYPES}
    ```
3. Add environment to `.env` file:
    ```properties
    S3_HOST=
    S3_BUCKET=
    S3_ENDPOINT=
    S3_REGION=
    S3_ACCESS_KEY_ID=
    S3_SECRET_ACCESS_KEY=
    S3_ALLOWED_MIME_TYPES=image/png,image/jpg,image/jpeg
    ```
4. Run command for merge this module `openapi.yaml` to application `openapi.yaml`, you can see settings in `docs/openapi/openapi-merge-settings.json`
   ```shell
   ./project.sh mergeOpenapi
   ```
5. Add `IFileType` implementation, like:
   ```kotlin
   enum class FileType(private val type: Int, private val label: String) : IFileType {
        MAIN_PHOTO(1, LK.fileMainPhoto);
   
        override fun getType(): Int {
            return type
        }

        override fun getLabel(i18n: I18N): String {
            return i18n.t(label)
        }
   }
   ```
