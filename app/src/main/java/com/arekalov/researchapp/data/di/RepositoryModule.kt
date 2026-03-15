package com.arekalov.researchapp.data.di

import com.arekalov.researchapp.data.local.LocalProductRepository
import com.arekalov.researchapp.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        localProductRepository: LocalProductRepository
    ): ProductRepository
}
