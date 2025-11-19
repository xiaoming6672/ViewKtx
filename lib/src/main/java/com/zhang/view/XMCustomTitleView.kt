package com.zhang.view

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zhang.library.utils.constant.ViewDirection
import com.zhang.library.utils.context.ResUtils
import com.zhang.library.utils.context.ViewUtils
import com.zhang.view.databinding.ViewCustomTitleBinding

/**
 * 自定义标题栏
 *
 * @author ZhangXiaoMing 2023-08-29 22:46 周二
 */
class XMCustomTitleView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : BaseView<ViewCustomTitleBinding>(context , attrs , defStyleAttr) {


    private var isAutoFitStatusBar = false

    /** 返回图标  */
    private var mBackRes : Drawable? = null

    /** 是否隐藏返回键  */
    private var isHideBack = false

    /** 返回键色调  */
    private var mBackTint : ColorStateList? = null

    /** 标题名  */
    private var mTitleName : String? = null

    /** 标题字色  */
    private var mTitleColor : ColorStateList? = null

    /** 标题字号  */
    private var mTitleSize = 0f

    /** 标题是否加粗  */
    private var isTitleBold = false

    /** 右侧功能图标  */
    private var mFunctionIcon : Drawable? = null

    /** 右侧功能名  */
    private var mFunctionName : String? = null

    /** 右侧功能字色  */
    private var mFunctionColor : ColorStateList? = null

    /** 右侧功能字号  */
    private var mFunctionSize = 0f

    /** 右侧功能偏移量  */
    private var mFunctionOffset = 0f

    /** 后退按钮点击事件  */
    private var mBackOnClickListener : OnClickListener? = null

    /** 标题点击事件  */
    private var mTitleOnClickListener : OnClickListener? = null

    /** 右侧功能点击事件  */
    private var mFunctionOnClickListener : OnClickListener? = null


    init {
        if (isInEditMode) ResUtils.set(context.applicationContext)

        initAttribute(attrs)
        initView()
        initData()
    }


    private fun initAttribute(attrs : AttributeSet?) {
        val array : TypedArray = context.obtainStyledAttributes(attrs , R.styleable.XMCustomTitleView)

        isAutoFitStatusBar = array.getBoolean(R.styleable.XMCustomTitleView_autoFitStatusBar , true)

        mBackRes = array.getDrawable(R.styleable.XMCustomTitleView_backRes)
        isHideBack = array.getBoolean(R.styleable.XMCustomTitleView_hideBack , false)
        mBackTint = array.getColorStateList(R.styleable.XMCustomTitleView_backTint)

        mTitleName = array.getString(R.styleable.XMCustomTitleView_titleName)
        mTitleColor = array.getColorStateList(R.styleable.XMCustomTitleView_titleTextColor)
        mTitleSize = array.getDimension(R.styleable.XMCustomTitleView_titleTextSize , ResUtils.dp2px(18f).toFloat())
        isTitleBold = array.getBoolean(R.styleable.XMCustomTitleView_titleBold , true)

        mFunctionIcon = array.getDrawable(R.styleable.XMCustomTitleView_functionIcon)
        mFunctionName = array.getString(R.styleable.XMCustomTitleView_functionName)
        mFunctionColor = array.getColorStateList(R.styleable.XMCustomTitleView_functionTextColor)
        mFunctionSize = array.getDimension(R.styleable.XMCustomTitleView_functionTextSize , ResUtils.dp2px(14f).toFloat())
        mFunctionOffset = array.getDimension(R.styleable.XMCustomTitleView_functionOffset , ResUtils.dp2px(15f).toFloat())

        array.recycle()
    }

    /** 初始化控件  */
    private fun initView() {
        binding.ivBack.setOnClickListener { v : View? ->
            if (mBackOnClickListener != null) mBackOnClickListener!!.onClick(v)
        }
        binding.tvTitleName.setOnClickListener { v : View? ->
            if (mTitleOnClickListener != null) mTitleOnClickListener!!.onClick(v)
        }
        binding.ivFunctionIcon.setOnClickListener { v : View? ->
            if (mFunctionOnClickListener != null) mFunctionOnClickListener!!.onClick(v)
        }
        binding.tvFunctionName.setOnClickListener { v : View? ->
            if (mFunctionOnClickListener != null) mFunctionOnClickListener!!.onClick(v)
        }
    }

    /** 初始化数据  */
    private fun initData() {
        if (isAutoFitStatusBar) ViewUtils.fitsSystemWindowsByPaddingTop(this)

        if (mBackRes != null) binding.ivBack.setImageDrawable(mBackRes)
        if (mBackTint != null) binding.ivBack.imageTintList = mBackTint
        ViewUtils.setViewVisibleOrGone(binding.ivBack , !isHideBack)

        binding.tvTitleName.text = mTitleName
        if (mTitleColor != null) binding.tvTitleName.setTextColor(mTitleColor)
        if (mTitleSize > 0) binding.tvTitleName.setTextSize(TypedValue.COMPLEX_UNIT_PX , mTitleSize)
        binding.tvTitleName.typeface = if (isTitleBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

        if (mFunctionIcon != null) {
            binding.ivFunctionIcon.visibility = VISIBLE
            binding.tvFunctionName.visibility = GONE

            binding.ivFunctionIcon.setImageDrawable(mFunctionIcon)
        } else if (!TextUtils.isEmpty(mFunctionName)) {
            binding.ivFunctionIcon.visibility = GONE
            binding.tvFunctionName.visibility = VISIBLE

            binding.tvFunctionName.text = mFunctionName
            if (mFunctionColor != null) binding.tvFunctionName.setTextColor(mFunctionColor)
            if (mFunctionSize > 0) binding.tvFunctionName.setTextSize(TypedValue.COMPLEX_UNIT_PX , mFunctionSize)
        } else {
            binding.ivFunctionIcon.visibility = GONE
            binding.tvFunctionName.visibility = GONE
        }

        if (mFunctionOffset > 0) {
            ViewUtils.setMarginValue(binding.ivFunctionIcon , mFunctionOffset.toInt() , ViewDirection.RIGHT)
            ViewUtils.setMarginValue(binding.tvFunctionName , mFunctionOffset.toInt() , ViewDirection.RIGHT)
        }
    }


    /**
     * 设置返回键图标资源
     *
     * @param resId 资源id
     */
    fun setBackRes(@DrawableRes resId : Int) : XMCustomTitleView = apply { binding.ivBack.setImageResource(resId) }

    /**
     * 设置返回键图标
     *
     * @param drawable drawable
     */
    fun setBackRes(drawable : Drawable?) : XMCustomTitleView = apply {
        mBackRes = drawable
        binding.ivBack.setImageDrawable(drawable)
    }

    /**
     * 设置标题名
     *
     * @param resId 资源id
     */
    fun setTitleName(@StringRes resId : Int) : XMCustomTitleView = apply { binding.tvTitleName.setText(resId) }

    /**
     * 设置标题名
     *
     * @param name 名称
     */
    fun setTitleName(name : String?) : XMCustomTitleView = apply {
        this.mTitleName = name
        binding.tvTitleName.text = name
    }

    /**
     * 设置标题字色
     *
     * @param color 颜色
     */
    fun setTitleTextColor(color : Int) : XMCustomTitleView = apply {
        mTitleColor = ColorStateList.valueOf(color)
        binding.tvTitleName.setTextColor(mTitleColor)
    }

    /**
     * 设置标题字色
     *
     * @param color 颜色
     */
    fun setTitleTextColor(color : ColorStateList?) : XMCustomTitleView = apply {
        mTitleColor = color
        binding.tvTitleName.setTextColor(mTitleColor)
    }

    /**
     * 设置标题字号
     *
     * @param px 字号
     */
    fun setTitleTextSize(px : Float) : XMCustomTitleView = apply {
        mTitleSize = px
        binding.tvTitleName.setTextSize(TypedValue.COMPLEX_UNIT_PX , mTitleSize)
    }

    /**
     * 设置标题是否加粗显示
     *
     * @param bold 是否加粗
     */
    fun setTitleBold(bold : Boolean) : XMCustomTitleView = apply {
        isTitleBold = bold
        binding.tvTitleName.typeface = if (isTitleBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }

    /**
     * 设置右侧功能图标资源id
     *
     * @param resId 资源id
     */
    fun setFunctionIcon(@DrawableRes resId : Int) : XMCustomTitleView = apply {
        binding.ivFunctionIcon.setImageResource(resId)

        binding.ivFunctionIcon.visibility = VISIBLE
        binding.tvFunctionName.visibility = GONE
    }

    /**
     * 设置右侧功能图标
     *
     * @param drawable 图标
     */
    fun setFunctionIcon(drawable : Drawable?) : XMCustomTitleView = apply {
        mFunctionIcon = drawable
        binding.ivFunctionIcon.setImageDrawable(drawable)

        binding.ivFunctionIcon.visibility = VISIBLE
        binding.tvFunctionName.visibility = GONE
    }

    /**
     * 设置右侧功能名
     *
     * @param resId 资源id
     */
    fun setFunctionName(@StringRes resId : Int) : XMCustomTitleView = apply {
        binding.tvFunctionName.setText(resId)

        binding.ivFunctionIcon.visibility = GONE
        binding.tvFunctionName.visibility = VISIBLE
    }

    /**
     * 设置右侧功能名
     *
     * @param name 名称
     */
    fun setFunctionName(name : String?) : XMCustomTitleView = apply {
        mFunctionName = name
        binding.tvFunctionName.text = mFunctionName

        binding.ivFunctionIcon.visibility = GONE
        binding.tvFunctionName.visibility = VISIBLE
    }

    /**
     * 设置右侧功能字色
     *
     * @param color 颜色
     */
    fun setFunctionTextColor(color : Int) : XMCustomTitleView = apply {
        mFunctionColor = ColorStateList.valueOf(color)
        binding.tvFunctionName.setTextColor(mFunctionColor)
    }

    /**
     * 设置右侧功能颜色
     *
     * @param color 颜色
     */
    fun setFunctionTextColor(color : ColorStateList?) : XMCustomTitleView = apply {
        mFunctionColor = color
        binding.tvFunctionName.setTextColor(mFunctionColor)
    }

    /**
     * 设置右侧功能字号
     *
     * @param px 字号
     */
    fun setFunctionTextSize(px : Float) : XMCustomTitleView = apply {
        mFunctionSize = px
        binding.tvFunctionName.setTextSize(TypedValue.COMPLEX_UNIT_PX , mFunctionSize)
    }

    /**
     * 设置右侧功能偏移量
     *
     * @param offset 偏移量
     */
    fun setFunctionOffset(offset : Float) : XMCustomTitleView = apply {
        mFunctionOffset = offset
        ViewUtils.setMarginValue(binding.ivFunctionIcon , mFunctionOffset.toInt() , ViewDirection.RIGHT)
        ViewUtils.setMarginValue(binding.tvFunctionName , mFunctionOffset.toInt() , ViewDirection.RIGHT)
    }

    /**
     * 设置返回键点击事件
     *
     * @param listener 监听器
     */
    fun setBackOnClickListener(listener : OnClickListener?) : XMCustomTitleView = apply {
        mBackOnClickListener = listener
    }

    /**
     * 设置标题点击事件
     *
     * @param listener 监听器
     */
    fun setTitleOnClickListener(listener : OnClickListener?) : XMCustomTitleView = apply {
        mTitleOnClickListener = listener
    }

    /**
     * 设置功能按钮点击事件
     *
     * @param listener 监听器
     */
    fun setFunctionOnClickListener(listener : OnClickListener?) : XMCustomTitleView = apply {
        mFunctionOnClickListener = listener
    }
}
