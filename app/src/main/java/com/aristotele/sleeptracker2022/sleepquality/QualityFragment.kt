package com.aristotele.sleeptracker2022.sleepquality


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
import com.aristotele.sleeptracker2022.databinding.FragmentQualityBinding

class QualityFragment : Fragment() {

    /**
این بخش فقط 6 تا عکس هست که رو هرکدوم کلیک بشه میره دیتابیس مقدار -1 که پیش فرض کیفیت خواب هست رو عوض میکنه از 0 تا 5

     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // مثل همیشه تعریف بایندینگ
        val binding: FragmentQualityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_quality, container, false)

        //معرفی اپلیکیشن و آرگومنت رسیده برای ویو مدل فکتوری
        val application = requireNotNull(this.activity).application
        val arguments = QualityFragmentArgs.fromBundle(arguments!!)
        val sleepDatabase1 = SleepDatabase.getInstance(application).sleepDatabaseDao

        //دادن داده ها به فکتوری
        val viewModelFactory = QualityViewModelFactory(arguments.sleepNightKey, sleepDatabase1)

        //تعریف ویو مدل و معرفی اون به لایوتxml
        val sleepQualityViewModel = ViewModelProvider(this, viewModelFactory)[QualityViewModel::class.java]
        binding.sleepQualityViewModel = sleepQualityViewModel

        // کنترل رفت و برگشت از این فرگمنت با این متد
        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner) {
            if (it == true) {
                this.findNavController().navigate(QualityFragmentDirections.actionQualityFragmentToTrackerFragment())
                //سریع بع حالت اول برمیگردونیم برای دفعه بعدی
                sleepQualityViewModel.doneNavigating()
            }
        }

        return binding.root
    }
}
