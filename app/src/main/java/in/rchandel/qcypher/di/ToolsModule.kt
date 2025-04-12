package `in`.rchandel.qcypher.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.rchandel.qcypher.domain.TextAnalyser
import javax.inject.Qualifier

@Qualifier
annotation class QRTextAnalyser

@Module
@InstallIn(SingletonComponent::class)
abstract class ToolsModule {

    @QRTextAnalyser
    @Binds
    abstract fun provideTextAnalyser(qrTextAnalyser: QRTextAnalyser) : TextAnalyser
}