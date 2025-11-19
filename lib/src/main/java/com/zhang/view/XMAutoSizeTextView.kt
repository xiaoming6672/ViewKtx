package com.zhang.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView

/**
 * 自动调整字号的TextView
 *
 * @author ZhangXiaoMing 2021-04-23 10:59 星期五
 */
class XMAutoSizeTextView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = android.R.attr.textViewStyle ,
) : AppCompatTextView(context , attrs , defStyleAttr) {

    /** 自适应字号是否可用  */
    var isAutoSizeEnabled : Boolean = false
        set(value) {
            field = value
            processFitAutoSize()
        }

    /** 原始设置的字号大小  */
    private var mOriginalTextSize = 0f

    init {
        init(attrs)
    }

    private fun init(attrs : AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs , R.styleable.XMAutoSizeTextView)

        isAutoSizeEnabled = a.getBoolean(R.styleable.XMAutoSizeTextView_autoSizeEnable , true)

        a.recycle()

        mOriginalTextSize = textSize
    }


    override fun setText(text : CharSequence , type : BufferType) {
        if (mOriginalTextSize > 0) setTextSize(TypedValue.COMPLEX_UNIT_PX , mOriginalTextSize)

        super.setText(text , type)
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
