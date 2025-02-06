package com.example.coolweather.UI.warning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.coolweather.R

class Warning : AppCompatActivity(), View.OnClickListener {

    private var warning_linear: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warning)

        warning_linear = findViewById<LinearLayout>(R.id.warning_linear)
        findViewById<View>(R.id.warning_back).setOnClickListener(this)

        val bundle = intent.extras
        for (i in 0 until bundle!!.size()) {
            val view: View = LayoutInflater.from(this).inflate(R.layout.warning_item, null)
            val bundleString = bundle!!.getString("预警$i")
            val warning_title =
                bundleString!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val warning_text =
                bundleString!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val tv_warning_title = view.findViewById<TextView>(R.id.warning_title)
            val tv_warning_text = view.findViewById<TextView>(R.id.warning_text)
            tv_warning_title.text = warning_title
            tv_warning_text.setPadding(0, 40, 0, 0)
            tv_warning_text.text = warning_text
            warning_linear!!.addView(view)
        }

    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.warning_back) {
            finish()
        }
    }
}