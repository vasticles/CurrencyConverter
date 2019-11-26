package com.paypay.currencycoverter.data.di

import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import com.paypay.currencycoverter.data.repositories.cache.CacheModule
import com.paypay.currencycoverter.data.repositories.cache.models.CacheExchangeRate
import com.paypay.currencycoverter.data.repositories.cache.models.CacheExchangeRates
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule(context: Context) {

    init {
        Realm.init(context)
        CacheModule.init(prodInstance())
        AndroidThreeTen.init(context)
    }

    @Provides
    @Named("Production")
    @Singleton
    fun prodInstance(): Realm {
        val config = RealmConfiguration.Builder()
            .schemaVersion(0)
            .build()

        return Realm.getInstance(config)
    }

    @Provides
    @Named("EmptyTest")
    fun emptyTestInstance(): Realm {
        val config = RealmConfiguration.Builder()
            .inMemory()
            .name("empty ${hashCode()}")
            .build()

        return Realm.getInstance(config)
    }

    @Provides
    @Named("PopulatedTest")
    fun populatedTestInstance(): Realm {
        val config = RealmConfiguration.Builder()
            .inMemory()
            .name("populated ${hashCode()}")
            .build()

        val realm = Realm.getInstance(config)

        val now = ZonedDateTime.now()
        val retrievedAt = now.truncatedTo(ChronoUnit.SECONDS)
        val retrievedAtEpoch = retrievedAt.toEpochSecond()
        val usdJpyRate = 108.76904
        val usdCadRate = 1.322315
        val usdRubRate = 63.748038
        val usdUsdRate = 1.0

        val cacheRates = RealmList(
            CacheExchangeRate("USDJPY", usdJpyRate),
            CacheExchangeRate("USDCAD", usdCadRate),
            CacheExchangeRate("USDRUB", usdRubRate),
            CacheExchangeRate("USDUSD", usdUsdRate)
        )

        val cache = CacheExchangeRates(
            sourceCurrency = "USD",
            retrievedAt = retrievedAtEpoch,
            rates = cacheRates
        )

        realm.executeTransaction { it.insert(cache) }
        return realm
    }
}
