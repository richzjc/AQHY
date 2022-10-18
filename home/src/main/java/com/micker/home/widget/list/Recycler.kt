package com.micker.home.widget.list

import android.view.View
import java.util.*
import kotlin.collections.HashMap

class Recycler {
    private var views: HashMap<Int, Stack<View>> = HashMap()

    fun put(view: View, type: Int) {
        if (views.containsKey(type)) {
            val stack = views[type]
            stack?.push(view)
        } else {
            val stack = Stack<View>()
            stack.push(view)
            views.put(type, stack)
        }
    }

    operator fun get(type: Int): View? {
        return try {
            if (views.containsKey(type)) {
                val stack = views[type]
                stack?.pop()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}