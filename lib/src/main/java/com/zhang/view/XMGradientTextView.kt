package com.zhang.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatTextView

/**
 * 渐变色的TextView
 *
 * @author ZhangXiaoMing 2023-01-18 14:38 周三
 */
class XMGradientTextView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : AppCompatTextView(context , attrs , defStyleAttr) {

    @IntDef(HORIZONTAL , VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    private annotation class Orientation

    private var orientation = HORIZONTAL
    private var gradientColor : IntArray? = null

    init {
        init(attrs)
    }

    private fun init(attrs : AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs , R.styleable.XMGradientTextView)
        orientation = a.getInteger(R.styleable.XMGradientTextView_gradientOrientation , HORIZONTAL)

        var startColor : Int? = null
        var centerColor : Int? = null
        var endColor : Int? = null
        if (a.hasValue(R.styleable.XMGradientTextView_gradientStart)) startColor = a.getColor(
            R.styleable.XMGradientTextView_gradientStart ,
            currentTextColor
        )

        if (a.hasValue(R.styleable.XMGradientTextView_gradientCenter)) centerColor = a.getColor(
            R.styleable.XMGradientTextView_gradientCenter ,
            currentTextColor
        )

        if (a.hasValue(R.styleable.XMGradientTextView_gradientEnd)) endColor = a.getColor(
            R.styleable.XMGradientTextView_gradientEnd ,
            currentTextColor
        )

        a.recycle()

        if (startColor != null && endColor != null) {
            gradientColor = if (centerColor != null) intArrayOf(startColor , centerColor , endColor)
            else intArrayOf(startColor , endColor)
        }
    }

    override fun onDraw(canvas : Canvas) {
        if (gradientColor != null) {
            val paint = paint
            paint.setShader(gradient)
        }

        super.onDraw(canvas)
    }

    private val gradient : LinearGradient
        get() {
            val gradient = if (orientation == VERTICAL) {
                LinearGradient(
                    0f ,
                    0f ,
                    0f ,
                    height.toFloat() ,
                    gradientColor!! ,
                    null ,
                    Shader.TileMode.CLAMP
                )
            } else {
                LinearGradient(
                    0f ,
                    0f ,
                    width.toFloat() ,
                    0f ,
                    gradientColor!! ,
                    null ,
                    Shader.TileMode.CLAMP
                )
            }

            return gradient
        }

    /**
     * 设置颜色渐变方向
     *
     * @param orientation 渐变方向
     */
    fun setGradientOrientation(@Orientation orientation : Int) {
        this.orientation = orientation
        invalidate()
    }

    /**
     * 设置渐变颜色
     *
     * @param startColor 起始颜色
     * @param endColor   结束颜色
     */
    fun setGradientColor(startColor : Int , endColor : Int) {
        gradientColor = intArrayOf(startColor , endColor)
        invalidate()
    }

    /**
     * 设置渐变颜色
     *
     * @param startColor  起始颜色
     * @param centerColor 中间颜色
     * @param endColor    结束颜色
     */
    fun setGradientColor(startColor : Int , centerColor : Int , endColor : Int) {
        gradientColor = intArrayOf(startColor , centerColor , endColor)
        invalidate()
    }

    companion object {

        const val HORIZONTAL : Int = 0
        const val VERTICAL : Int = 1
    }
}
