package com.yuriihetsko.fallingsand

fun build2DArrayList(cols: Int, rows: Int): ArrayList<ArrayList<Int>> {
    val arrayList2D = ArrayList<ArrayList<Int>>(cols)

    for (i in 0 until cols) {
        val row = ArrayList<Int>(rows)
        for (j in 0 until rows) {
            row.add(0)
        }
        arrayList2D.add(row)
    }

    return arrayList2D
}