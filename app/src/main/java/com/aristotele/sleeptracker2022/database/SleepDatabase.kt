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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * بخش ساخت یک کلاس سنگلتون برای دیتابیس
 *کتابخونه های room
 *  // دقت کنید اگر این نسخه نباشه نوتیشن ها رو در روم نمیخونه و داستان میشه وهمش خطامیده
implementation "androidx.activity:activity-ktx:1.5.0"
// بعد ساسپند فانکشن هم نمیتونیم داشته باشیم توی دیتابیس
def roomVersion = "2.5.0-alpha02"
// optional - Kotlin Extensions and Coroutines support for Room
implementation("androidx.room:room-ktx:$roomVersion")
kapt("androidx.room:room-compiler:$roomVersion")
testImplementation "androidx.room:room-testing:$roomVersion"
 */


/**
 هر بار دیتابیس تغیرات داره باید نسخه رو ببریم بالا تا با نسخه های قبلی به مشکل نخوره مثلا ستون اضافه و کم میشه و ....]
 */
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    /**
     * یک دائو رو معرفی میکنیم
     */
    abstract val sleepDatabaseDao: SleepDatabaseDao


    /**
     * این بخش معمولا همیشه ثابت هست و میاد یک اینستنس(نمونه) دیتابیس رو اگر ساخته نشده میسازه و اینشیالایز میکنه
     */
    companion object {

        @Volatile
        private var INSTANCE: SleepDatabase? = null


        /**
         * یه فانکشن که یه نمونه از دیتابیس رو برمیگردونه و سینگل تون هست یعنی فقط یه بار ساخته میشه
         */
        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
