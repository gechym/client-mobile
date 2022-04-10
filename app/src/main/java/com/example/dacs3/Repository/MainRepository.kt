package com.example.dacs3.Repository

import com.example.dacs3.data.retrofit.ApiRetrofit
import com.example.dacs3.data.retrofit.MovieMapper
import com.example.dacs3.data.retrofit.module.getDetail.DetailMovie
import com.example.dacs3.data.retrofit.module.getHome.ListCategory
import com.example.dacs3.data.retrofit.module.getMediaData.MediaData
import com.example.dacs3.data.retrofit.module.postSearch.PostSearch
import com.example.dacs3.data.retrofit.module.postSearch.SearchResults
import com.example.dacs3.module.CategoryFilm
import com.example.dacs3.module.Film
import com.example.dacs3.ui.Fragment.SearchPost
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject


class MainRepository
@Inject
constructor(
    private val apiRetrofit: ApiRetrofit,
    private val movieMapper: MovieMapper
) {
    suspend fun getListMovie(page: Int): List<CategoryFilm> {
        val res: Response<ListCategory> = apiRetrofit.getMovieHomePage(page)
        val listCategoryReturn: MutableList<CategoryFilm> = mutableListOf()

        if (res.isSuccessful) {
            val list = res.body()?.data // ListCategory

            list!!.forEachIndexed { index, element ->

                if (index == 0) {

                    listCategoryReturn.add(
                        CategoryFilm(
                            element.homeSectionName,
                            movieMapper.mapFromListEntity(element.listMovie),
                            true
                        )
                    )
                }else {
                    listCategoryReturn.add(
                        CategoryFilm(
                            element.homeSectionName,
                            movieMapper.mapFromListEntity(element.listMovie),
                            false
                        )
                    )
                }

            }


        }

        return listCategoryReturn
    }

    suspend fun getListMovie2(page: Int):  ListCategory{
        val res = apiRetrofit.getMovieHomePage(page)

        return res.body()!!
    }



    suspend fun getDataDetailMovie(id : Int, category: Int): DetailMovie {
        val res = apiRetrofit.getDetailMovie(id, category)

//        if (res.isSuccessful) {
//            val detailMovieReturn = res.body()
//            return detailMovieReturn!!
//        }

        return res.body()!!
    }

    suspend fun getMediaMovie(
        category : Int,
        contentId : Int,
        episodeId : Int,
        definition : String
    ) : MediaData {
        val res = apiRetrofit.getMediaMovie(category,contentId,episodeId,definition)

        return res.body()!!
    }

    suspend fun getSearchMovie(searchPost: SearchPost): List<Film> {
        val res = apiRetrofit.SearchPost(searchPost)
        return movieMapper.mapSearchListToEntity(res.body()!!.data.searchResults)
    }
    
    suspend fun searchWithKeyWord(kw : String) : List<Film> {
        val res = apiRetrofit.searchWithKeyWord(kw)
        return movieMapper.mapSearchKWListToEntity(res.body()!!.data.searchResults)
    }





}