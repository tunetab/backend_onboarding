package com.icerockdev.application.tests.common.db.containers

import java.sql.Statement
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

object DBContainerSetup : AfterEachCallback {
    val postgresContainer = PostgreSQLContainer(
        DockerImageName
            .parse("gitlab.icerockdev.com:4567/docker/alpine-postgres-postgis-15:latest")
            .asCompatibleSubstituteFor("postgres")
    )

    private val statement: Statement

    init {
        postgresContainer.start()
        statement = postgresContainer.createConnection("").createStatement()
    }

    override fun afterEach(context: ExtensionContext?) {
        try {
            // List of queries for table truncating
            // TRUNCATE tblName CASCADE;
            statement.execute(
                """
                SELECT 'TRUNCATE ' || input_table_name || ' CASCADE;' AS truncate_query
                FROM (SELECT table_schema || '.' || table_name AS input_table_name
                      FROM information_schema.tables
                      WHERE table_type != 'VIEW'
                        AND table_schema NOT IN ('pg_catalog', 'information_schema')
                        AND table_schema NOT LIKE 'pg_toast%'
                        AND table_name NOT LIKE 'spatial_%') AS information;
            """.trimIndent()
            )

            var sql = ""
            while (statement.resultSet.next()) {
                sql += statement.resultSet.getString(1)
            }
            statement.execute(sql)
        } catch (e: Throwable) {
            println(e.localizedMessage)
        }
    }
}
