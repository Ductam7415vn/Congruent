package com.aicontent.dongduu.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CongruentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CongruentUiState())
    val uiState: StateFlow<CongruentUiState> = _uiState.asStateFlow()

    private val _uiStateScreen =
        mutableStateOf<UiStateScreen<CongruentUiState>>(UiStateScreen.Success)
    val uiStateScreen: State<UiStateScreen<CongruentUiState>> = _uiStateScreen

    private val _bool = MutableStateFlow(false)
    val bool = _bool.asStateFlow()

    private val _boolState = MutableStateFlow(false)
    val boolState = _boolState.asStateFlow()

    var kali: Long = 0L

    fun clearText() {
        _uiState.value = CongruentUiState()
    }

    fun checkCondition(): Boolean {
        val firstNumber = _uiState.value.a.toLongOrNull() ?: return false
        val secondNumber = _uiState.value.b.toLongOrNull() ?: return false
        val modulus = _uiState.value.m.toLongOrNull() ?: return false

        return isFactor(secondNumber, findGCD(firstNumber, modulus)) && firstNumber != 0L
    }

    fun checkState() {
        _bool.value = !_bool.value
    }
    fun checkBoolState(){
        _boolState.value = !_boolState.value
    }

    private fun findGCD(a: Long, b: Long): Long {
        return if (b == 0L) a
        else findGCD(b, a % b)
    }

    fun isFactor(number: Long, potentialFactor: Long): Boolean {
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

//    private fun solution() {
//        val firstNumber = _uiState.value.a.toLong()
//        val secondNumber = _uiState.value.b.toLong()
//        val modulus = _uiState.value.m.toLong()
//
//        val gcdResult = extendedEuclidean(firstNumber, modulus)
//
//        if (gcdResult.gcd == 1L) {
//            val x = gcdResult.xGcd
//            val k = (x * secondNumber).mod(modulus)
//            _uiState.value.x = k
//            kali = k
//        } else {
//            _uiState.value.d = gcdResult.gcd
//            if (isFactor(secondNumber, gcdResult.gcd)) {
//                _uiState.value.apply {
//                    a = (firstNumber / gcdResult.gcd).toString()
//                    b = (secondNumber / gcdResult.gcd).toString()
//                    m = (modulus / gcdResult.gcd).toString()
//                }
//                val x = gcdResult.xGcd * (secondNumber / gcdResult.gcd)
//                val k = (x % (modulus / gcdResult.gcd)).mod(modulus / gcdResult.gcd)
//                _uiState.value.x = k
//                _uiState.value.k = k
//            }
//        }
//        if (_uiState.value.x >= modulus) {
//            _uiState.value.x %= modulus
//        }
//    }
//
//    data class ExtendedGcdResult(val gcd: Long, val xGcd: Long, val yGcd: Long)
//
//    private fun extendedEuclidean(a: Long, b: Long): ExtendedGcdResult {
//        if (b == 0L) {
//            return ExtendedGcdResult(a, 1, 0)
//        }
//
//        val result = extendedEuclidean(b, a % b)
//        val xGcd = result.yGcd
//        val yGcd = result.xGcd - (a / b) * result.yGcd
//        return ExtendedGcdResult(result.gcd, xGcd, yGcd)
//    }

    private fun solution() {
        val firstNumber = _uiState.value.a.toLong()
        val secondNumber = _uiState.value.b.toLong()
        val modulus = _uiState.value.m.toLong()

        val gcd = findGCD(firstNumber, modulus)

        if (gcd == 1L) {
            if (isFactor(secondNumber, firstNumber)) {
                _uiState.value.x = secondNumber / firstNumber
            } else {
                val k = findK(firstNumber, secondNumber, modulus, 1)
                _uiState.value.x = (secondNumber + k * modulus) / firstNumber
                kali = k
            }
        } else {
            _uiState.value.d = gcd
            if (isFactor(secondNumber, gcd)) {
                _uiState.value.apply {
                    a = (firstNumber / gcd).toString()
                    b = (secondNumber / gcd).toString()
                    m = (modulus / gcd).toString()
                }
                val k = findK(firstNumber, secondNumber, modulus, 1)
                _uiState.value.x = (secondNumber + k * modulus) / firstNumber
                _uiState.value.k = k
            }
        }
        if (_uiState.value.x.toInt() >= _uiState.value.m.toInt()) {
            _uiState.value.x %= _uiState.value.m.toInt()
        }
    }


    private fun findK(a: Long, b: Long, m: Long, currentK: Long): Long {
        if (isFactor(b + currentK * m, a)) {
            return currentK
        }
        return findK(a, b, m, currentK + 1)
    }

//
//    private fun findK(a: Long, b: Long, m: Long, originalM: Long, currentK: Long): Long {
//        if (currentK * m % a == (a - b % a) % a) {
//            return currentK
//        }
//        return findK(a, b, m, originalM, currentK + 1)
//    }

    private fun saveToInitialVales() {
        _uiState.value = _uiState.value.copy(
            initialA = _uiState.value.a,
            initialB = _uiState.value.b,
            initialM = _uiState.value.m
        )
    }

    fun resetToInitialValues() {
        _uiState.value = _uiState.value.copy(
            a = _uiState.value.initialA,
            b = _uiState.value.initialB,
            m = _uiState.value.initialM
        )
    }

    fun restartScreenAndNavigate() {
        viewModelScope.launch {
            restartScreen()
            saveToInitialVales()
            solution()
        }
    }

    fun onTextAChange(firstNumber: String) {
        _uiState.value = _uiState.value.copy(a = firstNumber)
    }

    fun onTextBChange(secondNumber: String) {
        _uiState.value = _uiState.value.copy(b = secondNumber)
    }

    fun onTextMChange(modulus: String) {
        _uiState.value = _uiState.value.copy(m = modulus)
    }

    fun fetchNumber() {
        solution()
        try {
            _uiStateScreen.value = UiStateScreen.Success
        } catch (e: Exception) {
            _uiStateScreen.value = UiStateScreen.Error("something wrong in here")
        }
    }

    private fun restartScreen() {
        viewModelScope.launch {
            _uiStateScreen.value = UiStateScreen.Loading
            delay(2000L)
            _uiStateScreen.value = UiStateScreen.Success
        }
    }
}