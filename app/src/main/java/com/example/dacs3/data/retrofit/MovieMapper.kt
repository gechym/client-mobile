package com.example.dacs3.data.retrofit

import com.example.dacs3.R
import com.example.dacs3.Util.MapperEtity
import com.example.dacs3.data.retrofit.module.getHome.MovieNetworkEntity
import com.example.dacs3.data.retrofit.module.postSearch.SearchResults
import com.example.dacs3.module.Film
import com.example.example.SearchKWResults
import javax.inject.Inject


class MovieMapper
@Inject
constructor() : MapperEtity<MovieNetworkEntity,Film,SearchResults> {
    override fun mapFromEnity(entity: MovieNetworkEntity): Film {
        return Film(
            id = entity.id,
            name = entity.title,
            image = R.drawable.img_1,
            imageUrl = entity.imageUrl,
            banner = "",
            description = "",
            category = entity.category,
            epvisodeTotal = 0,
            yearRelease = 0
        )
    }

    override fun mapToDonmainModule(domainModule: Film): MovieNetworkEntity {
        return MovieNetworkEntity(
            id = domainModule.id,
            category = 0,
            title = domainModule.name,
            contentType = "",
            imageUrl = domainModule.imageUrl
        )
    }

    fun mapFromListEntity(list: List<MovieNetworkEntity>) : List<Film> {
        return list.map {
            mapFromEnity(it)
        }
    }

    fun mapSearchToEntity(searchResults: SearchResults) : Film {
        return Film(
            id = searchResults.id.toInt(),
            name = searchResults.name,
            imageUrl = searchResults.coverVerticalUrl,
            image = R.drawable.img_1,
            banner = "",
            description = "",
            category = searchResults.domainType,
            epvisodeTotal = 0,
            yearRelease = 0,
        )
    }

    fun mapSearchKWtoEntity(searchKWResults: SearchKWResults) : Film {
        return Film(
            id = searchKWResults.id.toInt(),
            name = searchKWResults.name,
            imageUrl = searchKWResults.coverVerticalUrl,
            image = R.drawable.img_1,
            banner = "",
            description = "",
            category = searchKWResults.domainType,
            epvisodeTotal = 0,
            yearRelease = 0,
        )
    }

    fun mapSearchListToEntity(list: List<SearchResults>) : List<Film> {
        return list.map {
            mapSearchToEntity(it)
        }
    }

    fun mapSearchKWListToEntity(list: List<SearchKWResults>) : List<Film> {
        return list.map {
            mapSearchKWtoEntity(it)
        }
    }
}