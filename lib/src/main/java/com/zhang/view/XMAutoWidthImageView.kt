package com.zhang.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.zhang.library.utils.context.ContextUtils

/**
 * 自适应宽度图片控件，等比例拉伸控件宽度
 *
 * @author ZhangXiaoMing 2025-11-19 14:28 周三
 */
class XMAutoWidthImageView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : AppCompatImageView(context , attrs , defStyleAttr) {

    init {
        if (isInEditMode) ContextUtils.set(context.applicationContext)

        scaleType = ScaleType.FIT_XY
    }

    override fun setScaleType(scaleType : ScaleType) {
        super.setScaleType(ScaleType.FIT_XY)
    }

    override fun onMeasure(widthMeasureSpec : Int , heightMeasureSpec : Int) {
        super.onMeasure(widthMeasureSpec , heightMeasureSpec)

        val drawable = drawable
        if (drawable != null) {
            val measuredHeight = measuredHeight
            val width = getScaleWidth(drawable.intrinsicWidth , drawable.intrinsicHeight , measuredHeight)

            setMeasuredDimension(width , measuredHeight)
        }
    }

    private fun getScaleWidth(intrinsicWidth : Int , intrinsicHeight : Int , height : Int) : Int {
        if (intrinsicHeight == 0) return 0
        return intrinsicWidth * height / intrinsicHeight
    }

}
