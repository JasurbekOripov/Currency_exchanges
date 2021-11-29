package uz.juo.currencyexchanges.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.juo.currencyexchanges.models.Data
import uz.juo.currencyexchanges.retrofit.ApiService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    var apiService: ApiService
) : ViewModel() {

    suspend fun getData(): LiveData<Data> {
        val liveData = MutableLiveData<Data>()
        val res = apiService.getData()
        if (res.isSuccessful) {
            liveData.postValue(res.body())
        }
        return liveData
    }
}