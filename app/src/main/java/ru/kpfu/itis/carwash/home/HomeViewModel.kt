package ru.kpfu.itis.carwash.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.domain.AuthInteractor
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val interactor: AuthInteractor
) : ViewModel() {

    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val exit: MutableLiveData<Result<Boolean>> = MutableLiveData()

    fun progress(): LiveData<Boolean> = progress
    fun exit(): LiveData<Result<Boolean>> = exit

    fun signOut() {
        viewModelScope.launch {
            progress.value = true
            val signOut = interactor.signOut()
            if (signOut.isSuccess) {
                signOut.getOrNull()?.let {
                    exit.value = Result.success(it)
                }
            } else {
                signOut.exceptionOrNull()?.let {
                    exit.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }
}
