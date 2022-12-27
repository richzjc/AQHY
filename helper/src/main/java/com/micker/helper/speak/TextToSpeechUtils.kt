package com.micker.helper.speak

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

object TextToSpeechUtils {

    var mTextToSpeech: TextToSpeech? = null

    fun initTextToSpeech(context: Context) {
        mTextToSpeech = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                mTextToSpeech?.language = Locale.CHINA
            }
        }

        mTextToSpeech?.setPitch(1f)
        mTextToSpeech?.setSpeechRate(1.5f)
    }


    fun textSpeechclose(){
        if(mTextToSpeech != null){
            mTextToSpeech?.stop()
            mTextToSpeech?.shutdown()
            mTextToSpeech = null
        }
    }

    fun textSpeak(speakStr : String){
        if(mTextToSpeech != null && !mTextToSpeech!!.isSpeaking){
            mTextToSpeech?.speak(speakStr, TextToSpeech.QUEUE_ADD, null)
        }
    }
}