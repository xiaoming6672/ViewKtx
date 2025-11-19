/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */
package com.zhang.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class XMCenterLayoutManager : LinearLayoutManager {

    @JvmOverloads
    constructor(
        context : Context ,
        orientation : Int = RecyclerView.VERTICAL ,
        reverseLayout : Boolean = false ,
    ) : super(context , orientation , reverseLayout)

    constructor(
        context : Context ,
        attrs : AttributeSet? ,
        defStyleAttr : Int ,
        defStyleRes : Int ,
    ) : super(context , attrs , defStyleAttr , defStyleRes)

    override fun smoothScrollToPosition(recyclerView : RecyclerView , state : RecyclerView.State , position : Int) {
        val smoothScroller : RecyclerView.SmoothScroller = CenterSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private inner class CenterSmoothScroller(context : Context) : LinearSmoothScroller(context) {

        override fun calculateDtToFit(
            viewStart : Int ,
            viewEnd : Int ,
            boxStart : Int ,
            boxEnd : Int ,
            snapPreference : Int ,
        ) : Int {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
        }
    }
}
