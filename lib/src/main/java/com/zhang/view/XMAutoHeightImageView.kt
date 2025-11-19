package com.zhang.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.zhang.library.utils.context.ContextUtils

/**
 * 自适应高度图片控件，等比例拉伸控件高度
 *
 * @author ZhangXiaoMing 2020-05-29 15:22 星期五
 */
class XMAutoHeightImageView @JvmOverloads constructor(
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
            val measuredWidth = measuredWidth
            val height = getScaleHeight(drawable.intrinsicWidth , drawable.intrinsicHeight , measuredWidth)

            setMeasuredDimension(measuredWidth , height)
        }
    }

    private fun getScaleHeight(scaleWidth : Int , scaleHeight : Int , width : Int) : Int {
        if (scaleWidth == 0) return 0
        return scaleHeight * width / scaleWidth
    }


}
