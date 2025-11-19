package com.zhang.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zhang.library.utils.context.ContextUtils
import androidx.recyclerview.R as RecyclerviewR

/**
 * 自定义RecyclerView
 *
 * @author ZhangXiaoMing 2021-01-05 16:07 星期二
 */
class XMRecyclerView @JvmOverloads constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = RecyclerviewR.attr.recyclerViewStyle ,
) : RecyclerView(context , attrs , defStyleAttr) {

    init {
        if (isInEditMode) ContextUtils.set(context.applicationContext)
        init()
    }

    private fun init() {
        if (layoutManager == null) {
            setLinearLayoutManager(VERTICAL)
        }
    }

    //<editor-fold desc="Getter and Setter of LayoutManger">
    /** 获取线性布局管理器  */
    var linearLayoutManager : LinearLayoutManager?
        get() = layoutManager as? LinearLayoutManager
        set(value) {
            layoutManager = value
        }

    /**
     * 设置线性布局管理器
     *
     * @param orientation 布局管理器方向
     *
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    fun setLinearLayoutManager(@Orientation orientation : Int , reverseLayout : Boolean = false) {
        layoutManager = LinearLayoutManager(context , orientation , reverseLayout)
    }

    /** 获取表格布局管理器  */
    var gridLayoutManager : GridLayoutManager?
        get() = layoutManager as? GridLayoutManager
        set(value) {
            layoutManager = value
        }

    /**
     * 设置表格布局管理器
     *
     * @param orientation 布局管理器方向
     * @param spanCount   列数
     *
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    fun setGridLayoutManager(@Orientation orientation : Int , spanCount : Int , reverseLayout : Boolean = false) {
        layoutManager = GridLayoutManager(context , spanCount , orientation , reverseLayout)
    }

    /** 获取瀑布流布局管理器  */
    var staggeredGridLayoutManager : StaggeredGridLayoutManager?
        get() = layoutManager as? StaggeredGridLayoutManager
        set(value) {
            layoutManager = value
        }

    /**
     * 设置瀑布流布局管理器
     *
     * @param orientation 布局管理器方向
     * @param spanCount   列数
     *
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    fun setStaggeredGridLayoutManager(@Orientation orientation : Int , spanCount : Int) {
        layoutManager = StaggeredGridLayoutManager(spanCount , orientation)
    }

    //</editor-fold>

    //<editor-fold desc="ItemDecoration">
    /**
     * 添加透明分割线
     *
     * @param orientation 列表方向
     * @param size        分割线大小
     *
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    fun addTransparentDivider(@Orientation orientation : Int , size : Int) {
        addDivider(orientation , size , Color.TRANSPARENT)
    }

    /**
     * 添加分割线
     *
     * @param orientation 列表方向
     * @param size        分割线大小
     * @param color       分割线颜色
     * @param padding     分割线缩进大小
     *
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    /**
     * 添加分割线
     *
     * @param orientation 列表方向
     * @param size        分割线大小
     * @param color       分割线颜色
     *
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    @JvmOverloads
    fun addDivider(@Orientation orientation : Int , size : Int , color : Int , padding : Int = 0) {
        val decoration = XMEqualDividerItemDecoration(orientation , size , color)
        decoration.setPadding(padding)

        addItemDecoration(decoration)
    }

    /**
     * 添加分割线
     *
     * @param orientation 列表方向
     * @param drawable    分割线图案
     *
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    fun addDivider(@Orientation orientation : Int , drawable : Drawable) {
        val decoration = XMEqualDividerItemDecoration(orientation , drawable)

        addItemDecoration(decoration)
    }

    /**
     * 添加分割线
     *
     * @param orientation 列表方向
     * @param drawable    分割线图案
     * @param padding     分割线缩进大小
     *
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    fun addDivider(@Orientation orientation : Int , drawable : Drawable , padding : Int) {
        val decoration = XMEqualDividerItemDecoration(orientation , drawable)
        decoration.setPadding(padding)

        addItemDecoration(decoration)
    }

    //</editor-fold>
    /** 获取列表中数据数量  */
    fun <VH : ViewHolder?> getItemCount() : Int {
        return if (adapter == null) 0 else adapter!!.itemCount
    }

    /** 滑动到顶部  */
    fun scrollToHeader() {
        scrollToPosition(0)
    }

    /** 滑动到顶部，展现滑动过程  */
    fun smoothScrollToHeader() {
        smoothScrollToPosition(0)
    }

    /** 滑动到底部  */
    fun scrollToFooter() {
        val count = getItemCount<ViewHolder>()

        if (count > 0) scrollToPosition(count - 1)
    }

    /** 滑动到底部，展示滑动过程  */
    fun smoothScrollToFooter() {
        val count = getItemCount<ViewHolder>()

        if (count > 0) smoothScrollToPosition(count - 1)
    }
}
