package edu.weatherapp.network

import com.google.gson.GsonBuilder
import edu.weatherapp.BuildConfig
import edu.weatherapp.data.CoordinatesApi
import edu.weatherapp.data.WeatherApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

object Network {

    private const val weatherBaseUrl = "http://api.openweathermap.org"

    private val apiInterceptor: Interceptor = Interceptor {
        val originalRequest = it.request()
        val newUrl = originalRequest.url().newBuilder()
            .addQueryParameter("appid", BuildConfig.API_KEY)
            .build()
        val newRequest = originalRequest.newBuilder().url(newUrl).build()
        it.proceed(newRequest)
    }

    private val weatherClient = OkHttpClient().newBuilder()
        .addInterceptor(apiInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, MillisDateAdapter())
            .create()

        Retrofit.Builder()
            .client(weatherClient)
            .baseUrl(weatherBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(
                gson
            ))
            .build()
    }

    val coordinatesApi: CoordinatesApi by lazy {
        retrofit.create(CoordinatesApi::class.java)
    }

    val weatherApi: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}
