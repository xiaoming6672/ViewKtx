package com.zhang.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
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

    private val shaderMatrix : Matrix by lazy { Matrix() }
    private val bitmapPaint : Paint by lazy { Paint() }
    private var bitmapShader : BitmapShader? = null


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
        val drawable = drawable ?: run { super.onDraw(canvas);return }
        if (width * height == 0) {
            return
        }
        val bitmap = getBitmapFromDrawable(drawable) ?: run { super.onDraw(canvas);return }
        setupBitmap(bitmap)

        // 计算中心点和半径
        val centerX = width / 2F
        val centerY = height / 2F

        // 图片内容的半径需要减去描边宽度的一半，这样描边会正好在图片边缘
        val borderRadius = viewSize / 2F - borderWidth / 2F
        val contentRadius = borderRadius - borderWidth / 2F

        // 1. 绘制图片内容
        canvas.drawCircle(centerX , centerY , contentRadius , bitmapPaint)

        // 2. 如果有描边宽度，则绘制描边
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

    private fun getBitmapFromDrawable(drawable : Drawable) : Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        try {
            val bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(
                    COLOR_DRAWABLE_DIMENSION ,
                    COLOR_DRAWABLE_DIMENSION , BITMAP_CONFIG
                )
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth ,
                    drawable.intrinsicHeight , BITMAP_CONFIG
                )
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0 , 0 , viewSize , viewSize)
            drawable.draw(canvas)
            return bitmap
        } catch (e : OutOfMemoryError) {
            return null
        }
    }

    private fun setupBitmap(bitmap : Bitmap) {
        bitmapShader = BitmapShader(bitmap , Shader.TileMode.CLAMP , Shader.TileMode.CLAMP)
        bitmapPaint.isAntiAlias = true
        updateShaderMatrix(bitmap.width , bitmap.height)
        bitmapPaint.setShader(bitmapShader)
    }


    private fun updateShaderMatrix(bitmapWidth : Int , bitmapHeight : Int) {
        shaderMatrix.set(null)

        // 内容区域的尺寸 = 视图总尺寸 - 两倍的描边宽度
        val contentSize = viewSize - borderWidth * 2

        // 如果内容区域小于等于0，则无需绘制图片
        if (contentSize <= 0) {
            // 可以将shader置空，避免绘制任何内容
            bitmapPaint.shader = null
            return
        } else {
            bitmapPaint.shader = bitmapShader
        }

        val scale : Float
        val dx : Float
        val dy : Float

        // 计算缩放比例，让图片能填满内容区域
        if (bitmapWidth * contentSize > contentSize * bitmapHeight) {
            // 图片更宽，以高度为准进行缩放
            scale = contentSize / bitmapHeight
            dx = (contentSize - bitmapWidth * scale) * 0.5f
            dy = 0f
        } else {
            // 图片更高或等比，以宽度为准进行缩放
            scale = contentSize / bitmapWidth
            dx = 0f
            dy = (contentSize - bitmapHeight * scale) * 0.5f
        }

        shaderMatrix.setScale(scale , scale)
        // 平移时，需要先移动到内容区域的左上角（即 mBorderWidth），再加上居中的偏移量
        shaderMatrix.postTranslate(borderWidth + dx , borderWidth + dy)

        bitmapShader!!.setLocalMatrix(shaderMatrix)
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

        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMENSION = 1
    }
}
