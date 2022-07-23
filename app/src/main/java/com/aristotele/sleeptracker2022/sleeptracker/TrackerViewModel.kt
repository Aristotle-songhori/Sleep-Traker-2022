package com.aristotele.sleeptracker2022.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import com.aristotele.sleeptracker2022.database.SleepDatabaseDao
import com.aristotele.sleeptracker2022.database.SleepNight
import com.aristotele.sleeptracker2022.formatNights
import kotlinx.coroutines.launch

class TrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) {


    private var tonight = MutableLiveData<SleepNight?>()

     val nights = database.getAllNights()






    /**
     * چرا مپ استفاده کردیم ؟ چون یه داده ای داریم که لایو دیتا هست وهمش تغیر میکنه
     * با این روش میگیم هر وقت تغیر کرد شما بگیرش و یه تغیراتی توش بده و بده به پارامتر جدیدی مثل sexyStringSpanned
     * اینطوری تو لایوت وقتی میخواهیم تعریفش کنیم میایم و این مقدار رو آبزرو میکنیم
     * اینجا میایم میگیم برو پارامتر شبها رو که یه لیستی هست همش کنترل کن وقتی تغیراتی توش دیدی بیا بگیرش
     * بعد همین لیست رو و بخش ریسورس های برنامه رو پاس میدیم به یه تابع کمکی که یه تغیراتی بده  و یه پارامتر Spanned به ما بده نمایش بدیم در فایل تکس
     */
    val sexyStringSpanned = Transformations.map(nights) { nightsLocal ->
        formatNights(nightsLocal, application.resources) // از قصد ریسورس هارو میدیم که توی این تابع ازش بتونیم به راحتی استفاده کنیم
    }


    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }


    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }


    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }


    private var _showSnackbarEvent = MutableLiveData<Boolean>()


    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent



    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }



    /**
     *شروع بازی از اینجا و اینشیالایز پروژه
     *
     */
    init {
        initializeTonight()



    }

    private fun initializeTonight() {

        //چرا باید همیشه این تابع رو صدا بزنیم ؟ چون داریم بادیتابیس کار میکنیم//و دیتابیس با ساسپند فانکشن در کوروتین هماهنگه  و اگر صدا نکنیم ارور میده میگه باید در یه کو روتین باشه یا ساسپند فانکشن
        // پس وقتی کار با دیتابیس ها داریم باید از این ویومدل اسکوپ ها صدا بزنیم حتما
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }



    }


    private suspend fun getTonightFromDatabase(): SleepNight? {
        var night = database.getTonight()
        if (night?.endTimeMilli != night?.startTimeMilli) {
            night = null
        }
        return night
    }

    private suspend fun clear() {
        database.clear()
    }

    private suspend fun update(night: SleepNight) {
        database.update(night)
    }

    private suspend fun insert(night: SleepNight) {
        database.insert(night)
    }

    //این فانکشن ها حتما باید در کو روتین باشند چون ما برای عملیات دیتابیس از کو روتین استفاده میکنیم
    fun onStartTracking() {
        viewModelScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }


    fun onStopTracking() {
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
            tonight.value = null
        }
        _showSnackbarEvent.value = true
    }







    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    private val _navigateToSleepDataQuality = MutableLiveData<Long>()
    val navigateToSleepDataQuality
        get() = _navigateToSleepDataQuality
    fun onSleepNightClicked(id: Long) {
        _navigateToSleepDataQuality.value = id
    }

    fun onSleepDataQualityNavigated() {
        _navigateToSleepDataQuality.value = null
    }































}


