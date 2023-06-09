package com.example.advancecalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.advancecalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal) {
                    binding.tvInput1.append(view.text)
                    canAddDecimal = false
                }
            } else {
                binding.tvInput1.append(view.text)
                canAddOperation = true
            }
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.tvInput1.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        binding.tvInput1.text = ""
        binding.tvInput2.text = ""
    }

    fun backSpaceAction(view: View) {
        val length = binding.tvInput1.length()
        if (length > 0) {
            binding.tvInput1.text = binding.tvInput1.text.subSequence(0, length - 1)
        }
    }


    fun equalsAction(view: View) {
        binding.tvInput2.text = calculateResults()
    }

    private fun calculateResults(): String {

        val digitsOperators = digitsOperator()
        if (digitsOperators.isEmpty()) {
            return ""
        }
        val multiDivision = multiDivisionCalculator(digitsOperators)
        if (multiDivision.isEmpty())
            return ""

        val result = addSubtractCalculate(multiDivision)
        return result.toString()

        val mode = modulusAction(digitsOperators)
        return ""
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+') {
                    result += nextDigit
                }
                if (operator == '-') {
                    result -= nextDigit
                }
            }
        }
        return result
    }

    private fun multiDivisionCalculator(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = calcmultiDiv(list)
        }
        return list
    }

    private fun calcmultiDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex) {
                newList.add(passedList[i])
            }
        }
        return newList
    }
    fun modulusAction(passedList: MutableList<Any>): Float {
        var mode = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '%') {
                    mode %= nextDigit
                }
            }
        }
        return mode
    }

    private fun digitsOperator(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in binding.tvInput1.text) {
            if (character.isDigit() || character == '.') {
                currentDigit += character
            } else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "") {
            list.add(currentDigit.toFloat())
        }
        return list
    }

}



