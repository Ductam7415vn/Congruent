package com.aicontent.dongduu.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SystemCongruentCalculatorViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(SystemCongruentUiState())
    val uiState: StateFlow<SystemCongruentUiState> = _uiState.asStateFlow()

    private val _uiStateScreen =
        mutableStateOf<UiStateScreen<SystemCongruentUiState>>(UiStateScreen.Success)
    val uiStateScreen: State<UiStateScreen<SystemCongruentUiState>> = _uiStateScreen

    fun clearText() {
        _uiState.value = SystemCongruentUiState()
    }

    private val _bool = MutableStateFlow(false)
    val bool = _bool.asStateFlow()

    private val _boolState = MutableStateFlow(false)
    val boolState = _boolState.asStateFlow()

//    private val _state = MutableStateFlow(false)
//    val state = _state.asStateFlow()
//
//    private val _stateIHateIt = MutableStateFlow(false)
//    val stateIHateIt = _stateIHateIt.asStateFlow()

    fun checkConditionIllegal(): Boolean {
        for (i in 1 until _uiState.value.equations.size) {
            val d = findGCD(
                _uiState.value.equations[i].m.toLong(),
                _uiState.value.equations[0].m.toLong()
            )
            if (!isFactor(
                    (_uiState.value.equations[i].b.toLong() - _uiState.value.equations[0].b.toLong()),
                    d
                )
            ) {
                return false
            }
        }
        return true
    }

    private fun solutionSystemCongruence() {
        val const = _uiState.value
        for (i in 1 until const.equations.size) {

            val d = findGCD(
                const.equations[i].m.toLong(),
                const.equations[0].m.toLong()
            )
            var m1 = 0L
            var m2 = 0L
            var bNew = 0L

            if (const.equations[i].b.toLong() < const.equations[i - 1].b.toLong()) {
                if (isFactor(
                        (const.equations[i].b.toLong() - const.equations[0].b.toLong()),
                        d
                    )
                ) {
                    m1 = const.equations[i].m.toLong() / d
                    m2 = const.equations[i - 1].m.toLong() / d
                    bNew =
                        (const.equations[i - 1].b.toLong() - const.equations[i].b.toLong()) / d
                }

                if (findGCD(m1, m2).toInt() == 1) {
                    val (x, mNew) = solution(m1, bNew, m2)
                    _uiState.value.equations[i].b =
                        (_uiState.value.equations[i].b.toLong() + _uiState.value.equations[i].m.toLong() * x).toString()
                    _uiState.value.equations[i].m =
                        (_uiState.value.equations[i].m.toLong() * _uiState.value.equations[i - 1].m.toLong() / d).toString()
                }
            } else {
                if (isFactor(
                        (const.equations[i].b.toLong() - const.equations[0].b.toLong()),
                        d
                    )
                ) {
                    m1 = const.equations[i - 1].m.toLong() / d
                    m2 = const.equations[i].m.toLong() / d
                    bNew =
                        (const.equations[i].b.toLong() - const.equations[i - 1].b.toLong()) / d
                }

                if (findGCD(m1, m2).toInt() == 1) {
                    val (x, mNew) = solution(m1, bNew, m2)
                    _uiState.value.equations[i].b =
                        (_uiState.value.equations[i - 1].b.toLong() + _uiState.value.equations[i - 1].m.toLong() * x).toString()
                    _uiState.value.equations[i].m =
                        (_uiState.value.equations[i - 1].m.toLong() * mNew).toString()
                }
            }

            _uiState.value.a = "1"
            _uiState.value.b = _uiState.value.equations[const.equations.size - 1].b
            _uiState.value.m = _uiState.value.equations[const.equations.size - 1].m
        }
    }

    fun chineseRemainderTheorem() {
        val const = _uiState.value
        var d: Long = 0
        var n: Long = 1
        for (i in 1 until const.equations.size) {
            d = findGCD(
                const.equations[i].m.toLong(),
                const.equations[i - 1].m.toLong()
            )
            n *= const.equations[i - 1].m.toLong()
        }

        if (d != 1L) {
            solutionSystemCongruence()
        } else {
            n *= const.equations[const.equations.size - 1].m.toLong()
            var total = 0L
            for (i in 1 until const.equations.size) {
                val y = n / const.equations[i - 1].m.toLong()
                Log.d("the true", "$y")
                val (x, _) = solution(y, 1, const.equations[i - 1].m.toLong())
                total += x * const.equations[i - 1].b.toLong() * y
                Log.d("the true", "$x")
                Log.d("the true", "$total")

            }
            val y = n / const.equations[const.equations.size - 1].m.toLong()
            Log.d("the true", "$y")

            val (x, _) = solution(y, 1, const.equations[const.equations.size - 1].m.toLong())
            Log.d("the true", "$x")
            Log.d("the true", "$total")

            total += x * const.equations[const.equations.size - 1].b.toLong() * y

            val (u, _) = solution(1, total, n)

            _uiState.value.a = "1"
            _uiState.value.b = u.toString()
            _uiState.value.m = n.toString()
        }
    }

    fun addEquation(equation: LinearCongruenceEquation) {
        _uiState.value = _uiState.value.copy(
            equations = _uiState.value.equations + equation
        )
    }

    fun deleteEquation(equation: LinearCongruenceEquation) {
        _uiState.value = _uiState.value.copy(
            equations = _uiState.value.equations - equation
        )
    }


    fun checkState() {
        _bool.value = !_bool.value
    }

    fun checkBoolState() {
        _boolState.value = !_boolState.value

    }

//    fun findGCD(a: Long, b: Long): Long {
//        var num1 = a
//        var num2 = b
//        while (num2 != 0L) {
//            val temp = num2
//            num2 = num1 % num2
//            num1 = temp
//        }
//        return num1
//    }

    private fun findGCD(a: Long, b: Long): Long {
        return if (b == 0L) a
        else findGCD(b, a % b)
    }

    private fun isFactor(number: Long, potentialFactor: Long): Boolean {
        return number % potentialFactor == 0L
    }

    fun isInputTooLarge(input: Long): Boolean {
        val MAX_VALUE = 1000000000L
        if (input > MAX_VALUE) {
            _bool.value = true
            return true
        }
        _bool.value = false
        return false
    }

    fun isInputIsNotInteger(input: Long): Boolean {
        val floorValue = input.toDouble().toLong()
        val ceilValue = (input + 1).toDouble().toLong()
        return if (input != floorValue && input != ceilValue) {
            _boolState.value = true
            true
        } else {
            _boolState.value = false
            false
        }
    }

    private fun solution(firstNumber: Long, secondNumber: Long, modulus: Long): Pair<Long, Long> {
        val gcd = findGCD(firstNumber, modulus)
        var (x, m) = Pair(0L, 0L)

        if (gcd == 1L) {
            if (isFactor(secondNumber, firstNumber)) {
                x = secondNumber / firstNumber
                m = modulus
            } else {
                val k = findK(firstNumber, secondNumber, modulus, 1)
                x = (secondNumber + k * modulus) / firstNumber
                m = modulus
            }
        } else {
            val d = gcd
            if (isFactor(secondNumber, d)) {
                val a = firstNumber / d
                val b = secondNumber / d
                m = modulus / d

                val k = findK(a, b, m, 1)
                x = (secondNumber + k * modulus) / firstNumber
            }
        }

        if (x.toInt() >= m.toInt() && x > 0 && m > 0) {
            x %= m.toInt()
        }
        return Pair(x, m)
    }

    private fun findK(a: Long, b: Long, m: Long, currentK: Long): Long {
        if (isFactor(b + currentK * m, a)) {
            return currentK
        }
        return findK(a, b, m, currentK + 1)
    }


//    private fun findK(a: Long, b: Long, m: Long, currentK: Long): Long {
//        if (currentK * m % a == (a - b % a) % a) {
//            return currentK
//        }
//        return findK(a, b, m, currentK + 1)
//    }

    fun onTextBChange(b: String, index: Int) {
        val updatedEquations = _uiState.value.equations.toMutableList()
        updatedEquations[index] = updatedEquations[index].copy(b = b)
        _uiState.value = _uiState.value.copy(equations = updatedEquations)
    }

    fun onTextMChange(m: String, index: Int) {
        val updatedEquations = _uiState.value.equations.toMutableList()
        updatedEquations[index] = updatedEquations[index].copy(m = m)
        _uiState.value = _uiState.value.copy(equations = updatedEquations)
    }

//    fun handleStateIHateIt() {
//        _stateIHateIt.value = !_stateIHateIt.value
//    }
//
//    fun handleState() {
//        _state.value = !_state.value
//    }
//
//    fun handleOnClick() {
//        if (_uiState.value.equations.size == 1) {
//            _stateIHateIt.value = !_stateIHateIt.value
//        } else {
//            handleEquationsValidation()
//        }
//    }
//
//    private fun handleEquationsValidation() {
//        if (_uiState.value.equations.any { equation ->
//                equation.b.isEmpty() ||
//                        equation.m.isEmpty()
//            }) {
//            handleEmptyEquations()
//        } else {
//            val isAnyInputTooLargeInEquations = _uiState.value.equations.any { equation ->
//                isInputTooLarge(equation.b.toLong()) ||
//                        isInputTooLarge(equation.m.toLong())
//            }
//            if (isAnyInputTooLargeInEquations) {
//                clearText()
//            } else {
//                handleIntegerEquations()
//            }
//        }
//    }
//
//    private fun handleEmptyEquations() {
//        _state.value = !_state.value
//        clearText()
//    }
//
//    private fun handleIntegerEquations() {
//        val isAnyInputNotIntegerInEquations = _uiState.value.equations.any { equation ->
//            isInputIsNotInteger(equation.b.toLong()) ||
//                    isInputIsNotInteger(equation.m.toLong())
//        }
//        if (isAnyInputNotIntegerInEquations) {
//            clearText()
//        } else {
//            handleConditionIllegal()
//        }
//    }
//
//    fun handleConditionIllegal() : Boolean {
//        if (checkConditionIllegal()) {
//            chineseRemainderTheorem()
//            return true
//        }
//        return false
//    }
}
