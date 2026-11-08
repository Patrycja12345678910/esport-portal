package com.esportportal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.boot.runApplication
import org.springframework.resilience.annotation.EnableResilientMethods

@SpringBootApplication
@EnableScheduling
@EnableResilientMethods
class BackendApplication

fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}
