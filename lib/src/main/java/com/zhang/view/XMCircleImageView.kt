package com.zhang.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.withSave
import kotlin.math.min

/**
 * 圆形ImageView
 *
 * @author ZhangXiaoMing 2021-07-02 21:38 星期五
 */
class XMCircleImageView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : AppCompatImageView(context , attrs , defStyleAttr) {


    /**描边颜色*/
    private var borderColor : Int = Color.TRANSPARENT

    /**描边宽度*/
    private var borderWidth : Float = 0F

    private val clipPath : Path by lazy { Path() }

    /**描边Paint对象*/
    private val borderPaint : Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = borderColor
            strokeWidth = borderWidth
        }
    }

    private var viewSize : Int = 0


    init {
        context.obtainStyledAttributes(attrs , R.styleable.XMCircleImageView).apply {
            borderWidth = getDimension(R.styleable.XMCircleImageView_borderWidth , 0F)
            borderColor = getColor(R.styleable.XMCircleImageView_borderColor , Color.TRANSPARENT)
            recycle()
        }

        super.setScaleType(ScaleType.CENTER_CROP)
    }


    override fun setScaleType(scaleType : ScaleType) {
        super.setScaleType(ScaleType.CENTER_CROP)
    }


    override fun onDraw(canvas : Canvas) {
        if (drawable == null || width == 0 || height == 0) {
            super.onDraw(canvas)
            return
        }

        // 计算中心点和半径
        val centerX = width / 2F
        val centerY = height / 2F

        // 图片内容的半径需要减去描边宽度的一半，这样描边会正好在图片边缘
        val borderRadius = viewSize / 2F - borderWidth / 2F
        val contentRadius = borderRadius - borderWidth / 2F

        // 1. 裁剪 Canvas 为圆形
        canvas.withSave {
            clipPath.reset()
            clipPath.addCircle(centerX , centerY , contentRadius , Path.Direction.CW)
            clipPath(clipPath)

            // 2. 调用父类绘制，此时绘制的内容会被限制在圆形 Path 内
            super.onDraw(this)
        }

        // 3. 如果有描边宽度，则绘制描边
        if (borderWidth > 0) {
            canvas.drawCircle(centerX , centerY , borderRadius , borderPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec : Int , heightMeasureSpec : Int) {
        super.onMeasure(widthMeasureSpec , heightMeasureSpec) //限制为正方形
        viewSize = min(measuredWidth , measuredHeight)
        setMeasuredDimension(viewSize , viewSize)
    }

    override fun setImageBitmap(bm : Bitmap) {
        super.setImageBitmap(bm)
        invalidate()
    }

    override fun setImageDrawable(drawable : Drawable?) {
        super.setImageDrawable(drawable)
        invalidate()
    }

    override fun setImageResource(resId : Int) {
        super.setImageResource(resId)
        invalidate()
    }

    /**
     * 设置描边
     *
     * @param color 描边颜色
     * @param width 描边宽度
     */
    fun setBorder(color : Int , width : Float) {
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


    companion object {

        private const val COLOR_DRAWABLE_DIMENSION = 1
    }
}
