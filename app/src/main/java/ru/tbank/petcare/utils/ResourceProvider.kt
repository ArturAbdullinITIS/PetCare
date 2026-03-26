package ru.tbank.petcare.utils

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg args: Any): String
    fun getColor(@ColorRes resId: Int): Int
    fun getDimension(@DimenRes resId: Int): Float
    fun getDrawable(@DrawableRes resId: Int): Drawable?
}