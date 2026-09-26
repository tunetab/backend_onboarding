# Notification module

## Remove module without install

1. Remove string `include(":modules:notification")` in `settings.gradle.kts` file
2. Remove path to `openapi.yaml` for this module in `docs/openapi/openapi-merge-settings.json` file
3. Remove tests for notification module in path `tests/src/test/kotlin` package `com.icerockdev.application.tests.modules.notification`
4. Remove configuration for notification module in `build.gradle.kts` file in `tests`
   ```kotlin
   // Notification module
   implementation(projects.modules.notification)
   testImplementation(project(projects.modules.notification.dependencyProject.path, "testArchive"))
   ```
5. Remove tests configuration in `application.yaml` (`tests/src/test/resources`) file:
   ```yaml
   twilio:
      phone: 123
      account-sid: 31313131
      auth-token: 3131313
   fcm:
      google-service-account-json: '{}'
   smtp:
      host: 31313
      port: 3131
      secure: TLS
      auth: false
      user:
      password:
      from:
         email: ddsada
         name: dada     
     ```
6. Remove `notification` directory in directory `modules`

## Install
1. Add `implementation(projects.modules.notification)` to `build.gradle.kts` file in `application`
2. Add configuration to `application.yaml` (`application/src/main/resources`) file:
    ```yaml
   twilio:
      phone: ${TWILIO_PHONE}
      account-sid: ${TWILIO_ACCOUNT_SID}
      auth-token: ${TWILIO_AUTH_TOKEN}
   fcm:
      google-service-account-json: ${GOOGLE_SERVICE_ACCOUNT_JSON}
   smtp:
      host: ${SMTP_HOST}
      port: ${SMTP_PORT}
      secure: ${SMTP_SECURE}
      auth: ${SMTP_AUTH}
      user: ${SMTP_USER}
      password: ${SMTP_PASSWORD}
      from:
         email: ${SMTP_FROM_EMAIL}
         name: ${SMTP_FROM_NAME}     
    ```
3. Add environment to `.env` file
   ```properties
   GOOGLE_SERVICE_ACCOUNT_JSON=
   
   SMTP_FROM_EMAIL=noreply@icerockdev.com
   SMTP_FROM_NAME=#{PROJECT_NAME}-app
   SMTP_HOST=smtp.gmail.com
   SMTP_PORT=587
   SMTP_AUTH=false
   SMTP_SECURE=TLS
   SMTP_PASSWORD=
   SMTP_USER=
   
   TWILIO_PHONE=
   TWILIO_ACCOUNT_SID=
   TWILIO_AUTH_TOKEN=
   ```
4. Replace mocked bean `INotificationService` in `ApplicationConfig` with:

   ```kotlin
   @Bean
   fun getINotificationService(): INotificationService {
       return object : INotificationService {
            @Autowired
            private lateinit var notificationService: NotificationService
            override fun sendEmail(
                subject: String,
                to: Map<String, String?>,
                emailBody: String,
                async: Boolean,
                settingsForSaveNotification: INotificationService.ApplicationSettingsForSaveNotification?
            ) = notificationService.sendEmail(
                subject = subject,
                to = to,
                emailBody = emailBody,
                async = async,
                settingsForSaveNotification = settingsForSaveNotification?.let {
                    SettingsForSaveNotification(
                        entityId = it.entityId,
                        entityType = it.entityType,
                        notificationType = it.notificationType
                    )
                }
            )

            override fun sendPush(
                userIds: List<UUID>,
                notification: INotificationService.ApplicationNotificationModuleData,
                data: Map<String, String>?,
                settingsForSaveNotification: INotificationService.ApplicationSettingsForSaveNotification?
            ) = notificationService.sendPush(
                userIds = userIds,
                notification = NotificationModuleData(
                    title = notification.title,
                    body = notification.body,
                    sound = notification.sound,
                    clickAction = notification.clickAction,
                    badge = notification.badge,
                    icon = notification.icon,
                    color = notification.color,
                    tag = notification.tag
                ),
                data = data,
                settingsForSaveNotification = settingsForSaveNotification?.let {
                    SettingsForSaveNotification(
                        entityId = it.entityId,
                        entityType = it.entityType,
                        notificationType = it.notificationType
                    )
                }
            )

            override fun sendSms(
                message: String,
                phone: String,
                settingsForSaveNotification: INotificationService.ApplicationSettingsForSaveNotification?
            ) = notificationService.sendSms(
                message = message,
                phone = phone,
                settingsForSaveNotification = settingsForSaveNotification?.let {
                    SettingsForSaveNotification(
                        entityId = it.entityId,
                        entityType = it.entityType,
                        notificationType = it.notificationType
                    )
                }
            )
        }
   }
   ```
5. Add `INotificationType` and `INotificationTypeListByUserType` implementations, like:

   ```kotlin
   enum class NotificationType(private val type: Int, private val label: String) : INotificationType {
        INFO(1, LK.infoNotification);
   
        override fun getType(): Int {
            return type
        }

        override fun getLabel(i18n: I18N): String {
            return i18n.t(label)
        }
   }
   ```
   ```kotlin
   class NotificationTypeListByUserType : INotificationTypeListByUserType {
       override fun getList(entityType: Int): List<INotificationType> {
           return when (entityType) {
               Role.USER.entityType -> listOf(NotificationType.INFO)
               else -> emptyList()
           }
       }
   }
   ```
6. Add bean configuration for `INotificationTypeListByUserType` in `ApplicationConfig`:
   ```kotlin
    @Bean
    fun getNotificationTypeListByUserType(): INotificationTypeListByUserType {
        return NotificationTypeListByUserType()
    }
   ```
7. Run command for merge this module `openapi.yaml` to application `openapi.yaml`, you can see settings in `docs/openapi/openapi-merge-settings.json`
   ```shell
   ./project.sh mergeOpenapi
   ```
