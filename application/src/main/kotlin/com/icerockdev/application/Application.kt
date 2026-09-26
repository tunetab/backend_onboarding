package com.icerockdev.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.icerockdev"])
@EnableJpaRepositories(basePackages = ["com.icerockdev"])
@ComponentScan("com.icerockdev", nameGenerator = FullyQualifiedAnnotationBeanNameGenerator::class)
@EntityScan("com.icerockdev")
class Application

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
