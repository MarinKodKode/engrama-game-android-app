package com.manu.kode.engrama.data.dataset

import com.manu.kode.engrama.domain.model.TriviaQuestion
import com.manu.kode.engrama.domain.model.WordEntry

interface DatasetRepository {
    suspend fun words(): List<WordEntry>
    suspend fun trivia(): List<TriviaQuestion>
}
