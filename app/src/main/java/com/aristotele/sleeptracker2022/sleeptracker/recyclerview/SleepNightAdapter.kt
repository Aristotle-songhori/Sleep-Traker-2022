package com.aristotele.sleeptracker2022.sleeptracker.recyclerview


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aristotele.sleeptracker2022.R
import com.aristotele.sleeptracker2022.database.SleepNight
import com.aristotele.sleeptracker2022.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * برای کپی پیست و ساخت اسکرول همیشه این صفحه رو کپی کنید و تغیرات بخش سبز کامنت رو انجام بدید
 *
 * مواردی که لازمه یه لایوته بایندینگ هست
 *یه فایل کمکی بایندینگ برای تغیر در متد ویو ها
 *تعریف یک ریسایکلر و کتابخونش در برنامه در یکی از فرگمنت ها
 *
 *
 * فعلا 2 تا باید تغیرات بدید دربخش ویو هولدر همیشه
 * یک بایندینگ یوتیل فایل هم باید بسازید که بتونید لایوت رو و ویو های درونش رو کنترل کنید
 *
 * باید در بایندینگ لایوت حتما 2 مقدار یکی برای داده آیتم و یکی برای لیسنر تعریف کنید
 */

//برای تشخیص آیتم های هدر و ویو ساخته شده
//شما میتونید برای تشخیص تبلیغات بین لیست این رو بنویسید

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1


class SleepNightAdapter (private val clickListener: SleepNightListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    //یه کوروتین هم تعریف میکنیم
    private val coroutineScope1 = CoroutineScope(Dispatchers.Default)


    fun addHeaderAndSubmitList(list: List<SleepNight>?) {


        coroutineScope1.launch {

            //مرحله 1 یه آیتمی میسازیم
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }


            //مرحله 2 آیتم بالا رو که ساختیم میندازیم تو سابمیت لیست
            withContext(Dispatchers.Main) {
                submitList(items)
            }



        }


    }

    //همیشه همین
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight,clickListener )
            }
        }

    }




    //اگر ریسایکلر چند ویویی باشه اینجا تصمیم میگیریم کدوم ویو نمایش داده بشه
    //TextViewHolder.from(parent)
    //ViewHolder.from(parent)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else-> throw ClassCastException("Unknown viewType $viewType")
        }

    }


    /**
     * این 2 تا کلاس در واقع 2 تا ویو مختلف برای ریسایکلر ویو هستن
     * یکی هدر و در آیدی 0 نشون داده میشه و یکی دیگه خود داده های دیتابیس هست
     */
    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val yyy = LayoutInflater.from(parent.context)
                val view = yyy.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    class ViewHolder private constructor(private val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {


        /**
         * اینجا هم تغیرات داریم بخشی که یه داده به مقدار داخل لایوت تعریف میکنیم
         */
        //اینو با کلیک راست و ساخت اتوکاتیک ساختیم وگرنه هیچ فرقی نمیکنهو کپسوله شد چون پرایوت هست
        fun bind(item: SleepNight, clickListener: SleepNightListener) {
//            اتصال بایندینگ به داده داخل لایوت
            binding.sleep = item
            //اتصال بایندینگ به کلیک لیسنر
            binding.clickListener = clickListener
            //حتما باید فایل بایندینگ یوتیل ساخته شود و متد های اختصاصی به ویو های اندروید داده شود
//            و اونجا همه موضوعات رو ست کنیم
            binding.executePendingBindings() // باید باشه
        }
        /**
         * انجا تغیرات داریم برای معرفی لایه داخل ویو که یه لیست بایندینگه خود بایندینگ میشناستش
         */
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val xxx = LayoutInflater.from(parent.context)
                // ListItemSleepNightBinding معرفی لایه داخلی و ویو ریسایکلر ویو
                val binding = ListItemSleepNightBinding.inflate(xxx, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * این چی میگه
 * بدون این خیلی فشار میاد به ریسایکلر ویو و موقع ادیت داده ها همه چی از نو ساخته میشه
 * بنابر ایم این به همین سبک همیشه هست تا فقط یه پارامتر ثابت که معمولا آیدی هست رو مقایسه کنه اگر تغیر نداشته دیگه نسازه و همون رو نمایش بده
 * لگ زدن ریسایکلر و ... رو کاملا حل میکنه
 */
class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        //همیشه همینطوری و 3 تا مساوی
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
//        معمولا همیشه آیدی و میدیم برای مقایسه و 2 تا مساوی
        return oldItem.id == newItem.id
    }
}


/**
 * برای کلیک روی کل ویو ساخته میشه
 * این کلاس در لایوت معرفی میشه به عنوان لیسنر کلیک داده ها
 * در بخش بایندینگ
 *  android:onClick="@{() -> clickListener.onClick(sleep)}"
 */
class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}



//کلاس های نفوذ ناپذیر درکاتلین
//اوئل اینو بخونید
//https://pvlearn.com/product/%D8%A2%D9%85%D9%88%D8%B2%D8%B4-%DA%A9%D8%A7%D8%B1-%D8%A8%D8%A7-sealed-class-%DA%A9%D8%A7%D8%AA%D9%84%DB%8C%D9%86/

//بعد اینو بخونید
//https://virgool.io/@ramtintoosi/eum-vs-sealed-in-kotlin-qwg2lnwuydtg

//بعد هم این رو بخونید
//https://blog.faradars.org/power-of-sealed-classes-in-kotlin/
// برای استفاده ااز when خیلی کار رو راحت میکنه
// مثلا چند ویو داریم دیگه الان 2 تیپ ویو داریم 1- هدر 2-خود داده ها
//میتونیم مثلا برای اضافه کردن ویو تبلیغات هم استفاده کنیم
//و بگیم وقتی فلان شماره بود این ویو رو نشون بده
sealed class DataItem {
    // اینا همیشه اینطوری نوشته میشن
    data class SleepNightItem(val sleepNight: SleepNight): DataItem() {
        override val id = sleepNight.nightId
    }
    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }
    abstract val id: Long
}

