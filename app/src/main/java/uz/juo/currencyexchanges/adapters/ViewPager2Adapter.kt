package uz.juo.currencyexchanges.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.juo.currencyexchanges.ui.pager.PagerFragment
import uz.juo.currencyexchanges.models.DataX

class ViewPager2Adapter(var list: ArrayList<DataX>, var date:String, fa: FragmentActivity) :
    FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment =
        PagerFragment.newInstance(list[position],date)
}