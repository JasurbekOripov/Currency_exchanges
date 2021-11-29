package uz.juo.currencyexchanges.retrofit

import retrofit2.Response
import retrofit2.http.*
import uz.juo.currencyexchanges.models.Data

interface ApiService {


    @GET("nci/NCIRate")
   suspend fun getData():Response<Data>

}