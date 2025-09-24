package com.example.eventhandling

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.Color
import android.view.GestureDetector
import androidx.core.content.ContextCompat

// Класс для рисования прямоугольников
class BoxDrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    // Список всех нарисованных прямоугольников
    private val boxes = mutableListOf<Box>()

    // Текущий "рисуемый" прямоугольник
    private var currentBox: Box? = null

    // Цвет по умолчанию
    private var currentColor: Int =
        ContextCompat.getColor(context, android.R.color.holo_blue_light)

    // Кисть для рисования
    private val boxPaint = Paint().apply {
        color = currentColor
        style = Paint.Style.FILL
        alpha = 100 // прозрачность (0 = полностью прозрачный, 255 = непрозрачный)
    }

    // для обводки
    private val strokePaint = Paint().apply {
        color = currentColor
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    // обработка двойного тапа
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            // Проверяем, попадает ли точка в какой-либо прямоугольник
            val tapX = e.x
            val tapY = e.y
            val iterator = boxes.iterator()
            while (iterator.hasNext()) {
                val box = iterator.next()
                val rect = RectF(
                    Math.min(box.start.x, box.end.x),
                    Math.min(box.start.y, box.end.y),
                    Math.max(box.start.x, box.end.x),
                    Math.max(box.start.y, box.end.y)
                )
                if (rect.contains(tapX, tapY)) {
                    iterator.remove() // удаляем прямоугольник
                    invalidate()
                    return true
                }
            }
            return super.onDoubleTap(e)
        }
    })

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event) // обрабатываем жесты

        val current = PointF(event.x, event.y)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // создаем новый прямоугольник и добавляем в список
                currentBox = Box(current, current, currentColor)
                boxes.add(currentBox!!)
            }
            MotionEvent.ACTION_MOVE -> {
                currentBox?.end = current
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                currentBox?.end = current
                currentBox = null // фиксируем прямоугольник
                invalidate()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Рисуем все прямоугольники
        for (box in boxes) {
            val rect = RectF(
                Math.min(box.start.x, box.end.x),
                Math.min(box.start.y, box.end.y),
                Math.max(box.start.x, box.end.x),
                Math.max(box.start.y, box.end.y),
            )

            // Задаём цвет конкретного прямоугольника
            boxPaint.color = box.color
            boxPaint.alpha = 100

            // Задаём цвет обводки
            strokePaint.color = box.color
            strokePaint.alpha = 255

            canvas.drawRect(rect, boxPaint)
            canvas.drawRect(rect, strokePaint)
        }
    }

    // Метод смены цвета
    fun setBoxColor(color: Int) {
        currentColor = color
    }

    // Метод очистки экрана
    fun clearBoxes() {
        boxes.clear()
        invalidate()
    }
}

// Класс-хранилище прямоугольник
data class Box(
    var start: PointF,
    var end: PointF,
    var color: Int
)