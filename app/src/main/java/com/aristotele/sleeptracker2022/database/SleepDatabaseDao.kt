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

package com.aristotele.sleeptracker2022.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 *دقت کنید اگر از نسخه خاص کتابخونه روم استفاده نکنید و آخرین نسخه نباشه نمیتونید اینجا جلوتر برید
 * ارور پشت ارور که آفا ساسپند رو نمیشناسه و ...
 *  // دقت کنید اگر این نسخه نباشه نوتیشن ها رو در روم نمیخونه و داستان میشه وهمش خطامیده
implementation "androidx.activity:activity-ktx:1.5.0"
// بعد ساسپند فانکشن هم نمیتونیم داشته باشیم توی دیتابیس
def roomVersion = "2.5.0-alpha02"
// optional - Kotlin Extensions and Coroutines support for Room
implementation("androidx.room:room-ktx:$roomVersion")
kapt("androidx.room:room-compiler:$roomVersion")
testImplementation "androidx.room:room-testing:$roomVersion"
 */
@Dao
interface SleepDatabaseDao {
    @Insert
    suspend fun insert(night: SleepNight)

    @Update
    suspend fun update(night: SleepNight)

    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    suspend fun get(key: Long): SleepNight?

    @Query("DELETE FROM daily_sleep_quality_table")
    suspend fun clear()

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?

    /**
     * Selects and returns the night with given nightId.
     */
    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun getNightWithId(key: Long): LiveData<SleepNight>
}

