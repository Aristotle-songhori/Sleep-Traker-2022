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

package com.aristotele.sleeptracker2022.sleeptracker.recyclerview

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aristotele.sleeptracker2022.R
import com.aristotele.sleeptracker2022.convertDurationToFormatted
import com.aristotele.sleeptracker2022.convertNumericQualityToString
import com.aristotele.sleeptracker2022.database.SleepNight

/**
 * خیلی جالبه میاد به هر ویو در اندروید که بخواهیم یک متد اضافه میکنه به اسم بعدش
 * ImageView.setSleepImage مثلا لان به همه ایمیج ویو ها یه متد ست پیکچر اضافه شده
 * مه داخل لایوت اینطوری صدا میشه
 * app:sleepImage="@{sleep}"
 * خوب اون مقدار داخل آکولاد مقدار بایندینگ لایوت هست در بخش دیتا
 * چون همونطور که میبینیم باید یه مقداری بهش بدیم تو پرانتز به عنوان آیتم
 * fun ImageView.setSleepImage(item: SleepNight?)
 * و بعد داخل فانکشن برای این ویو یه چیزی انجام میشه مثلا اینجا ست ایمیج شده
 *  setImageResource
 */

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight?) {
    item?.let {
        setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })
    }
}

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight?) {
    item?.let {
        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
    }
}


/**
 * اینجل یه متد ساختیم که به همه ویو های تکس مربوت میشه
 * و توش تکس ست کردیم
 * و اینطوری صدا میکنیم
 * app:sleepQualityString="@{sleep}"/>
 * و به هر ویو تکسی که تو ایوت باشه مقدار این تکس رو برمیگردونه
 */
@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(item: SleepNight?) {
    item?.let {
        text = convertNumericQualityToString(item.sleepQuality, context.resources)
    }
}

//این برای تست و فهمیدن خودمون

@BindingAdapter("rangg")
fun TextView.setRangg  (item: SleepNight?) {
    //میگیم اگر آیدی زوج هست رنگش قرمز بشه
//    اگر نه آبی
//    به کار علاقه مندی و لایک میخوره
    item?.let {
        if(item.nightId % 2L == 0L){
            setTextColor(Color.RED)
        }
        else{
            setTextColor(Color.BLUE)
        }


    }
}
