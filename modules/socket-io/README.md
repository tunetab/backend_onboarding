# [Socket IO](https://socket.io/) module

## Remove module without install

1. Remove string `include(":modules:socket-io")` in `settings.gradle.kts` file
2. Remove string `socketIO = { module = "com.corundumstudio.socketio:netty-socketio", version.ref = "socketIOVersion"}`
   in `./gradle/libs.versions.toml`
3. Remove string `socketIOVersion` with version of socket-io in `./gradle/libs.versions.toml`
4. Remove `socket-io` directory in directory `modules`

## Install

1. Add `implementation(projects.modules.socketIo)` to `build.gradle.kts` file in `application`
2. Add configuration to `application.yaml` (`main/resources`) file
    ```yaml
    #...
    socketServer:
      port: ${SOCKET_SERVER_PORT}
      debug: ${SOCKET_SERVER_DEBUG_ENABLED}
    #...
    ```
   and in `application.yaml` for tests (`test/resources`)
   ```yaml
   socketServer:
     port: 8082
     debug: true
   ```
3. Add environment to `.env` file
    ```properties
    #...
    SOCKET_SERVER_PORT=8082
    SOCKET_SERVER_DEBUG_ENABLED=true
    #...
    ```
4. Add configuration file to application config package (`com.icerockdev.application.config`), for example:
    ```kotlin
   @Configuration
   class SocketIOConfig {
   
       @Autowired
       private lateinit var socketConfig: Config
   
       @Autowired
       private lateinit var customJWTTokenGenerator: CustomJWTTokenGenerator
   
       @Component
       data class Config(
           @Value("\${server.address}")
           val host: String,
           @Value("\${socketServer.port}")
           val port: Int,
           @Value("\${socketServer.debug}")
           val debug: Boolean
       )
   
       @Bean
       fun getSocketServer(): SocketServer {
           val socketServer = SocketServer(
               socketServerConfig = SocketServerConfig(
                   host = socketConfig.host,
                   port = socketConfig.port,
                   logger = LoggerFactory.getLogger(SocketServer::class.java),
                   isDebug = socketConfig.debug,
                   getSocketUser = { token: String ->
                       val userKey: UserKey = customJWTTokenGenerator.getUserKey(token)
                       return@SocketServerConfig SocketUser(id = userKey.id.toString())
                   }
               )
           )
           // example of add event listener
           socketServer.addEventListener(
               "TEST_EVENT",
               Request::class,
               SocketDataListener { request: Request, socketUser: SocketUser ->
                   // some logic
                   AbstractResponse()
               }
           )
   
           socketServer.start()
   
           return socketServer
       }
   }
   ```
5. Add event listeners if needed (example in SocketIOConfig, previous step)
6. For development and production servers add configuration for socket to `nginx.conf.tmpl`
   ```
   map $http_upgrade $connection_upgrade {
       default upgrade;
       '' close;
   }
   
   upstream websocket {
      server web:8082;
   }
   
   server {
      listen 8080 ssl http2;
      server_name ${API_SERVER_NAME};
      client_max_body_size 128M;
   
      ssl_protocols TLSv1.1 TLSv1.2 TLSv1.3;
      ssl_session_cache shared:SSL:20m;
      ssl_session_timeout 10m;
      ssl_ciphers  "HIGH:!aNULL:!MD5:!kEDH";
      ssl_prefer_server_ciphers on;
   
      error_page 404 = @404;
   
      location / {
         proxy_set_header X-Real-IP $remote_addr;
         proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
         proxy_set_header X-NginX-Proxy true;
         proxy_set_header X-Forwarded-Proto https;
         proxy_ssl_session_reuse off;
         proxy_set_header Host $http_host;
         proxy_set_header Upgrade $http_upgrade;
         proxy_set_header Connection $connection_upgrade;
         proxy_cache_bypass $http_upgrade;
         proxy_redirect off;
         proxy_pass http://websocket;
      }
   
      #SSL
      ssl_certificate /etc/letsencrypt/cert/live/${API_SERVER_NAME}/fullchain.pem;
      ssl_certificate_key /etc/letsencrypt/cert/live/${API_SERVER_NAME}/privkey.pem;
   }
   ```
7. Add port `8080` to nginx service in `docker-compose.yml`
   ```yaml
      ....
    ports:
      - "80:80"
      - "443:443"
      - "8080:8080"
      ....
   ```


