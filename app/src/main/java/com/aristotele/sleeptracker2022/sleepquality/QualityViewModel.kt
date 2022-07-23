package com.aristotele.sleeptracker2022.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aristotele.sleeptracker2022.database.SleepDatabaseDao
import kotlinx.coroutines.launch

class QualityViewModel(private val sleepNightKey: Long = 0L, val database: SleepDatabaseDao) : ViewModel() {



    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker
    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }


    /**
     * وقتی رو یه عکس کلیک میشه آیدیش رو ساب کلیک میاد اینجا
     * حتما باید توی کو روتین باشه چون دیتابیس ها از ساسپند فانکشن هستن
     */


    fun onSetSleepQuality(quality: Int) {

        viewModelScope.launch {

            // یه آبجکت میسازیم و از دیتابیس اون آیدی میگیریم میریزسم توش
            val tonight = database.get(sleepNightKey) ?: return@launch

//            کیفیتی که رسیده رو میندازیم توش و آپدیتش میکنیم چون همیشه -1 هست
            tonight.sleepQuality = quality

            //حالا دیتابیس رو فعال میکنیم
            database.update(tonight)

            // Setting this state variable to true will alert the observer and trigger navigation.
            _navigateToSleepTracker.value = true

        }


    }
}

