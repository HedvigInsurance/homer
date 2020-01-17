package com.hedvig.homer.handlers

import com.google.cloud.speech.v1.RecognitionAudio
import com.google.cloud.speech.v1.RecognitionConfig
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding
import com.google.cloud.speech.v1.SpeechClient
import com.google.cloud.speech.v1.SpeechRecognitionResult
import com.google.protobuf.ByteString
import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import org.apache.tomcat.util.http.parser.AcceptLanguage
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.util.UUID


@Component
class SpeechHandler {
  fun convertSpeechToText(file: String? = null, language: String): String {
    SpeechClient.create().use { speechClient ->

      val fileName = ""

      val config = RecognitionConfig.newBuilder()
      .setEncoding(AudioEncoding.FLAC)
      .setSampleRateHertz(RATE)
      .setLanguageCode(language)
      .build()

      val file = convert(fileName)
      val data = Files.readAllBytes(file.toPath())
      val audioBytes = ByteString.copyFrom(data)

      val audio = RecognitionAudio.newBuilder()
        .setContent(audioBytes)
        .build()

      val response = speechClient.longRunningRecognizeAsync(config, audio)

      while (!response.isDone) {
        println("Waiting for response...")
        Thread.sleep(10000)
      }

      val results: List<SpeechRecognitionResult> = response.get().resultsList

      var finalResult : String = ""

      results.forEach { result ->
        val alternative = result.getAlternatives(0)
        finalResult += alternative.transcript +  " [Confidence: ${alternative.confidence}] " + "\n"
        println("Transcription: ${alternative.transcript}]\n")
      }
      return finalResult
    }
  }


  private fun convert(filename: String): File {
    val tempExecId = UUID.randomUUID().toString()
    val tempOutputFile = File.createTempFile("temp_$tempExecId", "_out.flac")

    val ffmpeg = FFmpeg("ffmpeg")
    val ffprobe = FFprobe("ffprobe")

    val builder = FFmpegBuilder()
      .setInput(filename)     // Filename, or a FFmpegProbeResult
      .overrideOutputFiles(true) // Override the output if it exists
      .addOutput(tempOutputFile.absolutePath)
      .setAudioSampleRate(RATE)
      .setAudioChannels(1)
      .done()

    val executor = FFmpegExecutor(ffmpeg, ffprobe)

    // Run a one-pass encode
    executor.createJob(builder).run()

    return tempOutputFile
  }

  companion object {
    const val RATE: Int = 16000
  }
}
