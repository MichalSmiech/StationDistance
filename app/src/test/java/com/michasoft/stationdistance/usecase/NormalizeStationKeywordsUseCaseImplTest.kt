package com.michasoft.stationdistance.usecase

import com.michasoft.stationdistance.model.StationKeyword
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class NormalizeStationKeywordsUseCaseImplTest {
    @Test
    fun normalizeStationKeywordsWithDiacritics() = runBlocking {
        val stationKeywords = listOf<StationKeyword>(
            StationKeyword(1, "ążćł", stationId = 1)
        )
        val usecase = NormalizeStationKeywordsUseCaseImpl()

        val normalizeStationKeywords = usecase.normalizeStationKeywords(stationKeywords)

        assertEquals("azcl", normalizeStationKeywords.first().keyword)
    }

    @Test
    fun normalizeStationKeywordsWithNoDiacritics() = runBlocking {
        val stationKeywords = listOf<StationKeyword>(
            StationKeyword(1, "abc", stationId = 1)
        )
        val usecase = NormalizeStationKeywordsUseCaseImpl()

        val normalizeStationKeywords = usecase.normalizeStationKeywords(stationKeywords)

        assertEquals("abc", normalizeStationKeywords.first().keyword)
    }

    @Test
    fun normalizeStationKeywordsWithMix() = runBlocking {
        val stationKeywords = listOf<StationKeyword>(
            StationKeyword(1, "aącćł", stationId = 1)
        )
        val usecase = NormalizeStationKeywordsUseCaseImpl()

        val normalizeStationKeywords = usecase.normalizeStationKeywords(stationKeywords)

        assertEquals("aaccl", normalizeStationKeywords.first().keyword)
    }
}