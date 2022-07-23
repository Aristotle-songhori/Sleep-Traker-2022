package com.aristotele.sleeptracker2022.sleeptracker

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristotele.sleeptracker2022.R
import com.aristotele.sleeptracker2022.database.SleepDatabase
import com.aristotele.sleeptracker2022.databinding.FragmentTrackerBinding
import com.aristotele.sleeptracker2022.sleeptracker.recyclerview.SleepNightAdapter
import com.aristotele.sleeptracker2022.sleeptracker.recyclerview.SleepNightListener
import com.google.android.material.snackbar.Snackbar


/**
 * این فرگمنت حتما مقدار فکتوری رو داره چون داده از دیتابیس داریم و میخونیم و ...
 * یعنی داده توش میگیریم از بخش دیتابیس پس باید حتما فکتوری داشته باشه
 */
class TrackerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentTrackerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tracker, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = TrackerViewModelFactory(dataSource, application)

        // دقت کنید که این سه بار اینجا استفاده میشه  که درواقع پرووایدر ویو مدل هست
        val trackerViewModel1 = ViewModelProvider(this, viewModelFactory)[TrackerViewModel::class.java]

        //تعریف و اتصال به لایوت
        binding.sleepTrackerViewModel = trackerViewModel1

        // binding.setLifecycleOwner(this)
        binding.lifecycleOwner = this

        // //میگیم این ادابتر یه کلیک لیسنر داره که کلیک میشه یه آیدی برمیگردونه
        //آیدی رو بگیر ببر به ویو کنترلر و بنداز تو فانکشن onSleepNightClicked
        //یعنی رو فلان آیتم کلیک شده
        val adapter = SleepNightAdapter(SleepNightListener { nightId ->
            //Toast.makeText(context,"$nightId",Toast.LENGTH_LONG).show()
            trackerViewModel1.onSleepNightClicked(nightId)

        })

        binding.recyclerView.adapter=adapter

        val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(binding.root.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

       // binding.recyclerView.layoutManager = linearLayoutManager
        val manager = GridLayoutManager(activity, 3)
        binding.recyclerView.layoutManager = manager

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =  when (position) {
                0 -> 3
                else -> 1
            }
        }




        trackerViewModel1.nights.observe(viewLifecycleOwner) {
            it?.let {
                Log.i("sexy", "==>${it.size}")
                adapter.addHeaderAndSubmitList(it)// این روش خفت دادن داده به آدابتر هاست
            }
        }



        //میگیم برو مقدار showSnackBarEvent آبزرو کن که یه بولین هر وقت تغییر کرد یه پیام بده
        trackerViewModel1.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    binding.root,
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                // Reset state to make sure the snackbar is only shown once, even if the device
                // has a configuration change.
                trackerViewModel1.doneShowingSnackbar()
            }
        }






        //میگیم برو اینو نگاه کن که یه نایت رو برمیگردونه navigateToSleepQuality و هر وقت نایت جدید برگردوند بیا آیدی نایت رو بفرست به یه فرگمنت دیگه
        //اینطوری میره تو اون فرگمنت کوآلیتی و بهش یه داده کیفیت رو اضافه میکنه و بعد برمیگردونه به همینجا گمونم
        trackerViewModel1.navigateToSleepQuality.observe(viewLifecycleOwner) {

            it?.let {

                this.findNavController()
                    .navigate(TrackerFragmentDirections.actionTrackerFragmentToQualityFragment(it.nightId))

                trackerViewModel1.onSleepDataQualityNavigated()
            }

        }






        trackerViewModel1.navigateToSleepDataQuality.observe(viewLifecycleOwner) { night ->
            night?.let {

                this.findNavController().navigate(
                    TrackerFragmentDirections.actionTrackerFragmentToDetailFragment(night)
                )
                trackerViewModel1.onSleepDataQualityNavigated()
            }
        }

        return binding.root
}
}
