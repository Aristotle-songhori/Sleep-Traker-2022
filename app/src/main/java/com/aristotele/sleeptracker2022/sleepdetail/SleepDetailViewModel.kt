/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aristotele.sleeptracker2022.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aristotele.sleeptracker2022.database.SleepDatabaseDao
import com.aristotele.sleeptracker2022.database.SleepNight


/**
 * ViewModel for SleepQualityFragment.
 *
 * @param sleepNightKey The key of the current night we are working on.
 */
class SleepDetailViewModel(private val sleepNightKey: Long = 0L, dataSource: SleepDatabaseDao) : ViewModel() {

    /**
     * Hold a reference to SleepDatabase via its SleepDatabaseDao.
     */
    val database = dataSource


    /**   ولی از MediatorLiveData به عنوان واسطه ای بین دو یا چند LiveData استفاده می شود و میتوان با کمک آن ، مقادیر بین دو یا چند LiveData را با یکدیگر ادغام (merge) کرد و یا مقادیر هر کدام از LiveData ها را به یک View واحد نسبت داد.
     ** فرض بگیرید 2 فرگمنت دارید که در هر کدام یک SeekBar در حال نمایش است اگر بخواهیم مقادیر این دو SeekBar با یکدیگر هماهنگ باشد از MediatorLiveData استفاده میکنیم.
     */

    //یه مدیتور میسازیم که پایین تر تو اینشیالایز پُرش کنیم از یه آبجکت
    // با گرفتن آیدی میریم و اون آبجکت رو پیدا میکنیم
    private val night = MediatorLiveData<SleepNight>()

    fun getNight() = night


//اینجا آیدی رسیده و میریم دیتابیس میگیریمش و میریزسم نوی نایتتتت

    init {
        night.addSource(database.getNightWithId(sleepNightKey), night::setValue)
    }





    /**
     * برای کنترل نویگیت شدن هست
     */
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    /**
     * وقتی این متد انجام میشه ما این رو نالل میکنیم یعنی برگشتیم به صفحه قبلی
     * که دفعه بعد اومدیم چک کنیم تروو نباشه دیگه وگرنه برنامه برنمیگرده
     */
    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }


    // فانکشن خروخ از فرگمنت که در لایوت روی دکمه برگشت پایین صفحه تنظیم شده
    fun onClose() {
        _navigateToSleepTracker.value = true
    }

}

 