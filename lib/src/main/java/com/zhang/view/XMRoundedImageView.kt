package com.zhang.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.zhang.lib.ktx.common.ifTrue
import com.zhang.lib.ktx.widget.getViewHeight
import com.zhang.lib.ktx.widget.getViewWidth

/**
 * 圆角图控件
 *
 * @author ZhangXiaoMing 2021-07-19 11:38 星期一
 */
class XMRoundedImageView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : AppCompatImageView(context , attrs , defStyleAttr) {


    /** 左上角圆角度数，四个圆角度数不统一的时候使用  */
    private var topLeftRadius = 0f

    /** 右上角圆角度数，四个圆角度数不统一的时候使用  */
    private var topRightRadius = 0f

    /** 左下角圆角度数，四个圆角度数不统一的时候使用  */
    private var bottomLeftRadius = 0f

    /** 右下角圆角度数，四个圆角度数不统一的时候使用  */
    private var bottomRightRadius = 0f

    /**描边颜色*/
    private var borderColor : Int = Color.TRANSPARENT

    /**描边宽度*/
    private var borderWidth : Float = 0F

    /**描边Paint对象*/
    private val borderPaint : Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = borderColor
            strokeWidth = borderWidth
        }
    }

    init {
        initAttributes(context , attrs)
    }

    private fun initAttributes(context : Context , attrs : AttributeSet?) {
        context.obtainStyledAttributes(attrs , R.styleable.XMRoundedImageView).apply {
            borderWidth = getDimension(R.styleable.XMCircleImageView_borderWidth , 0F)
            borderColor = getColor(R.styleable.XMCircleImageView_borderColor , Color.TRANSPARENT)

            hasValue(R.styleable.XMRoundedImageView_radius).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radius , 0F)) {
                    topLeftRadius = this
                    topRightRadius = this
                    bottomLeftRadius = this
                    bottomRightRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusTopPart).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radiusTopPart , 0F)) {
                    topLeftRadius = this
                    topRightRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusBottomPart).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radiusBottomPart , 0F)) {
                    bottomLeftRadius = this
                    bottomRightRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusLeftPart).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radiusLeftPart , 0F)) {
                    topLeftRadius = this
                    bottomLeftRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusRightPart).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radiusRightPart , 0F)) {
                    topRightRadius = this
                    bottomRightRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusTopLeft).ifTrue {
                topLeftRadius = getDimension(R.styleable.XMRoundedImageView_radiusTopLeft , 0F)
            }

            hasValue(R.styleable.XMRoundedImageView_radiusTopRight).ifTrue {
                topRightRadius = getDimension(R.styleable.XMRoundedImageView_radiusTopRight , 0F)
            }

            hasValue(R.styleable.XMRoundedImageView_radiusBottomLeft).ifTrue {
                bottomLeftRadius = getDimension(R.styleable.XMRoundedImageView_radiusBottomLeft , 0F)
            }

            hasValue(R.styleable.XMRoundedImageView_radiusBottomRight).ifTrue {
                bottomRightRadius = getDimension(R.styleable.XMRoundedImageView_radiusBottomRight , 0F)
            }

            recycle()
        }
    }

    override fun onDraw(canvas : Canvas) {
        canvas.clipPath(roundedPath)
        super.onDraw(canvas)

        // 2. 如果设置了描边宽度，则在图片之上绘制描边
        if (borderWidth > 0) {
            canvas.drawPath(roundedPath , borderPaint)
        }
    }

    private val roundedPath : Path
        get() {
            val radius = floatArrayOf(
                topLeftRadius , topLeftRadius ,
                topRightRadius , topRightRadius ,
                bottomRightRadius , bottomRightRadius ,
                bottomLeftRadius , bottomLeftRadius ,
            )

            val path = Path()
            val rect = RectF(
                /* left = */ 0f ,
                /* top = */ 0f ,
                /* right = */ getViewWidth().toFloat() ,
                /* bottom = */ getViewHeight().toFloat()
            )
            path.addRoundRect(rect , radius , Path.Direction.CW)

            return path
        }

    /**
     * 设置描边
     *
     * @param color 描边颜色
     * @param width 描边宽度
     */
    fun setBoarder(color : Int , width : Float) {
        if (borderColor == color && borderWidth == width) return

        if (borderColor != color) {
            borderColor = color
            borderPaint.color = color
        }
        if (borderWidth != width) {
            borderWidth = width
            borderPaint.strokeWidth = width
        }
    }

    /**
     * 设置圆角度数
     *
     * @param radius 圆角度数
     */
    fun setCornerRadius(radius : Float) {
        if (topLeftRadius == radius
            && topRightRadius == radius
            && bottomLeftRadius == radius
            && bottomRightRadius == radius
        ) {
            return
        }

        topLeftRadius = radius
        topRightRadius = radius
        bottomLeftRadius = radius
        bottomRightRadius = radius
        invalidate()
    }

    /**
     * 设置圆角度数
     *
     * @param topLeft       圆角度数
     * @param topRight      圆角度数
     * @param bottomLeft    圆角度数
     * @param bottomRight   圆角度数
     */
    fun setCornerRadius(topLeft : Float , topRight : Float , bottomLeft : Float , bottomRight : Float) {
        if (topLeftRadius == topLeft
            && topRightRadius == topRight
            && bottomLeftRadius == bottomLeft
            && bottomRightRadius == bottomRight
        ) {
            return
        }

        topLeftRadius = topLeft
        topRightRadius = topRight
        bottomLeftRadius = bottomLeft
        bottomRightRadius = bottomRight
        invalidate()
    }
}
