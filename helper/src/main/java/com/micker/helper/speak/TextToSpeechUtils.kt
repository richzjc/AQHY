package com.micker.helper.speak

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

object TextToSpeechUtils {

    var mTextToSpeech: TextToSpeech? = null

    fun initTextToSpeech(context: Context) {
        mTextToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener {
            if(it == TextToSpeech.SUCCESS){
                mTextToSpeech?.setLanguage(Locale.CHINA)
            }
        })

        mTextToSpeech?.setPitch(1.0f)
        mTextToSpeech?.setSpeechRate(1.0f)
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