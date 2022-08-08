package com.gray.annotation_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.gray.annotation.Binding
import com.gray.runtime.BindingData

class MainActivity : AppCompatActivity() {
    @Binding("张三")
    lateinit var person: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BindingData.bind(this)
        val tv = findViewById<TextView>(R.id.tv)
        tv.setOnClickListener {
            tv.text = person.name
        }
    }
}