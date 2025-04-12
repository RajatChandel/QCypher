package `in`.rchandel.qcypher.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.rchandel.qcypher.domain.TextAnalyser
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

}