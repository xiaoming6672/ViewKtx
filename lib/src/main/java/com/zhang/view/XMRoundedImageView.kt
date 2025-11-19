package com.zhang.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.zhang.library.utils.context.ViewUtils

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


    /** 圆角度数，四个圆角度数统一时候使用  */
    private var mRadius : Float? = null

    /** 左上角圆角度数，四个圆角度数不统一的时候使用  */
    private var mTopLeftRadius = 0f

    /** 右上角圆角度数，四个圆角度数不统一的时候使用  */
    private var mTopRightRadius = 0f

    /** 左下角圆角度数，四个圆角度数不统一的时候使用  */
    private var mBottomLeftRadius = 0f

    /** 右下角圆角度数，四个圆角度数不统一的时候使用  */
    private var mBottomRightRadius = 0f

    init {
        initAttributes(context , attrs)
    }

    private fun initAttributes(context : Context , attrs : AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs , R.styleable.XMRoundedImageView)

        if (a.hasValue(R.styleable.XMRoundedImageView_radius)) mRadius =
            a.getDimension(R.styleable.XMRoundedImageView_radius , 0f)
        else {
            mTopLeftRadius = a.getDimension(R.styleable.XMRoundedImageView_radiusTopLeft , 0f)
            mTopRightRadius = a.getDimension(R.styleable.XMRoundedImageView_radiusTopRight , 0f)
            mBottomLeftRadius = a.getDimension(R.styleable.XMRoundedImageView_radiusBottomLeft , 0f)
            mBottomRightRadius = a.getDimension(R.styleable.XMRoundedImageView_radiusBottomRight , 0f)
        }

        a.recycle()
    }

    override fun onDraw(canvas : Canvas) {
        canvas.clipPath(roundedPath)
        super.onDraw(canvas)
    }

    private val roundedPath : Path
        /** 获取圆角矩形路径  */
        get() = if (mRadius != null) uniteCornersPath
        else differentCornersPath

    private val uniteCornersPath : Path
        /** 获取四个圆角度数统一的路径  */
        get() {
            val path = Path()

            val rect = RectF(
                /* left = */ 0f ,
                /* top = */ 0f ,
                /* right = */ ViewUtils.getWidth(this).toFloat() ,
                /* bottom = */ ViewUtils.getHeight(this).toFloat()
            )
            path.addRoundRect(rect , mRadius!! , mRadius!! , Path.Direction.CW)

            return path
        }

    /** 获取四个圆角度数不统一的路径  */
    private val differentCornersPath : Path
        get() {
            val radius = floatArrayOf(
                mTopLeftRadius , mTopLeftRadius ,
                mTopRightRadius , mTopRightRadius ,
                mBottomRightRadius , mBottomRightRadius ,
                mBottomLeftRadius , mBottomLeftRadius ,
            )

            val path = Path()
            val rect = RectF(
                /* left = */ 0f ,
                /* top = */ 0f ,
                /* right = */ ViewUtils.getWidth(this).toFloat() ,
                /* bottom = */ ViewUtils.getHeight(this).toFloat()
            )
            path.addRoundRect(rect , radius , Path.Direction.CW)

            return path
        }
}
