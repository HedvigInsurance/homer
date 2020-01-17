package com.hedvig.homer.web

import com.hedvig.homer.handlers.SpeechHandler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("v1")
class FileController(
  val speechHandler: SpeechHandler
) {

  @GetMapping("/get")
  fun get(): ResponseEntity<String> {

    val test1 = speechHandler.convertSpeechToText(null, "sv-SE")

    val test2 = speechHandler.convertSpeechToText(null, "el-GR")

    return ResponseEntity.ok(test1 + "\n" + test2)
  }
}