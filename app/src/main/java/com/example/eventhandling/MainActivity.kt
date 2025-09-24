package com.example.eventhandling

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boxDrawingView = findViewById<BoxDrawingView>(R.id.boxDrawingView)
        val spinnerColors = findViewById<Spinner>(R.id.spinnerColors)
        val btnClear = findViewById<Button>(R.id.btnClear)

        // Список цветов
        val colorNames = listOf("Красный", "Зелёный", "Синий", "Чёрный", "Фиолетовый", "Оранжевый")
        val colorValues = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.MAGENTA, Color.parseColor("#FFA500"))

        // Адаптер для Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colorNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerColors.adapter = adapter

        // Слушатель выбора
        spinnerColors.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedColor = colorValues[position]
                boxDrawingView.setBoxColor(selectedColor)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })

        // Очистка экрана
        btnClear.setOnClickListener {
            boxDrawingView.clearBoxes()
        }
    }
}
