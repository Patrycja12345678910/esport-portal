package com.esportportal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.resilience.annotation.EnableResilientMethods

@SpringBootApplication
@EnableResilientMethods
class BackendApplication

fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}