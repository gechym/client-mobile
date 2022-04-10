package com.example.dacs3.data.retrofit

import com.example.dacs3.data.retrofit.module.getDetail.DetailMovie
import com.example.dacs3.data.retrofit.module.getHome.ListCategory
import com.example.dacs3.data.retrofit.module.getMediaData.MediaData
import com.example.dacs3.data.retrofit.module.postSearch.PostSearch
import com.example.dacs3.ui.Fragment.SearchPost
import com.example.example.PostSearchWithKeyWord
import retrofit2.Response
import retrofit2.http.*


interface ApiRetrofit {


    //http://localhost:3000/getListMovie?page=0
    @GET("getListMovie")
    suspend fun getMovieHomePage(
        @Query("page") page : Int
    ) : Response<ListCategory>


//    http://localhost:3000/getDetailMovie?id=12228&category=1
    @GET("getDetailMovie")
    suspend fun getDetailMovie(
        @Query("id") id : Int,
        @Query("category") category : Int
    ) : Response<DetailMovie>



    @GET("getMovieMedia")
    suspend fun getMediaMovie(
        @Query("category") category : Int,
        @Query("contentId") contentId : Int,
        @Query("episodeId") episodeId : Int,
        @Query("definition") definition : String
    ) : Response<MediaData>

//    suspend fun getSearchConfig() : Response<>

    @POST("search")
    suspend fun SearchPost(
        @Body searchPost: SearchPost
    ) : Response<PostSearch>


    @FormUrlEncoded
    @POST("searchWithKeyWord")
    suspend fun searchWithKeyWord(
        @Field("name") kw : String
    ) : Response<PostSearchWithKeyWord>

}