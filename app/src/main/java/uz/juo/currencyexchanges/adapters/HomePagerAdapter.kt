package uz.juo.currencyexchanges.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.juo.currencyexchanges.ui.exchange.ExchangeFragment
import uz.juo.currencyexchanges.ui.rate.RateFragment

class HomePagerAdapter(fa: FragmentActivity) :
    FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            RateFragment()
        } else {
            ExchangeFragment()
        }
    }
}