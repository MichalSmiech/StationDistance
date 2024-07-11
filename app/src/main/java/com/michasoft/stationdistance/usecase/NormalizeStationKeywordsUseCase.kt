package com.michasoft.stationdistance.usecase

import com.michasoft.stationdistance.model.StationKeyword
import com.michasoft.stationdistance.util.normalize
import javax.inject.Inject

interface NormalizeStationKeywordsUseCase {
    suspend fun normalizeStationKeywords(stationKeywords: List<StationKeyword>): List<StationKeyword>
}

class NormalizeStationKeywordsUseCaseImpl @Inject constructor() : NormalizeStationKeywordsUseCase {
    override suspend fun normalizeStationKeywords(stationKeywords: List<StationKeyword>): List<StationKeyword> {
        return stationKeywords.map {
            it.copy(
                keyword = it.keyword.normalize()
            )
        }
    }
}