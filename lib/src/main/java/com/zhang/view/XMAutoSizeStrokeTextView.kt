package com.zhang.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue

/**
 * 自动字号的描边文字控件
 *
 * @author ZhangXiaoMing 2021-04-25 18:26 星期日
 */
class XMAutoSizeStrokeTextView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : XMStrokeTextView(context , attrs , defStyleAttr) {

    var isAutoSizeEnabled = true
        set(value) {
            field = value
            processFitAutoSize()
        }


    override fun onDraw(canvas : Canvas) {
        super.onDraw(canvas)
        processFitAutoSize()
    }


    /** 自适应字号  */
    private fun processFitAutoSize() {
        if (!isAutoSizeEnabled) return

        if (this.lineCount <= 1) return

        val paint : Paint = paint
        var textSize = textSize

        val drawables = compoundDrawables
        var drawableWidth = 0
        if (drawables[0] != null) {
            drawableWidth += drawables[0]!!.intrinsicWidth
            drawableWidth += compoundDrawablePadding
        }
        if (drawables[2] != null) {
            drawableWidth += drawables[2]!!.intrinsicWidth
            drawableWidth += compoundDrawablePadding
        }

        val availableTextViewWidth = this.width - paddingLeft - paddingRight - drawableWidth
        val measureWidth = paint.measureText(this.text.toString())
        if (measureWidth > availableTextViewWidth) {
            textSize = textSize * (availableTextViewWidth.toFloat() / measureWidth)
        }

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX , textSize)
    }
}
