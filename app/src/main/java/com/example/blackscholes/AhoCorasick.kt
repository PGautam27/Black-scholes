package com.example.blackscholes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class AhoCorasick() : ViewModel() {

//    private var _arr = MutableLiveData<Array<String>>()
//    var arr1 : MutableLiveData<Array<String>> = _arr
//    var arr : MutableList<String>? = _arr.value?.toMutableList()

    private var _list = MutableLiveData<List<String>>()
    var list: LiveData<List<String>> = _list

    private var _inputString = MutableLiveData<String>()

    private var _inpuArray = MutableLiveData<Array<String>>()

    private var MAXS = 500

    private var MAXC = 26

    private var out = IntArray(MAXS)

    private var f = IntArray(MAXS)

    private var g = Array(MAXS) {
        IntArray(
            MAXC
        )
    }

    fun inputValues(string:String, list:List<String>){
        _inpuArray.value = list.toTypedArray()
        _inputString.value = string
        searchWords(_inpuArray.value!!, _inpuArray.value!!.size, _inputString.value!!)
    }

    private fun buildMatchingMachine(arr: Array<String>, k: Int): Int {

        Arrays.fill(out, 0)

        for (i in 0 until MAXS) Arrays.fill(g[i], -1)

        var states = 1

        for (i in 0 until k) {
            val word = arr[i]
            var currentState = 0

            for (element in word) {
                val ch = element - 'a'

                if (g[currentState][ch] == -1) g[currentState][ch] = states++
                currentState = g[currentState][ch]
            }

            out[currentState] = out[currentState] or (1 shl i)
        }

        for (ch in 0 until MAXC) if (g[0][ch] == -1) g[0][ch] = 0

        Arrays.fill(f, -1)

        val q: Queue<Int> = LinkedList()

        for (ch in 0 until MAXC) {

            if (g[0][ch] != 0) {
                f[g[0][ch]] = 0
                q.add(g[0][ch])
            }
        }

        while (!q.isEmpty()) {

            val state: Int = q.peek() as Int
            q.remove()

            for (ch in 0 until MAXC) {

                if (g[state][ch] != -1) {

                    var failure = f[state]

                    while (g[failure][ch] == -1) failure = f[failure]
                    failure = g[failure][ch]
                    f[g[state][ch]] = failure

                    out[g[state][ch]] = out[g[state][ch]] or out[failure]

                    q.add(g[state][ch])
                }
            }
        }
        return states
    }

    private fun findNextState(currentState: Int, nextInput: Char): Int {
        var answer = currentState
        val ch = nextInput - 'a'

        while (g[answer][ch] == -1) answer = f[answer]
        return g[answer][ch]
    }

    private fun searchWords(
        arr: Array<String>, k: Int,
        text: String
    ) {

        buildMatchingMachine(arr, k)

        var currentState = 0
        for (i in text.indices) {
            currentState = findNextState(
                currentState,
                text[i]
            )

            if (out[currentState] == 0) continue

            for (j in 0 until k) {
                if (out[currentState] and (1 shl j) > 0) {
//                    _arr.value?.set(j,"""Word ${arr[j]} appears from ${i - arr[j].length + 1} to $i""")
//                    _list.value = _list.value?.plus("""Word ${arr[j]} appears from ${i - arr[j].length + 1} to $i""")
                    _list.value = _list.value.orEmpty() + """Word ${arr[j]} appears from ${i - arr[j].length + 1} to $i"""
//                    print(
//                        """Word ${arr[j]} appears from ${i - arr[j].length + 1} to $i"""
//                    )
                }
            }
        }
    }

//    @JvmStatic
//    fun main(args: Array<String>) {
//        val arr = arrayOf("he", "she", "hers", "his")
//        val text = "ahishers"
//        val k = arr.size
//        searchWords(arr, k, text)
//    }
}
