package ru.kpfu.itis.carwash.common

interface ResourceManager {

    fun getString(id: Int): String

    fun getString(res: Int, vararg arguments: Any): String
}
