package `in`.rchandel.qcypher.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.rchandel.qcypher.domain.mecard.MeCardParser
import `in`.rchandel.qcypher.domain.mecard.MeCardParserImpl
import `in`.rchandel.qcypher.domain.resultparser.ResultParser
import `in`.rchandel.qcypher.domain.resultparser.ResultParserImpl
import `in`.rchandel.qcypher.domain.textanalyser.QRTextAnalyserImpl
import `in`.rchandel.qcypher.domain.textanalyser.TextAnalyser
import `in`.rchandel.qcypher.domain.wifi.WifiParser
import `in`.rchandel.qcypher.domain.wifi.WifiParserImpl
import javax.inject.Qualifier

@Qualifier
annotation class QRTextAnalyser

@Qualifier
annotation class DefaultWifiParser

@Qualifier
annotation class DefaultMeCardParser

@Qualifier
annotation class DefaultResultParser

@Module
@InstallIn(SingletonComponent::class)
abstract class ToolsModule {

    @QRTextAnalyser
    @Binds
    abstract fun provideTextAnalyser(qrTextAnalyser: QRTextAnalyserImpl) : TextAnalyser

    @DefaultWifiParser
    @Binds
    abstract fun provideWifiParser(wifiParser: WifiParserImpl) : WifiParser

    @DefaultMeCardParser
    @Binds
    abstract fun provideMeCardParser(meCardParser: MeCardParserImpl) : MeCardParser

    @DefaultResultParser
    @Binds
    abstract fun provideResultParser(resultParser: ResultParserImpl) : ResultParser
}