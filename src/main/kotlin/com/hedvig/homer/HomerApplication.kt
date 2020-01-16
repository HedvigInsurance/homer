package com.hedvig.homer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HomerApplication

fun main(args: Array<String>) {
	runApplication<HomerApplication>(*args)
}
