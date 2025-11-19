package com.zhang.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * BaseView
 *
 * @author ZhangXiaoMing 2025-11-19 14:09 周三
 */
open class BaseView<T : ViewBinding> @JvmOverloads internal constructor(
    context : Context ,
    attrs : AttributeSet? = null ,
    defStyleAttr : Int = 0 ,
) : ConstraintLayout(context , attrs , defStyleAttr) {


    private val bindingInner : T = initViewBinding()
    val binding : T get() = bindingInner


    /** [View]创建[ViewBinding]对象 */
    private fun <T : ViewBinding> initViewBinding() : T {
        var genericSuperclass = javaClass.genericSuperclass
        var superclass = javaClass.superclass

        while (superclass != null) {
            (genericSuperclass as? ParameterizedType)?.actualTypeArguments?.forEach { type ->
                val clazz = type as? Class<*> ?: return@forEach
                val result = clazz.declaredMethods.find {
                    val requiredParameters =
                        arrayOf(LayoutInflater::class.java , ViewGroup::class.java , Boolean::class.javaPrimitiveType)
                    it.name == "inflate" && it.parameterTypes.contentEquals(requiredParameters) && it.returnType == type
                }?.invoke(null , LayoutInflater.from(context) , this , true) as? T
                return result ?: return@forEach
            }

            genericSuperclass = superclass.genericSuperclass
            superclass = superclass.superclass
        }

        error(("${javaClass.name}.initViewBinding对象创建失败"))
    }
}
