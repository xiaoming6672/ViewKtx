package com.zhang.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * 正六边形ImageView
 *
 * @author ZhangXiaoMing 2020-12-23 13:42 星期三
 */
class XMHexagonImageView : AppCompatImageView {

    private var mBitmapShader : BitmapShader? = null
    private var mBitmapWidth = 0
    private var mBitmapHeight = 0
    var viewWidth : Int = 0
        private set
    private var mBitmap : Bitmap? = null

    constructor(context : Context) : super(context) {
        init()
    }

    constructor(context : Context , attrs : AttributeSet?) : super(context , attrs , 0) {
        init()
    }

    constructor(context : Context , attrs : AttributeSet? , defStyleAttr : Int) : super(context , attrs , defStyleAttr) {
        init()
    }

    private fun init() {
        this.isClickable = true
    }

    override fun onDraw(canvas : Canvas) {
        val drawable = drawable
        if (drawable == null || width * height == 0) {
            return
        }
        mBitmap = getBitmapFromDrawable(drawable)
        if (mBitmap == null) {
            return
        }

        setup()
        canvas.drawPath(hexagonPath , mBitmapPaint)
    }

    private var mPath : Path? = null

    val hexagonPath : Path
        /** 绘制正六边形的边，可以参照图 Posy/doc/HexagonImageView.jpg  */
        get() {
            val radius = (viewWidth / 2).toFloat()
            val distance = (viewWidth.toFloat() / 4 * (2 - sqrt(3.0))).toFloat() //六边形到边到内切圆的距离
            val halfRadius = radius / 2

            val p0x = radius
            val p0y = 0f

            val p1x = viewWidth - distance
            val p1y = halfRadius

            val p2x = p1x
            val p2y = (viewWidth * 3 / 4).toFloat()

            val p3x = radius
            val p3y = viewWidth.toFloat()

            val p4x = distance
            val p4y = p2y

            val p5x = p4x
            val p5y = p1y

            val path = mPath ?: synchronized(this) { Path().also { mPath = it } }
            return path.apply {
                reset()
                moveTo(p0x , p0y)
                lineTo(p1x , p1y)
                lineTo(p2x , p2y)
                lineTo(p3x , p3y)
                lineTo(p4x , p4y)
                lineTo(p5x , p5y)
                lineTo(p0x , p0y)
            }
        }

    override fun onMeasure(widthMeasureSpec : Int , heightMeasureSpec : Int) {
        super.onMeasure(widthMeasureSpec , heightMeasureSpec) //限制为正方形
        viewWidth = min(measuredWidth.toDouble() , measuredHeight.toDouble()).toInt()
        setMeasuredDimension(viewWidth , viewWidth)
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

    private fun getBitmapFromDrawable(drawable : Drawable?) : Bitmap? {
        if (drawable == null) {
            return null
        }
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
            drawable.setBounds(0 , 0 , viewWidth , viewWidth)
            drawable.draw(canvas)
            return bitmap
        } catch (e : OutOfMemoryError) {
            return null
        }
    }

    private fun setup() {
        if (mBitmap != null) {
            mBitmapShader = BitmapShader(mBitmap!! , Shader.TileMode.CLAMP , Shader.TileMode.CLAMP)
            mBitmapPaint.isAntiAlias = true
            mBitmapHeight = mBitmap!!.height
            mBitmapWidth = mBitmap!!.width
            updateShaderMatrix()
            mBitmapPaint.setShader(mBitmapShader)
        }
    }


    private fun updateShaderMatrix() {
        mShaderMatrix.set(null)
        val scale = if (mBitmapWidth != mBitmapHeight) {
            max((viewWidth.toFloat() / mBitmapWidth).toDouble() , (viewWidth.toFloat() / mBitmapHeight).toDouble())
                .toFloat()
        } else {
            viewWidth.toFloat() / mBitmapWidth
        }

        mShaderMatrix.setScale(scale , scale) //放大铺满

        val dx = viewWidth - mBitmapWidth * scale
        val dy = viewWidth - mBitmapHeight * scale
        mShaderMatrix.postTranslate(dx / 2 , dy / 2) //平移居中
        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    companion object {

        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMENSION = 1
        private val mShaderMatrix = Matrix()
        private val mBitmapPaint = Paint()
    }
}
