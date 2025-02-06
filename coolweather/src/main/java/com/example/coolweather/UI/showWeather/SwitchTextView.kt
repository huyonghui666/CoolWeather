package com.example.coolweather.UI.showWeather

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextSwitcher
import android.widget.TextView
import java.util.Timer
import java.util.TimerTask

class SwitchTextView(context:Context?,attrs:AttributeSet?): TextSwitcher(context,attrs) {

    private lateinit var tv:TextView
    init {
        setFactory {
            tv=TextView(context)
            tv.ellipsize=TextUtils.TruncateAt.END
            tv.maxLines=1
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f)
            tv.setPadding(0, 40, 0, 0)

            val translationY = tv.translationY
            val translateIn = TranslateAnimation(
                0f, 0f,
                200f, translationY
            )
            val translateOut = TranslateAnimation(
                0f, 0f,
                0f, -200f
            )

            val alphaIn = AlphaAnimation(0f, 1f)
            val alphaOut = AlphaAnimation(1f, 0f)

            val animatorSetIn = AnimationSet(true)
            animatorSetIn.addAnimation(translateIn)
            animatorSetIn.addAnimation(alphaIn)
            animatorSetIn.duration = 2000
            animatorSetIn.start()

            val animatorSetOut = AnimationSet(true)
            animatorSetOut.addAnimation(translateOut)
            animatorSetOut.addAnimation(alphaOut)
            animatorSetOut.duration = 2000

            //设置文字进入和出去的动画
            inAnimation = animatorSetIn
            outAnimation = animatorSetOut
            tv
        }
    }

    var position=0
    var size:Int = 0

    fun startPlay(data:List<String>){
        if (data==null||data.size==0)return
        size=data.size
        Timer().schedule(object :TimerTask(){
            override fun run() {
                post {
                    setText(data[position%size])
                    position++
                }
            }
        },100,5000)
    }
}