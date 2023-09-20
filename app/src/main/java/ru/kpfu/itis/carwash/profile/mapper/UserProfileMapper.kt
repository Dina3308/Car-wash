package ru.kpfu.itis.carwash.profile.mapper

import ru.kpfu.itis.carwash.profile.model.UserProfile
import ru.kpfu.itis.domain.model.UserEntity

fun mapUserEntityToUserProfile(user: UserEntity): UserProfile {
    return with(user) {
        UserProfile(
            address,
            location,
            date,
            levelOfCarPollution
        )
    }
}
