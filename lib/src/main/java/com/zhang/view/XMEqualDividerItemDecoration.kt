package com.zhang.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zhang.library.utils.context.ResUtils

/**
 * 均等分割线
 *
 * @author ZhangXiaoMing 2020-11-27 10:28 星期五
 */
open class XMEqualDividerItemDecoration : RecyclerView.ItemDecoration {

    /** 获取分割线在列表中的方向  */
    /** 分割线方向，如果设置的是[RecyclerView.VERTICAL]，则表示在列表竖直方向话分割线，即分割线是横向的；反之是竖向的  */
    var orientation : Int = RecyclerView.VERTICAL
        private set
    /** 获取分割线大小  */
    /** 分割间距，单位：px  */
    var size : Int
        private set

    /** 左边缩进，[.mOrientation]为[RecyclerView.VERTICAL]的时候生效  */
    private var mPaddingLeft = 0

    /** 右边缩进，[.mOrientation]为[RecyclerView.VERTICAL]的时候生效  */
    private var mPaddingRight = 0

    /** 顶部缩进，[.mOrientation]为[RecyclerView.HORIZONTAL]的时候生效  */
    private var mPaddingTop = 0

    /** 底部缩进，[.mOrientation]为[RecyclerView.HORIZONTAL]的时候生效  */
    private var mPaddingBottom = 0

    /** 分割线Drawable  */
    private var mDivider : Drawable?

    private val mBounds = Rect()


    @JvmOverloads
    constructor(@RecyclerView.Orientation orientation : Int = RecyclerView.VERTICAL , size : Int = ResUtils.dp2px(1F)) {
        checkOrientation(orientation)

        this.orientation = orientation
        this.size = size
        mDivider = ColorDrawable(Color.TRANSPARENT)
    }

    constructor(@RecyclerView.Orientation orientation : Int , drawable : Drawable) {
        checkOrientation(orientation)
        checkDrawable(drawable)

        this.orientation = orientation
        this.mDivider = drawable

        this.size = if (orientation == RecyclerView.VERTICAL) mDivider!!.intrinsicHeight else mDivider!!.intrinsicWidth
    }

    constructor(@RecyclerView.Orientation orientation : Int , size : Int , color : Int) {
        checkOrientation(orientation)

        this.orientation = orientation
        this.size = size
        mDivider = ColorDrawable(color)
    }

    //<editor-fold desc="设置、获取属性">
    fun setOrientation(@RecyclerView.Orientation orientation : Int) : XMEqualDividerItemDecoration {
        checkOrientation(orientation)

        this.orientation = orientation
        return this
    }

    /** 设置分割线图案  */
    fun setDrawable(drawable : Drawable) : XMEqualDividerItemDecoration {
        checkDrawable(drawable)

        this.mDivider = drawable
        this.size = mDivider!!.intrinsicWidth
        return this
    }

    /** 设置分割线大小  */
    fun setSize(size : Int) : XMEqualDividerItemDecoration {
        this.size = size
        return this
    }

    /** 设置分割线缩进大小  */
    fun setPadding(padding : Int) : XMEqualDividerItemDecoration {
        if (orientation == RecyclerView.VERTICAL) {
            this.mPaddingLeft = padding
            this.mPaddingRight = padding
        } else {
            this.mPaddingTop = padding
            this.mPaddingBottom = padding
        }
        return this
    }

    /** 设置分割线左缩进大小  */
    fun setPaddingLeft(paddingLeft : Int) : XMEqualDividerItemDecoration {
        this.mPaddingLeft = paddingLeft
        return this
    }

    /** 设置分割线右缩进大小  */
    fun setPaddingRight(paddingRight : Int) : XMEqualDividerItemDecoration {
        this.mPaddingRight = paddingRight
        return this
    }

    /** 设置分割线上缩进大小  */
    fun setPaddingTop(paddingTop : Int) : XMEqualDividerItemDecoration {
        this.mPaddingTop = paddingTop
        return this
    }

    /** 设置分割线下缩进大小  */
    fun setPaddingBottom(paddingBottom : Int) : XMEqualDividerItemDecoration {
        this.mPaddingBottom = paddingBottom
        return this
    }

    /** 设置分割线颜色  */
    fun setColor(color : Int) {
        mDivider = ColorDrawable(color)
    }

    //</editor-fold>
    private fun checkOrientation(orientation : Int) {
        if (orientation == RecyclerView.VERTICAL || orientation == RecyclerView.HORIZONTAL) return
        throw IllegalArgumentException("Orientation must be RecyclerView.HORIZONTAL or RecyclerView.VERTICAL")
    }

    private fun checkDrawable(drawable : Drawable) {
        requireNotNull(drawable) { "Drawable cannot be null." }
    }

    override fun onDraw(c : Canvas , parent : RecyclerView , state : RecyclerView.State) {
//        super.onDraw(c, parent, state);
        if (parent.layoutManager == null || mDivider == null) return

        if (orientation == RecyclerView.VERTICAL) {
            drawVertical(c , parent)
        } else {
            drawHorizontal(c , parent)
        }
    }

    override fun getItemOffsets(outRect : Rect , view : View , parent : RecyclerView , state : RecyclerView.State) {
        super.getItemOffsets(outRect , view , parent , state)

        val position = parent.getChildAdapterPosition(view)

        if (isLinearLayoutManager(parent)) {
            getLinearItemOffsets(outRect , parent , position)
        } else if (isGridLayoutManager(parent)) {
            getGridItemOffsets(outRect , parent , position)
        }
    }

    /** 是否是线性布局  */
    protected fun isLinearLayoutManager(parent : RecyclerView) : Boolean {
        return (parent.layoutManager != null && parent.layoutManager is LinearLayoutManager)
                && !isGridLayoutManager(parent)
    }

    /** 是否是表格布局  */
    protected fun isGridLayoutManager(parent : RecyclerView) : Boolean {
        return parent.layoutManager != null && parent.layoutManager is GridLayoutManager
    }

    /** 是否是瀑布流布局  */
    protected fun isStaggeredGridLayoutManager(parent : RecyclerView) : Boolean {
        return parent.layoutManager != null && parent.layoutManager is StaggeredGridLayoutManager
    }

    /**
     * 判断当前item是否是第一行
     *
     * @param parent   RecyclerView列表
     * @param position 当前item位置
     */
    protected fun isFirstRow(parent : RecyclerView , position : Int) : Boolean {
        if (parent.layoutManager == null) return false

        if (isGridLayoutManager(parent)) {
            val manager = parent.layoutManager as GridLayoutManager?
            val spanCount = manager!!.spanCount

            return if (manager.orientation == RecyclerView.VERTICAL) position < spanCount
            else position % spanCount == 0
        } else if (isLinearLayoutManager(parent)) {
            val manager = parent.layoutManager as LinearLayoutManager?

            return manager!!.orientation == RecyclerView.HORIZONTAL || position == 0
        }

        return false
    }

    /**
     * 判断当前item是否是最后一行
     *
     * @param parent   RecyclerView列表
     * @param position 当前item位置
     */
    protected fun isLastRow(parent : RecyclerView , position : Int) : Boolean {
        if (parent.layoutManager == null) return false

        val childCount = if (parent.adapter == null) 0 else parent.adapter!!.itemCount

        if (isGridLayoutManager(parent)) {
            val manager = parent.layoutManager as GridLayoutManager?
            val spanCount = manager!!.spanCount

            if (manager.orientation == RecyclerView.VERTICAL) {
                val result = childCount % spanCount
                return if (result == 0) position >= childCount - spanCount
                else position >= childCount - result
            } else {
                return (position + 1) % spanCount == 0
            }
        } else if (isLinearLayoutManager(parent)) {
            val manager = parent.layoutManager as LinearLayoutManager?

            return manager!!.orientation == RecyclerView.HORIZONTAL //横向
                    || position == childCount - 1 //纵向
        }

        return false
    }

    /**
     * 判断当前位置的item是否是第一列
     *
     * @param parent   RecyclerView列表
     * @param position 当前item的位置
     */
    protected fun isFirstColumn(parent : RecyclerView , position : Int) : Boolean {
        if (parent.layoutManager == null) return false

        if (isGridLayoutManager(parent)) {
            val manager = parent.layoutManager as GridLayoutManager?
            val spanCount = manager!!.spanCount

            return if (manager.orientation == RecyclerView.VERTICAL) position % spanCount == 0 else position < spanCount
        } else if (isLinearLayoutManager(parent)) {
            val manager = parent.layoutManager as LinearLayoutManager?

            return manager!!.orientation == RecyclerView.VERTICAL || position == 0
        }

        return false
    }

    /**
     * 判断当前位置的item是否是最后一列
     *
     * @param parent   RecyclerView列表
     * @param position 当前item的位置
     */
    protected fun isLastColumn(parent : RecyclerView , position : Int) : Boolean {
        if (parent.layoutManager == null) return false

        val childCount = if (parent.adapter == null) 0 else parent.adapter!!.itemCount

        if (isGridLayoutManager(parent)) {
            val manager = parent.layoutManager as GridLayoutManager?
            val spanCount = manager!!.spanCount

            if (manager.orientation == RecyclerView.VERTICAL) {
                return (position + 1) % spanCount == 0
            } else {
                val result = childCount % spanCount
                return if (result == 0) position >= childCount - spanCount else position >= childCount - result
            }
        } else if (isLinearLayoutManager(parent)) {
            val manager = parent.layoutManager as LinearLayoutManager?

            return manager!!.orientation == RecyclerView.VERTICAL || position == childCount - 1
        }

        return false
    }

    /**
     * 获取当前item所在第几列
     *
     * @param parent   RecyclerView
     * @param position 当前item的位置
     */
    private fun getColumnCount(parent : RecyclerView , position : Int) : Int {
        if (parent.layoutManager == null) return 0

        if (isLinearLayoutManager(parent)) {
            val manager = parent.layoutManager as LinearLayoutManager?
            return if (manager!!.orientation == RecyclerView.VERTICAL) 1 else position
        } else if (isGridLayoutManager(parent)) {
            val manager = parent.layoutManager as GridLayoutManager?
            val spanCount = manager!!.spanCount

            return if (manager.orientation == RecyclerView.VERTICAL) {
                position % spanCount + 1
            } else {
                position / spanCount + 1
            }
        }

        return 0
    }

    /**
     * 获取当前item所在第几行
     *
     * @param parent   RecyclerView
     * @param position 当前item的位置
     */
    private fun getRowCount(parent : RecyclerView , position : Int) : Int {
        if (parent.layoutManager == null) return 0

        if (isLinearLayoutManager(parent)) {
            val manager = parent.layoutManager as LinearLayoutManager?

            return if (manager!!.orientation == RecyclerView.VERTICAL) position else 1
        } else if (isGridLayoutManager(parent)) {
            val manager = parent.layoutManager as GridLayoutManager?
            val spanCount = manager!!.spanCount

            return if (manager.orientation == RecyclerView.VERTICAL) {
                position / spanCount + 1
            } else {
                position % spanCount + 1
            }
        }

        return 0
    }

    /** 绘制垂直方向上的颜色  */
    private fun drawVertical(canvas : Canvas , parent : RecyclerView) {
        if (isLinearLayoutManager(parent)) {
            drawLinearVertical(canvas , parent)
        } else if (isGridLayoutManager(parent)) {
            drawGridVertical(canvas , parent)
        } else if (isStaggeredGridLayoutManager(parent)) {
            drawStaggeredVertical(canvas , parent)
        }
    }

    /** 绘制水平方向上的颜色  */
    private fun drawHorizontal(canvas : Canvas , parent : RecyclerView) {
        if (isLinearLayoutManager(parent)) {
            drawLinearHorizontal(canvas , parent)
        } else if (isGridLayoutManager(parent)) {
            drawGridHorizontal(canvas , parent)
        } else if (isStaggeredGridLayoutManager(parent)) {
            drawStaggeredHorizontal(canvas , parent)
        }
    }

    /** 绘制线性布局垂直方向上的颜色  */
    private fun drawLinearVertical(canvas : Canvas , parent : RecyclerView) {
        canvas.save()

        val left : Int
        val right : Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left , parent.paddingTop , right ,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)

            val position = parent.getChildAdapterPosition(child)
            if (isFirstRow(parent , position)) {
                continue
            }

            parent.getDecoratedBoundsWithMargins(child , mBounds)

            val top = mBounds.top + Math.round(child.translationY)
            val bottom = top + size
            mDivider!!.setBounds(left + mPaddingLeft , top , right - mPaddingRight , bottom)
            mDivider!!.draw(canvas)
        }
        canvas.restore()
    }

    /** 绘制表格布局垂直方向上的颜色  */
    private fun drawGridVertical(canvas : Canvas , parent : RecyclerView) {
        canvas.save()

        val left : Int
        val right : Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left , parent.paddingTop , right ,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            val position = parent.getChildAdapterPosition(child)

            parent.getDecoratedBoundsWithMargins(child , mBounds)

            val size = IntArray(2)
            getGridHorizontalSideWidth(parent , position , size)

            val topPartHeight = size[0]
            val bottomPartHeight = size[1]

            run {
                val top = mBounds.top + Math.round(child.translationY)
                val bottom = top + topPartHeight
                mDivider!!.setBounds(left + mPaddingLeft , top , right - mPaddingRight , bottom)
                mDivider!!.draw(canvas)
            }

            run {
                val bottom = mBounds.bottom + Math.round(child.translationY)
                val top = bottom - bottomPartHeight
                mDivider!!.setBounds(left + mPaddingLeft , top , right - mPaddingRight , bottom)
                mDivider!!.draw(canvas)
            }
        }
        canvas.restore()
    }

    /** 绘制瀑布流垂直方向上的颜色  */
    private fun drawStaggeredVertical(canvas : Canvas , parent : RecyclerView) {
    }

    /** 绘制线性布局水平方向上的颜色  */
    private fun drawLinearHorizontal(canvas : Canvas , parent : RecyclerView) {
        canvas.save()

        val top : Int
        val bottom : Int

        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft , top , parent.width - parent.paddingRight ,
                bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }

        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)
            val position = parent.getChildAdapterPosition(child)

            if (isFirstColumn(parent , position)) {
                continue
            }

            parent.getDecoratedBoundsWithMargins(child , mBounds)

            val left = mBounds.left + Math.round(child.translationX)
            val right = left + size
            mDivider!!.setBounds(left , top + mPaddingTop , right , bottom - mPaddingBottom)
            mDivider!!.draw(canvas)
        }
        canvas.restore()
    }

    /** 绘制表格布局水平方向上的颜色  */
    private fun drawGridHorizontal(canvas : Canvas , parent : RecyclerView) {
        canvas.save()

        val top : Int
        val bottom : Int

        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft , top , parent.width - parent.paddingRight ,
                bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }

        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            val position = parent.getChildAdapterPosition(child)

            parent.layoutManager!!.getDecoratedBoundsWithMargins(child , mBounds)

            val size = IntArray(2)
            getGridVerticalSideWidth(parent , position , size)

            val leftPartWidth = size[0]
            val rightPartWidth = size[1]

            run {
                //item左侧
                val left = mBounds.left + Math.round(child.translationX)
                val right = left + leftPartWidth
                mDivider!!.setBounds(left , top + mPaddingTop , right , bottom - mPaddingBottom)
                mDivider!!.draw(canvas)
            }

            run {
                //item右侧
                val right = mBounds.right + Math.round(child.translationX)
                val left = right - rightPartWidth
                mDivider!!.setBounds(left , top + mPaddingTop , right , bottom - mPaddingBottom)
                mDivider!!.draw(canvas)
            }
        }
    }

    /** 绘制瀑布流布局水平方向上的颜色  */
    private fun drawStaggeredHorizontal(canvas : Canvas , parent : RecyclerView) {
    }

    /**
     * 计算线性布局的偏移量
     *
     * @param position item所在的位置
     */
    private fun getLinearItemOffsets(rect : Rect , parent : RecyclerView , position : Int) {
        if (parent.layoutManager == null) return

        if (orientation != (parent.layoutManager as LinearLayoutManager).orientation) {
            return
        }

        if ((isFirstColumn(parent , position) && orientation == RecyclerView.HORIZONTAL)
            || (isFirstRow(parent , position) && orientation == RecyclerView.VERTICAL)
        ) {
            rect[0 , 0 , 0] = 0
            return
        }

        if (orientation == RecyclerView.VERTICAL) rect[0 , size , 0] = 0
        else rect[size , 0 , 0] = 0
    }

    /**
     * 计算表格布局的偏移量
     *
     * @param position item所在的位置
     */
    private fun getGridItemOffsets(rect : Rect , parent : RecyclerView , position : Int) {
        if (parent.layoutManager == null) return

        val manager = parent.layoutManager as GridLayoutManager?

        if (manager!!.orientation == RecyclerView.VERTICAL) {
            if (orientation == RecyclerView.VERTICAL) {
                if (!isFirstRow(parent , position)) {
                    rect[0 , size , 0] = 0
                }
            } else {
                val size = IntArray(2)
                getGridVerticalSideWidth(parent , position , size)
                rect[size[0] , 0 , size[1]] = 0
            }
        } else {
            if (orientation == RecyclerView.HORIZONTAL) {
                if (!isFirstColumn(parent , position)) rect[size , 0 , 0] = 0
            } else {
                val size = IntArray(2)
                getGridHorizontalSideWidth(parent , position , size)
                rect[0 , size[0] , 0] = size[1]
            }
        }
    }

    /**
     * 获取垂直表格列表column左右两边添加分割线时候的大小
     *
     * @param parent   列表
     * @param position item所在位置
     * @param size     存放结果的数组
     */
    private fun getGridVerticalSideWidth(parent : RecyclerView , position : Int , size : IntArray) {
        if (parent.layoutManager == null || !isGridLayoutManager(parent) || size.size < 2) {
            return
        }

        val manager = parent.layoutManager as GridLayoutManager?
        if (manager!!.orientation == RecyclerView.HORIZONTAL) {
            if (isFirstColumn(parent , position)) {
                size[1] = 0
                size[0] = size[1]
            } else {
                size[0] = this.size
                size[1] = 0
            }
            return
        }

        val spanCount = manager.spanCount
        val column = getColumnCount(parent , position)
        val left = (column - 1) * this.size / spanCount
        val right = this.size - column * this.size / spanCount

        size[0] = left
        size[1] = right
    }

    /**
     * 获取水平表格列表row上下两边添加分割线时候的大小
     *
     * @param parent   列表
     * @param position item所在位置
     * @param size     存放结果的数组
     */
    private fun getGridHorizontalSideWidth(parent : RecyclerView , position : Int , size : IntArray) {
        if (parent.layoutManager == null || !isGridLayoutManager(parent) || size.size != 2) {
            return
        }

        val manager = parent.layoutManager as GridLayoutManager?
        if (manager!!.orientation == RecyclerView.VERTICAL) {
            if (isFirstRow(parent , position)) {
                size[1] = 0
                size[0] = size[1]
            } else {
                size[0] = this.size
                size[1] = 0
            }
            return
        }

        val spanCount = manager.spanCount
        val row = getRowCount(parent , position)
        val top = (row - 1) * this.size / spanCount
        val bottom = this.size - row * this.size / spanCount

        size[0] = top
        size[1] = bottom
    }
}
