package com.zhang.view.layout

import androidx.annotation.IntDef

/**
 * 适配系统状态栏风格
 *
 * @author ZhangXiaoMing 2024-04-14 22:10 周日
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    FitsSystemStyle.NONE ,
    FitsSystemStyle.PADDING ,
    FitsSystemStyle.MARGIN
)
annotation class FitsSystemStyle {

    companion object {

        const val NONE : Int = 0
        const val PADDING : Int = 1
        const val MARGIN : Int = 2
    }
}
