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

    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private var mBitmapShader : BitmapShader? = null

    private var mBitmapWidth = 0
    private var mBitmapHeight = 0
    var viewSize : Int = 0
        private set
    private var mBitmap : Bitmap? = null

    private var mPath : Path? = null

    init {
        super.setScaleType(ScaleType.CENTER_CROP)
    }


    override fun setScaleType(scaleType : ScaleType) {
        super.setScaleType(ScaleType.CENTER_CROP)
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
        canvas.drawPath(circlePath , mBitmapPaint)
    }

    private val circlePath : Path
        /** 绘制正六边形的边，可以参照图 Posy/doc/HexagonImageView.jpg  */
        get() {
            val path = mPath ?: synchronized(this) { Path().also { mPath = it } }
            val radius = (viewSize / 2).toFloat()

            path.addCircle(
                (width / 2).toFloat() ,
                (height / 2).toFloat() ,
                radius ,
                Path.Direction.CW
            )

            return path
        }

    override fun onMeasure(widthMeasureSpec : Int , heightMeasureSpec : Int) {
        super.onMeasure(widthMeasureSpec , heightMeasureSpec) //限制为正方形
        viewSize = min(measuredWidth.toDouble() , measuredHeight.toDouble()).toInt()
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
            drawable.setBounds(0 , 0 , viewSize , viewSize)
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
            max((viewSize.toFloat() / mBitmapWidth).toDouble() , (viewSize.toFloat() / mBitmapHeight).toDouble()).toFloat()
        } else {
            viewSize.toFloat() / mBitmapWidth
        }

        mShaderMatrix.setScale(scale , scale) //放大铺满

        val dx = viewSize - mBitmapWidth * scale
        val dy = viewSize - mBitmapHeight * scale
        mShaderMatrix.postTranslate(dx / 2 , dy / 2) //平移居中
        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    companion object {

        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMENSION = 1
    }
}
