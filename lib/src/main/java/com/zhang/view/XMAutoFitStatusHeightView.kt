package com.zhang.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.zhang.library.utils.context.ResUtils

/**
 * 自动适应状态栏高度的View。仿照[Space]不显示任何内容
 *
 * @author ZhangXiaoMing 2023-02-13 14:19 周一
 */
class XMAutoFitStatusHeightView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : View(context , attrs , defStyleAttr) {

    init {
        if (isInEditMode) ResUtils.set(context.applicationContext)
    }

    override fun onDraw(canvas : Canvas) = Unit

    override fun onMeasure(widthMeasureSpec : Int , heightMeasureSpec : Int) {
        val height = ResUtils.getStatusBarHeight()
        setMeasuredDimension(1 , height)
    }
}
