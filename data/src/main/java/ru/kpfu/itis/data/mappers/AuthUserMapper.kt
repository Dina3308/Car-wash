package ru.kpfu.itis.data.mappers

import com.google.firebase.auth.FirebaseUser
import ru.kpfu.itis.domain.model.AuthUser

fun mapFireBaseUserToAuthUser(user: FirebaseUser): AuthUser {
    return with(user) {
        AuthUser(uid)
    }
}
