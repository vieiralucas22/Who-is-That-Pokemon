package com.example.who_is_that_pokemon.model.repository.remote

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission
import com.example.who_is_that_pokemon.exception.NoInternetException
import retrofit2.Response

open class BaseRepository (private val context: Context){

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    protected fun isConnectionAvailable() : Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var result = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            val activeNet = cm.activeNetwork ?: return false

            val netWorkCapabilities = cm.getNetworkCapabilities(activeNet) ?: return false

            result = when {
                netWorkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                netWorkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else
        {
            if (cm.activeNetwork != null)
            {
                when (cm.activeNetworkInfo!!.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return result
    }

    suspend fun <T> safeApiCall(apiCall : suspend () -> Response<T>) : Response<T>{
        if (!isConnectionAvailable()){
            throw NoInternetException("No internet available")
        }

        return apiCall()
    }

}