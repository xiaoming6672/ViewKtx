package com.zhang.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint.Style
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.zhang.library.utils.context.ResUtils

/**
 * 文字带描边效果的TextView
 *
 * @author ZhangXiaoMing 2021-03-06 22:04 星期六
 */
open class XMStrokeTextView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : AppCompatTextView(context , attrs , defStyleAttr) {

    /** 用于描边的TextView  */
    protected val strokeTextView : TextView

    /** 描边宽度  */
    private var mStrokeWidth = 0f

    /** 描边颜色  */
    var strokeColor : ColorStateList? = null
        private set

    /** 是否自动填充空格适应左右两边  */
    private var autoFitSpace = true


    init {
        init(attrs)

        strokeTextView = TextView(context , attrs , defStyleAttr)
        initStrokeTextView()
    }

    private fun init(attrs : AttributeSet?) {
        if (attrs == null) {
            strokeColor = ColorStateList.valueOf(Color.TRANSPARENT)
            mStrokeWidth = 0f
            autoFitSpace = true
        } else {
            val a = context.obtainStyledAttributes(attrs , R.styleable.XMStrokeTextView)
            strokeColor =
                if (a.hasValue(R.styleable.XMStrokeTextView_strokeColor)) a.getColorStateList(R.styleable.XMStrokeTextView_strokeColor) else ColorStateList.valueOf(
                    Color.TRANSPARENT
                )
            mStrokeWidth =
                if (a.hasValue(R.styleable.XMStrokeTextView_strokeWidth)) a.getDimension(
                    R.styleable.XMStrokeTextView_strokeWidth ,
                    ResUtils.dp2px(1f).toFloat()
                ) else 0f
            autoFitSpace = a.getBoolean(R.styleable.XMStrokeTextView_autoFitSpace , true)

            a.recycle()
        }
    }

    private fun initStrokeTextView() {
        strokeTextView.compoundDrawablePadding = compoundDrawablePadding

        val drawables = compoundDrawablesRelative
        if (drawables.isNotEmpty()) strokeTextView.setCompoundDrawables(drawables[0] , drawables[1] , drawables[2] , drawables[3])

        strokeTextView.setTextColor(strokeColor)
        strokeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX , textSize)
        val strokePaint = strokeTextView.paint
        strokePaint.strokeWidth = mStrokeWidth
        strokePaint.style = Style.STROKE
    }

    override fun setTypeface(tf : Typeface?) {
        strokeTextView.typeface = tf

        super.setTypeface(tf)
    }

    override fun setTypeface(tf : Typeface? , style : Int) {
        strokeTextView.setTypeface(tf , style)

        super.setTypeface(tf , style)
    }

    override fun setGravity(gravity : Int) {
        strokeTextView.gravity = gravity

        super.setGravity(gravity)
    }

    override fun setLayoutParams(params : ViewGroup.LayoutParams) {
        strokeTextView.layoutParams = params

        super.setLayoutParams(params)
    }

    override fun onMeasure(widthMeasureSpec : Int , heightMeasureSpec : Int) {
        val text = strokeTextView.text
        if (TextUtils.isEmpty(text) || text != getText()) {
            strokeTextView.text = getText()
            this.postInvalidate()
        }

        strokeTextView.measure(widthMeasureSpec , heightMeasureSpec)
        super.onMeasure(widthMeasureSpec , heightMeasureSpec)
    }

    override fun onLayout(changed : Boolean , left : Int , top : Int , right : Int , bottom : Int) {
        strokeTextView.layout(left , top , right , bottom)

        super.onLayout(changed , left , top , right , bottom)
    }

    override fun setEnabled(enabled : Boolean) {
        strokeTextView.isEnabled = enabled

        super.setEnabled(enabled)
    }

    override fun setSelected(selected : Boolean) {
        strokeTextView.isSelected = selected

        super.setSelected(selected)
    }

    override fun setTextSize(size : Float) {
        strokeTextView.textSize = size

        super.setTextSize(size)
    }

    override fun setTextSize(unit : Int , size : Float) {
        strokeTextView.setTextSize(unit , size)

        super.setTextSize(unit , size)
    }

    override fun setText(text : CharSequence , type : BufferType) {
        if (!autoFitSpace) {
            strokeTextView.text = text.toString()

            super.setText(text , type)
            return
        }

        val builder = SpannableStringBuilder()
        if (!TextUtils.isEmpty(text)) {
            val space = " "
            val s = text.toString()

            if (!s.startsWith(space)) builder.append(space)

            builder.append(text)

            if (!s.endsWith(space)) builder.append(space)
        }

        strokeTextView.text = builder.toString()

        super.setText(builder , type)
    }

    override fun setCompoundDrawablePadding(pad : Int) {
        strokeTextView.compoundDrawablePadding = pad

        super.setCompoundDrawablePadding(pad)
    }

    override fun setCompoundDrawables(left : Drawable? , top : Drawable? , right : Drawable? , bottom : Drawable?) {
        strokeTextView.setCompoundDrawables(left , top , right , bottom)

        super.setCompoundDrawables(left , top , right , bottom)
    }

    override fun setCompoundDrawablesRelative(start : Drawable? , top : Drawable? , end : Drawable? , bottom : Drawable?) {
        strokeTextView.setCompoundDrawablesRelative(start , top , end , bottom)

        super.setCompoundDrawablesRelative(start , top , end , bottom)
    }

    override fun onDraw(canvas : Canvas) {
        strokeTextView.draw(canvas)

        super.onDraw(canvas)
    }

    /** 设置描边颜色  */
    fun setStrokeColor(color : Int) {
        strokeColor = ColorStateList.valueOf(color)
        strokeTextView.setTextColor(strokeColor)
    }

    /** 设置描边颜色  */
    fun setStrokeColor(color : ColorStateList) {
        strokeColor = color
        strokeTextView.setTextColor(strokeColor)
    }

    var strokeWidth : Float
        /** 获取描边宽度，单位：px  */
        get() = mStrokeWidth
        /** 设置描边宽度，单位：px  */
        set(width) {
            mStrokeWidth = width

            val strokePaint = strokeTextView.paint
            strokePaint.strokeWidth = mStrokeWidth
        }

    /** 是否自动填充空格适配  */
    fun isAutoFitSpace() : Boolean {
        return autoFitSpace
    }

    /** 设置是否自动填充空格适配  */
    fun setAutoFitSpace(autoFitSpace : Boolean) {
        this.autoFitSpace = autoFitSpace

        invalidate()
    }
}
