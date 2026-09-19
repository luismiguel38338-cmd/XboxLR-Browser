package com.example.tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TextToSpeechManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _speechRate = MutableStateFlow(1.0f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _currentText = MutableStateFlow("")
    val currentText: StateFlow<String> = _currentText.asStateFlow()

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale("es", "ES")
            tts?.setSpeechRate(_speechRate.value)
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                    _isPaused.value = false
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _isPaused.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _isPaused.value = false
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    _isSpeaking.value = false
                    _isPaused.value = false
                }
            })
        }
    }

    fun speak(text: String, title: String = "") {
        if (text.isBlank()) return
        val cleanText = text
            .replace("<[^>]*>".toRegex(), " ")
            .replace("\\s+".toRegex(), " ")
            .trim()

        if (cleanText.isBlank()) return

        _currentText.value = title.ifBlank { "Página Web" }
        _isSpeaking.value = true
        _isPaused.value = false

        if (!isInitialized) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isInitialized = true
                    tts?.language = Locale("es", "ES")
                    tts?.setSpeechRate(_speechRate.value)
                    val params = Bundle()
                    tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, params, "nova_web_reader")
                }
            }
            return
        }

        val params = Bundle()
        tts?.setSpeechRate(_speechRate.value)
        tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, params, "nova_web_reader")
    }

    fun pause() {
        if (_isSpeaking.value) {
            tts?.stop()
            _isSpeaking.value = false
            _isPaused.value = true
        }
    }

    fun resume(fullText: String) {
        if (_isPaused.value) {
            speak(fullText, _currentText.value)
        }
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _isPaused.value = false
        _currentText.value = ""
    }

    fun cycleSpeed() {
        val nextRate = when (_speechRate.value) {
            1.0f -> 1.25f
            1.25f -> 1.5f
            1.5f -> 2.0f
            2.0f -> 0.75f
            else -> 1.0f
        }
        _speechRate.value = nextRate
        tts?.setSpeechRate(nextRate)
    }

    fun destroy() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
