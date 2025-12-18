package com.zhang.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint.Style
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.zhang.lib.ktx.number.dp

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


    /**描边粗细大小*/
    var strokeWidth = 0F
        private set

    /**描边颜色*/
    lateinit var strokeColor : ColorStateList
        private set


    init {
        initAttribute(attrs)
    }

    private fun initAttribute(attrs : AttributeSet?) {
        attrs?.let {
            with(context.obtainStyledAttributes(attrs , R.styleable.XMStrokeTextView)) {
                strokeColor = getColorStateList(R.styleable.XMStrokeTextView_strokeColor)
                    ?: ColorStateList.valueOf(Color.TRANSPARENT)

                strokeWidth = getDimension(R.styleable.XMStrokeTextView_strokeWidth , 0F)

                recycle()
            }
        }
    }

    /**
     * 设置描边粗细
     *
     * @param width 粗细大小，单位：px
     */
    fun setStrokeWidth(width : Float) {
        strokeWidth = width
        postInvalidate()
    }

    /**
     * 设置描边粗细
     *
     * @param width 粗细大小，单位：dp
     */
    fun setStrokeWidth(width : Int) {
        strokeWidth = width.dp.toFloat()
        postInvalidate()
    }

    /**
     * 设置描边颜色
     *
     * @param color 颜色
     */
    fun setStrokeColor(color : Int) {
        strokeColor = ColorStateList.valueOf(color)
        postInvalidate()
    }

    /**
     * 设置描边颜色
     *
     * @param color 颜色
     */
    fun setStrokeColor(color : ColorStateList) {
        strokeColor = color
        postInvalidate()
    }

    override fun onMeasure(widthMeasureSpec : Int , heightMeasureSpec : Int) {
        // 1. 先让父类计算出文本本身的尺寸
        super.onMeasure(widthMeasureSpec , heightMeasureSpec)

        if (strokeWidth > 0) {
            // 2. 获取原始计算出的宽高
            val originalWidth = measuredWidth
//            val originalHeight = measuredHeight

            // 3. 增加描边宽度
            // 描边是居中于文本轮廓绘制的，所以需要左右/上下各留出 strokeWidth
            val newWidth = strokeWidth.times(2).plus(originalWidth).toInt()
//            val newHeight = strokeWidth.times(2).plus(originalHeight).toInt()

            // 3. 设置新的尺寸
//            setMeasuredDimension(newWidth , newHeight)
            setMeasuredDimension(newWidth , measuredHeight)
        }
    }

    override fun onDraw(canvas : Canvas) {
        // 1. 保存当前的 Canvas 状态
        canvas.save()

        // 2. 将 Canvas 向右下方平移 strokeWidth 的距离
        // 这样文本和描边都会被推入新增的 onMeasure 空间中央
//        canvas.translate(strokeWidth , strokeWidth)
        canvas.translate(strokeWidth , 0F)

        //保存设定的字色
        val currentTextColor = textColors

        //绘制描边
        with(paint) {
            style = Style.STROKE
            strokeWidth = this@XMStrokeTextView.strokeWidth
            setTextColor(strokeColor)
        }
        super.onDraw(canvas)

        //绘制填充
        with(paint) {
            style = Style.FILL
            setTextColor(currentTextColor)
        }
        super.onDraw(canvas)

        // 3. 恢复 Canvas 状态，避免影响后续绘制
        canvas.restore()
    }
}