package com.manu.kode.engrama.data.dataset

import android.content.Context
import com.manu.kode.engrama.domain.model.TriviaQuestion
import com.manu.kode.engrama.domain.model.WordEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetDatasetRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) : DatasetRepository {

    override suspend fun words(): List<WordEntry> =
        DatasetParser.parseWords(readAsset("words.json"))

    override suspend fun trivia(): List<TriviaQuestion> =
        DatasetParser.parseTrivia(readAsset("trivia.json"))

    private suspend fun readAsset(name: String): String = withContext(Dispatchers.IO) {
        context.assets.open(name).bufferedReader().use { it.readText() }
    }
}
