package ru.tbank.petcare.utils

import android.content.Context
import android.graphics.drawable.Drawable


class ResourceProviderImpl(
    private val context: Context
): ResourceProvider {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }

    override fun getColor(resId: Int): Int {
        return context.getColor(resId)
    }

    override fun getDimension(resId: Int): Float {
        return context.resources.getDimension(resId)
    }

    override fun getDrawable(resId: Int): Drawable? {
        return context.getDrawable(resId)
    }
}