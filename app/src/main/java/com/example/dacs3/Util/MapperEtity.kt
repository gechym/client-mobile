package com.example.dacs3.Util

interface MapperEtity<Entity, DomainModule,SearchResults> {

    fun mapFromEnity(entity : Entity) : DomainModule

    fun mapToDonmainModule(domainModule: DomainModule) : Entity

}