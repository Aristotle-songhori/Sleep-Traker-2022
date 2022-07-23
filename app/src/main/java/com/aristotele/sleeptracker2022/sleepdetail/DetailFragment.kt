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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aristotele.sleeptracker2022.R
import com.aristotele.sleeptracker2022.database.SleepDatabase
import com.aristotele.sleeptracker2022.databinding.FragmentDetailBinding


/**
 * این جا وقتی روی یک گزینه کلیک میشه در ریسایکلر ویو
 * یه آبجکت یا یه آیدی یا .. رو میفرستیم اینجا
 * تا مشخصات اون رو بخونیم و نمایش بدیم
 */
class DetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding: FragmentDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = DetailFragmentArgs.fromBundle(arguments!!)

        // ساخت یک نمونه از ویو مدل فکتوری که به پارامتر دایو و آرگومنت داخل فرگمنت نیاز داره
        val sleepDatabaseDao1 = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = DetailViewModelFactory(arguments.sleepNightKey, sleepDatabaseDao1)

        // یک مرجع به ViewModel مرتبط با این فرگمنت دریافت کنید.
        val sleepDetailViewModel = ViewModelProvider(this, viewModelFactory)[SleepDetailViewModel::class.java]

        // برای استفاده از View Model با اتصال داده ها، باید به صراحت
        // اتصال لایوت و داده های آن به ویو مدل در فایل XML.
        binding.sleepDetailViewModel = sleepDetailViewModel

        // اتصال بایندینگ به چرخه حیات
        binding.lifecycleOwner = this

        //برای Navigation یک Observer را به متغیر حالت اضافه کنید
        //هنگامی که یک نماد کیفیت ضربه می زند.
        sleepDetailViewModel.navigateToSleepTracker.observe(viewLifecycleOwner) {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToTrackerFragment())
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                sleepDetailViewModel.doneNavigating()
            }
        }

        return binding.root
    }
}
