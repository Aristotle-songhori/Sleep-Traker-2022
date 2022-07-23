package com.aristotele.sleeptracker2022.sleepquality



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aristotele.sleeptracker2022.database.SleepDatabaseDao

/**
 * همیشه ثابته و وقتی دیتابیس داریم یه پایه کار دیتابیسه و یه داده دیگه که اینجا آیدی اون شب خاص در دیتابیس هست
 */
class QualityViewModelFactory(
    private val sleepNightKey: Long,
    private val dataSource: SleepDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QualityViewModel::class.java)) {
            return QualityViewModel(sleepNightKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

