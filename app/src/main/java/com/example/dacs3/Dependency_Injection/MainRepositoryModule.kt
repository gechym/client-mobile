package com.example.dacs3.Dependency_Injection

import com.example.dacs3.Repository.MainRepository
import com.example.dacs3.data.retrofit.ApiRetrofit
import com.example.dacs3.data.retrofit.MovieMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object MainRepositoryModule {

    @Singleton
    @Provides
    fun provideMainReposotory(
        apiRetrofit: ApiRetrofit,
        movieMapper: MovieMapper
    ) : MainRepository{
        return MainRepository(apiRetrofit,movieMapper)
    }


}