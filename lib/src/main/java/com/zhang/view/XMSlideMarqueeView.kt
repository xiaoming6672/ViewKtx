package com.zhang.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhang.view.XMSlideMarqueeView.AdapterWrapper

/**
 * 跑马灯功能的RecyclerView
 *
 * @author ZhangXiaoMing 2021-06-23 17:01 星期三
 */
class XMSlideMarqueeView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = androidx.recyclerview.R.attr.recyclerViewStyle ,
) :
    RecyclerView(context , attrs , defStyleAttr) ,
    Runnable {

    private var mAdapterWrapper : AdapterWrapper<*>? = null


    /** 获取滑动距离  */
    /** 设置滑动距离  */
    /** 每次滚动的距离  */
    var scrollDistance : Int = 0
    /** 获取滑动方向  */
    /** 设置滑动方向  */
    /** 跑马灯滚动方向  */
    @get:Orientation
    @Orientation
    var scrollOrientation : Int = 0
    /** 获取滚动延时  */
    /** 跑马灯滑动间隔时间  */
    var scrollTimeMillis : Int = 0

    /**
     * 设置是否支持滑动
     *
     * @param touchSupported **true:**不阻塞Touch事件，允许滑动
     * <br></br>**false:**阻塞Touch事件，不允许滑动
     */
    /** 是否支持滑动  */
    var isTouchSupported : Boolean = false

    /** 正在运行  */
    var isRunning : Boolean = false
        private set

    init {
        init(attrs)
    }

    override fun performClick() : Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(e : MotionEvent) : Boolean {
        if (isTouchSupported) return super.onTouchEvent(e)

        performClick()
        return true
    }

    override fun setAdapter(adapter : Adapter<*>?) {
//        super.setAdapter(adapter)
        adapterWrapper.setAdapter(adapter)
    }

    override fun getAdapter() : Adapter<*>? {
        return adapterWrapper.adapter
    }

    override fun onDetachedFromWindow() {
        stop()

        super.onDetachedFromWindow()
    }

    private fun init(attrs : AttributeSet?) {
        if (attrs == null) {
            scrollDistance = 1
            scrollOrientation = HORIZONTAL
            scrollTimeMillis = 10
        } else {
            val typedArray = context.obtainStyledAttributes(attrs , R.styleable.XMSlideMarqueeView)

            scrollDistance = typedArray.getInteger(R.styleable.XMSlideMarqueeView_slide_distance , 1)
            scrollOrientation = typedArray.getInt(R.styleable.XMSlideMarqueeView_slide_orientation , HORIZONTAL)
            scrollTimeMillis = typedArray.getInteger(R.styleable.XMSlideMarqueeView_slide_timemillis , 10)

            typedArray.recycle()
        }

        super.setAdapter(adapterWrapper)
    }

    /** 通知适配器刷新数据  */
    fun notifyDataSetChanged() {
        adapterWrapper.notifyDataSetChanged()
    }

    /** 开始滑动  */
    fun startDelay() {
        start(scrollTimeMillis.toLong())
    }

    /**
     * 开始滑动
     *
     * @param delay 延时开始时间
     */
    /** 开始滑动  */
    @JvmOverloads
    fun start(delay : Long = 0) {
        if (adapter == null || adapter!!.itemCount == 0) return

        stop()

        try {
            isRunning = true
            postDelayed(this , delay)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    /** 结束滑动  */
    fun stop() {
        try {
            isRunning = false
            removeCallbacks(this)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private val adapterWrapper : AdapterWrapper<*>
        /** 适配器  */
        get() {
            if (mAdapterWrapper == null) {
                mAdapterWrapper = AdapterWrapper<ViewHolder>()
            }

            return mAdapterWrapper!!
        }

    override fun run() {
        if (!isRunning) return

        val orientation = scrollOrientation
        if (orientation == HORIZONTAL) scrollBy(scrollDistance , 0)
        else scrollBy(0 , scrollDistance)

        postDelayed(this , scrollTimeMillis.toLong())
    }


    /** 包裹层适配器  */
    private class AdapterWrapper<VH : ViewHolder> : Adapter<VH>() {

        private var mAdapter : Adapter<VH>? = null

        val adapter : Adapter<VH>?
            get() = mAdapter

        @SuppressLint("NotifyDataSetChanged")
        fun setAdapter(adapter : Adapter<*>?) {
            if (mAdapter == adapter) return
            if (mAdapter != null) mAdapter!!.unregisterAdapterDataObserver(mObserver)

            @Suppress("UNCHECKED_CAST")
            this.mAdapter = adapter as? Adapter<VH>

            mAdapter?.registerAdapterDataObserver(mObserver)

            notifyDataSetChanged()
        }

        /** 获取真实的位置  */
        fun getRealPosition(position : Int) : Int {
            if (mAdapter == null || mAdapter!!.itemCount == 0) return position

            val itemCount = mAdapter!!.itemCount
            return position % itemCount
        }

        override fun getItemViewType(position : Int) : Int {
            return mAdapter!!.getItemViewType(getRealPosition(position))
        }

        override fun getItemCount() : Int {
            if (mAdapter == null || mAdapter!!.itemCount == 0) return 0

            val itemCount = mAdapter!!.itemCount
            return if (itemCount == 1) 1 else Int.MAX_VALUE
        }

        override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : VH {
            return mAdapter!!.onCreateViewHolder(parent , viewType)
        }

        override fun onBindViewHolder(holder : VH , position : Int) {
            mAdapter!!.onBindViewHolder(holder , getRealPosition(position))
        }

        override fun onViewAttachedToWindow(holder : VH) {
            super.onViewAttachedToWindow(holder)

            if (mAdapter != null) mAdapter!!.onViewAttachedToWindow(holder)
        }

        override fun onViewDetachedFromWindow(holder : VH) {
            super.onViewDetachedFromWindow(holder)

            if (mAdapter != null) mAdapter!!.onViewDetachedFromWindow(holder)
        }

        override fun onAttachedToRecyclerView(recyclerView : RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)

            if (mAdapter != null) mAdapter!!.onAttachedToRecyclerView(recyclerView)
        }

        override fun onDetachedFromRecyclerView(recyclerView : RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)

            if (mAdapter != null) mAdapter!!.onDetachedFromRecyclerView(recyclerView)
        }

        private val mObserver : AdapterDataObserver = object : AdapterDataObserver() {
            override fun onChanged() {
                Log.i(TAG , "registerAdapterDataObserver>>>onChanged()")
                super.onChanged()

                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart : Int , itemCount : Int) {
                Log.i(TAG , "registerAdapterDataObserver>>>onItemRangeChanged()")
                super.onItemRangeChanged(positionStart , itemCount)

                notifyItemRangeChanged(positionStart , itemCount)
            }

            override fun onItemRangeChanged(positionStart : Int , itemCount : Int , payload : Any?) {
                Log.i(TAG , "registerAdapterDataObserver>>>onItemRangeChanged()")
                super.onItemRangeChanged(positionStart , itemCount , payload)

                notifyItemRangeChanged(positionStart , itemCount , payload)
            }

            override fun onItemRangeInserted(positionStart : Int , itemCount : Int) {
                Log.i(TAG , "registerAdapterDataObserver>>>onItemRangeInserted()")
                super.onItemRangeInserted(positionStart , itemCount)

                notifyItemRangeInserted(positionStart , itemCount)
            }

            override fun onItemRangeRemoved(positionStart : Int , itemCount : Int) {
                Log.i(TAG , "registerAdapterDataObserver>>>onItemRangeRemoved()")
                super.onItemRangeRemoved(positionStart , itemCount)

                notifyItemRangeRemoved(positionStart , itemCount)
            }

            override fun onItemRangeMoved(fromPosition : Int , toPosition : Int , itemCount : Int) {
                Log.i(TAG , "registerAdapterDataObserver>>>onItemRangeMoved()")
                super.onItemRangeMoved(fromPosition , toPosition , itemCount)

                notifyItemMoved(fromPosition , toPosition)
            }

            override fun onStateRestorationPolicyChanged() {
                Log.i(TAG , "registerAdapterDataObserver>>>onStateRestorationPolicyChanged()")
                super.onStateRestorationPolicyChanged()

                notifyDataSetChanged()
            }
        }

        companion object {

            private val TAG : String = AdapterWrapper::class.java.simpleName
        }
    }
}
