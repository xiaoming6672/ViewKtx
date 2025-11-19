package com.zhang.view.layout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.RelativeLayout
import com.zhang.library.utils.LogUtils
import com.zhang.library.utils.constant.ViewDirection
import com.zhang.library.utils.context.ContextUtils
import com.zhang.library.utils.context.ResUtils
import com.zhang.library.utils.context.ViewUtils
import com.zhang.view.R

/**
 * 适应状态栏高度的[RelativeLayout]，并且能兼容
 *
 * @author ZhangXiaoMing 2024-04-14 21:49 周日
 */
class XMFitsSystemRelativeLayout @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : RelativeLayout(context , attrs , defStyleAttr) {

    private val mInsets = IntArray(4)

    init {
        if (isInEditMode) ContextUtils.set(context.applicationContext)
    }

    @Deprecated("Deprecated in Java")
    override fun fitSystemWindows(insets : Rect) : Boolean {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        mInsets[0] = insets.left
        mInsets[1] = insets.top
        mInsets[2] = insets.right
        insets.left = 0
        insets.top = 0
        insets.right = 0

        //        }
        @Suppress("DEPRECATION")
        return super.fitSystemWindows(insets)
    }

    @Suppress("DEPRECATION")
    override fun onApplyWindowInsets(insets : WindowInsets) : WindowInsets {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
        mInsets[0] = insets.systemWindowInsetLeft
        LogUtils.info(TAG , "mInsets[0]=" + mInsets[0])

        mInsets[1] = insets.systemWindowInsetTop
        LogUtils.info(TAG , "mInsets[1]" + mInsets[1])

        mInsets[2] = insets.systemWindowInsetRight
        LogUtils.info(TAG , "mInsets[2]" + mInsets[2])

        val bottom = insets.systemWindowInsetBottom
        val replaceSystemWindowInsets = insets.replaceSystemWindowInsets(0 , 0 , 0 , bottom)
        return super.onApplyWindowInsets(replaceSystemWindowInsets)
        //        } else {
//        return super.onApplyWindowInsets(insets);
//        }
    }

    override fun generateDefaultLayoutParams() : LayoutParams {
        return LayoutParams()
    }

    override fun generateLayoutParams(lp : ViewGroup.LayoutParams) : ViewGroup.LayoutParams {
        return LayoutParams(lp)
    }

    override fun generateLayoutParams(attrs : AttributeSet) : LayoutParams {
        return LayoutParams(context , attrs)
    }

    override fun onViewAdded(view : View?) {
        super.onViewAdded(view)

        if (view == null || view.layoutParams !is LayoutParams) return

        val params = view.layoutParams as LayoutParams
        when (params.fitsSystemStyle) {
            FitsSystemStyle.PADDING -> if (!view.fitsSystemWindows) {
                val top = view.paddingTop + ResUtils.getStatusBarHeight()
                view.setPadding(view.paddingLeft , top , view.paddingRight , view.paddingBottom)
            }
            FitsSystemStyle.MARGIN  -> {
                val marginTop = ViewUtils.getMarginValue(view , ViewDirection.TOP)
                val value = marginTop + ResUtils.getStatusBarHeight()
                ViewUtils.setMarginValue(view , value , ViewDirection.TOP)
            }
            FitsSystemStyle.NONE    -> {}
        }
    }


    class LayoutParams : RelativeLayout.LayoutParams {

        @FitsSystemStyle
        internal var fitsSystemStyle : Int = FitsSystemStyle.NONE

        constructor(source : LayoutParams) : super(source) {
            this.fitsSystemStyle = source.fitsSystemStyle
        }

        constructor(c : Context , attrs : AttributeSet?) : super(c , attrs) {
            val array = c.obtainStyledAttributes(attrs , R.styleable.XMFitsSystemRelativeLayout)
            fitsSystemStyle = array.getInteger(R.styleable.XMFitsSystemRelativeLayout_fitsSystemStyle , FitsSystemStyle.NONE)
            array.recycle()
        }

        @JvmOverloads
        constructor(width : Int = WRAP_CONTENT , height : Int = WRAP_CONTENT) : super(width , height)

        constructor(source : ViewGroup.LayoutParams?) : super(source)
    }

    companion object {

        private const val TAG = "XMFitsSystemRelativeLayout"
    }
}
