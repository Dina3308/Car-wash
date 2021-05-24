package ru.kpfu.itis.carwash.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.launch
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.auth.model.LoginForm
import ru.kpfu.itis.carwash.auth.model.RegisterForm
import ru.kpfu.itis.carwash.common.Event
import ru.kpfu.itis.carwash.common.ResourceManager
import ru.kpfu.itis.domain.AuthInteractor
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor,
    private val resourceManager: ResourceManager
) : ViewModel() {

    companion object {
        private const val MATCH_EMAIL = "^[A-Za-z0-9._%+-]+@([A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,6}\$"
    }

    private val register: MutableLiveData<Event<Unit>> = MutableLiveData()
    private val login: MutableLiveData<Event<Unit>> = MutableLiveData()
    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val showErrorEvent: MutableLiveData<Event<String>> = MutableLiveData()

    fun register(): LiveData<Event<Unit>> = register
    fun login(): LiveData<Event<Unit>> = login
    fun progress(): LiveData<Boolean> = progress
    fun showErrorEvent(): LiveData<Event<String>> = showErrorEvent

    fun register(form: RegisterForm, place: Place?) {
        viewModelScope.launch {
            if (isRegisterFormValid(form) && place != null) {
                progress.value = true
                val currentUser = interactor.register(form.email, form.password, place)
                if (currentUser.isSuccess) {
                    register.value = Event(Unit)
                } else {
                    currentUser.exceptionOrNull()?.let {
                        when (it) {
                            is FirebaseAuthException -> showErrorEvent.value = Event(resourceManager.getString(R.string.email_is_exists))
                            else -> showErrorEvent.value = Event(resourceManager.getString(R.string.no_interner))
                        }
                    }
                }
                progress.value = false
            }
        }
    }

    fun login(form: LoginForm) {
        viewModelScope.launch {
            if (isLoginFormValid(form)) {
                progress.value = true
                val result = interactor.login(form.email, form.password)
                if (result.isSuccess) {
                    login.value = Event(Unit)
                } else {
                    result.exceptionOrNull()?.let {
                        when (it) {
                            is FirebaseAuthException -> showErrorEvent.value = Event(resourceManager.getString(R.string.sign_in_err))
                            else -> showErrorEvent.value = Event(resourceManager.getString(R.string.no_interner))
                        }
                    }
                }
                progress.value = false
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Regex(MATCH_EMAIL).matches(email)
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    private fun isRegisterFormValid(form: RegisterForm): Boolean {
        if (form.address.isEmpty() ||
            form.email.isEmpty() ||
            form.password.isEmpty() ||
            form.passwordRepeat.isEmpty()
        ) {
            showErrorEvent.value = Event(resourceManager.getString(R.string.field_is_empty))
            return false
        } else if (!isEmailValid(form.email)) {
            showErrorEvent.value = Event(resourceManager.getString(R.string.email_no_correct))
            return false
        } else if (!isPasswordValid(form.password)) {
            showErrorEvent.value = Event(resourceManager.getString(R.string.password_no_correct))
            return false
        } else if (form.passwordRepeat != form.password) {
            showErrorEvent.value = Event(resourceManager.getString(R.string.password_repeat__no_correct))
            return false
        }
        return true
    }

    private fun isLoginFormValid(form: LoginForm): Boolean {
        if (form.email.isEmpty() || form.password.isEmpty()) {
            showErrorEvent.value = Event(resourceManager.getString(R.string.field_is_empty))
            return false
        } else if (!isEmailValid(form.email)) {
            showErrorEvent.value = Event(resourceManager.getString(R.string.email_no_correct))
            return false
        } else if (!isPasswordValid(form.password)) {
            showErrorEvent.value = Event(resourceManager.getString(R.string.password_no_correct))
            return false
        }
        return true
    }
}
