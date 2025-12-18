package com.zhang.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.zhang.lib.ktx.common.ifTrue
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
        context.obtainStyledAttributes(attrs , R.styleable.XMRoundedImageView).apply {
            hasValue(R.styleable.XMRoundedImageView_radius).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radius , 0F)) {
                    mTopLeftRadius = this
                    mTopRightRadius = this
                    mBottomLeftRadius = this
                    mBottomRightRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusTopPart).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radiusTopPart , 0F)) {
                    mTopLeftRadius = this
                    mTopRightRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusBottomPart).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radiusBottomPart , 0F)) {
                    mBottomLeftRadius = this
                    mBottomRightRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusLeftPart).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radiusLeftPart , 0F)) {
                    mTopLeftRadius = this
                    mBottomLeftRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusRightPart).ifTrue {
                with(getDimension(R.styleable.XMRoundedImageView_radiusRightPart , 0F)) {
                    mTopRightRadius = this
                    mBottomRightRadius = this
                }
            }

            hasValue(R.styleable.XMRoundedImageView_radiusTopLeft).ifTrue {
                mTopLeftRadius = getDimension(R.styleable.XMRoundedImageView_radiusTopLeft , 0F)
            }

            hasValue(R.styleable.XMRoundedImageView_radiusTopRight).ifTrue {
                mTopRightRadius = getDimension(R.styleable.XMRoundedImageView_radiusTopRight , 0F)
            }

            hasValue(R.styleable.XMRoundedImageView_radiusBottomLeft).ifTrue {
                mBottomLeftRadius = getDimension(R.styleable.XMRoundedImageView_radiusBottomLeft , 0F)
            }

            hasValue(R.styleable.XMRoundedImageView_radiusBottomRight).ifTrue {
                mBottomRightRadius = getDimension(R.styleable.XMRoundedImageView_radiusBottomRight , 0F)
            }

            recycle()
        }
    }

    override fun onDraw(canvas : Canvas) {
        canvas.clipPath(roundedPath)
        super.onDraw(canvas)
    }

    private val roundedPath : Path
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
