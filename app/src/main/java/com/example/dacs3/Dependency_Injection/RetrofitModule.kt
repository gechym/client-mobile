package com.example.dacs3.Dependency_Injection

import com.example.dacs3.Util.const.HOST_LINK
import com.example.dacs3.data.retrofit.ApiRetrofit
import com.example.dacs3.ui.Fragment.SearchPost
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun proviceGsonBuilder(): Gson { //TODO Khởi tạo cung cấp đối tượng Gson cho retrofit
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Singleton
    @Provides
    fun proviceRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(HOST_LINK)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }


    @Singleton
    @Provides
    fun proviceBlogService(retrofit: Retrofit.Builder): ApiRetrofit {
        return retrofit.build().create(ApiRetrofit::class.java)
    }





}


