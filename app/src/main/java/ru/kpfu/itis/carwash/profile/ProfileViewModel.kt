package ru.kpfu.itis.carwash.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import ru.kpfu.itis.domain.AuthInteractor
import ru.kpfu.itis.domain.FireStoreInteractor
import java.util.*
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val fireStoreInteractor: FireStoreInteractor
) : ViewModel() {

    private val progress: MutableLiveData<Boolean> = MutableLiveData()
    private val exit: MutableLiveData<Result<Boolean>> = MutableLiveData()
    private val date: MutableLiveData<Result<Date>> = MutableLiveData()
    private val user: MutableLiveData<Result<DocumentSnapshot>> = MutableLiveData()

    init {
        getUser()
    }

    fun progress(): LiveData<Boolean> = progress
    fun exit(): LiveData<Result<Boolean>> = exit
    fun date(): LiveData<Result<Date>> = date
    fun user(): LiveData<Result<DocumentSnapshot>> = user

    fun signOut() {
        viewModelScope.launch {
            progress.value = true
            val signOut = authInteractor.signOut()
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

    fun setDate(dateOfLastWash: Date) {
        viewModelScope.launch {
            progress.value = true
            val dateResult = fireStoreInteractor.updateDate(dateOfLastWash)
            if (dateResult.isSuccess) {
                dateResult.getOrNull()?.let {
                    date.value = Result.success(it)
                }
            } else {
                dateResult.exceptionOrNull()?.let {
                    date.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }

    private fun getUser(){
        viewModelScope.launch {
            progress.value = true
            val document = fireStoreInteractor.getUser()
            if(document.isSuccess){
                document.getOrNull()?.let {
                    user.value = Result.success(it)
                }
            }
            else{
                document.exceptionOrNull()?.let {
                    user.value = Result.failure(it)
                }
            }
            progress.value = false
        }
    }
}
