package ru.kpfu.itis.data.storage

import android.content.SharedPreferences

class ProfileSharedPreferencesImpl(
    private val sharedPreferences: SharedPreferences,
) : ProfileSharedPreferences {

    override var email: String?
        get() = sharedPreferences.getString("email", null)
        set(value) = sharedPreferences.edit().putString("email", value).apply()

    override var password: String?
        get() = sharedPreferences.getString("password", null)
        set(value) = sharedPreferences.edit().putString("password", value).apply()

    override var carNumber: String?
        get() = sharedPreferences.getString("carNumber", null)
        set(value) = sharedPreferences.edit().putString("carNumber", value).apply()

    override var carWashDate: String?
        get() = sharedPreferences.getString("carWashDate", null)
        set(value) = sharedPreferences.edit().putString("carWashDate", value).apply()

    override var city: String?
        get() = sharedPreferences.getString("city", null)
        set(value) = sharedPreferences.edit().putString("city", value).apply()

    override var lat: String?
        get() = sharedPreferences.getString("lat", null)
        set(value) = sharedPreferences.edit().putString("lat", value).apply()

    override var lon: String?
        get() = sharedPreferences.getString("lon", null)
        set(value) = sharedPreferences.edit().putString("lon", value).apply()
}
