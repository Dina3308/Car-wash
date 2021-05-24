package ru.kpfu.itis.carwash.common

import android.content.Context

class ResourceManagerImpl(
    val context: Context
) : ResourceManager {

    override fun getString(id: Int): String {
        return context.getString(id)
    }

    override fun getString(res: Int, vararg arguments: Any): String {
        return context.getString(res, *arguments)
    }
}
