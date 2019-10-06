package com.krishna.gojekassignment.ui.utility

import com.krishna.gojekassignment.ui.model.TrendDataItem
import kotlin.Comparator


internal class NameComparator : Comparator<TrendDataItem> {
    override fun compare(o1: TrendDataItem?, o2: TrendDataItem?): Int {
        val s1 = o1
        val s2 = o2

        return s1?.name!!.compareTo(s2?.name!!)
    }
}