package com.yuriihetsko.fallingsand

fun build2DArrayList(rows: Int, columns: Int): ArrayList<ArrayList<Int>> {
    val arrayList2D = ArrayList<ArrayList<Int>>(rows)

    for (i in 0..rows) {
        val row = ArrayList<Int>(columns)
        for (j in 0..columns) {
            row.add(0)
        }
        arrayList2D.add(row)
    }

    return arrayList2D
}