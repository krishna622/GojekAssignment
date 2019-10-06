package com.krishna.gojekassignment.ui.utility

import com.krishna.gojekassignment.ui.model.TrendDataItem

class StarComparator : Comparator<TrendDataItem> {

    override fun compare(o1: TrendDataItem, o2: TrendDataItem): Int {
        return if (o1.stars == o2.stars)
            0
        else if (o1.stars!! > o2.stars!!)
            1
        else
            -1
    }
}