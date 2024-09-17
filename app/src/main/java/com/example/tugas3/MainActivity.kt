package com.example.tugas3

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var display: TextView? = null
    var displayResult: TextView? = null
    var currentNumber: String = ""
    var operations: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        display = findViewById(R.id.display)
        displayResult = findViewById(R.id.displayResult)

        val numberButtons = listOf(
            findViewById<Button>(R.id.button0),
            findViewById<Button>(R.id.button1),
            findViewById<Button>(R.id.button2),
            findViewById<Button>(R.id.button3),
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button6),
            findViewById<Button>(R.id.button7),
            findViewById<Button>(R.id.button8),
            findViewById<Button>(R.id.button9)
        )

        for (button in numberButtons) {
            button.setOnClickListener { onNumberClick(button.text.toString()) }
        }

        findViewById<Button>(R.id.buttonPlus).setOnClickListener { onOperatorClick("+") }
        findViewById<Button>(R.id.buttonMinus).setOnClickListener { onOperatorClick("-") }
        findViewById<Button>(R.id.buttonMultiply).setOnClickListener { onOperatorClick("*") }
        findViewById<Button>(R.id.buttonDivide).setOnClickListener { onOperatorClick("/") }

        findViewById<Button>(R.id.buttonEquals).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.buttonC).setOnClickListener { clear() }
    }

    fun onNumberClick(number: String) {
        currentNumber += number
        updateDisplay()
    }

    fun onOperatorClick(op: String) {
        if (currentNumber.isNotEmpty()) {
            operations.add(currentNumber)
            operations.add(op)
            currentNumber = ""
            updateDisplay()
        }
    }

    fun calculateResult() {
        if (currentNumber.isNotEmpty()) {
            operations.add(currentNumber)
        }

        if (operations.isNotEmpty()) {
            try {
                val firstPass = mutableListOf<String>()
                var i = 0

                while (i < operations.size) {
                    if (operations[i] == "*" || operations[i] == "/") {
                        val num1 = firstPass.removeAt(firstPass.size - 1).toDouble()
                        val operator = operations[i]
                        val num2 = operations[i + 1].toDouble()
                        val result = when (operator) {
                            "*" -> num1 * num2
                            "/" -> num1 / num2
                            else -> num1
                        }
                        firstPass.add(result.toString())
                        i += 2
                    } else {
                        firstPass.add(operations[i])
                        i++
                    }
                }

                var result = firstPass[0].toDouble()
                var index = 1

                while (index < firstPass.size) {
                    val operator = firstPass[index]
                    val nextNumber = firstPass[index + 1].toDouble()

                    result = when (operator) {
                        "+" -> result + nextNumber
                        "-" -> result - nextNumber
                        else -> result
                    }

                    index += 2
                }

                displayResult?.text = result.toString()
                currentNumber = result.toString()
                operations.clear()
            } catch (e: Exception) {
                displayResult?.text = "Error"
            }
        }
    }

    fun clear() {
        currentNumber = ""
        operations.clear()
        display?.text = "0"
        displayResult?.text = "0"
    }

    fun updateDisplay() {
        val displayText = buildString {
            append(operations.joinToString(" "))
            if (currentNumber.isNotEmpty()) {
                append(" ")
                append(currentNumber)
            }
        }
        display?.text = displayText
    }
}