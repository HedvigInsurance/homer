package com.hedvig.homer.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("v1")
class FileController {

  @GetMapping("/get")
  fun get(): ResponseEntity<Void> {
    return ResponseEntity.accepted().build()
  }
}