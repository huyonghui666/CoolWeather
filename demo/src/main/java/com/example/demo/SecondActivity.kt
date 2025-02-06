package com.example.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        findViewById<Button>(R.id.button2).setOnClickListener(this)

        val xingming=intent.getStringExtra("姓名")
        Toast.makeText(this,"$xingming", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        val intent=Intent().apply {
            putExtra("曾姓名","冬瓜")
        }
        setResult(RESULT_OK,intent)
        finish()
    }
}