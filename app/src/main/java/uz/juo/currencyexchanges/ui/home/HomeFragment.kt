package uz.juo.currencyexchanges.ui.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.juo.currencyexchanges.R
import uz.juo.currencyexchanges.adapters.HomePagerAdapter
import uz.juo.currencyexchanges.adapters.ViewPager2Adapter
import uz.juo.currencyexchanges.databinding.FragmentHomeBinding
import uz.juo.currencyexchanges.databinding.TabItemBinding
import uz.juo.currencyexchanges.utils.NetworkHelper
import uz.juo.currencyexchanges.models.DataX
import uz.juo.currencyexchanges.ui.pager.PagerFragment
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(){
    lateinit var pagerAdapter: HomePagerAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        pagerAdapter = HomePagerAdapter(requireActivity())
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.isUserInputEnabled = false
        binding.bottomNav.setOnNavigationItemSelectedListener {
            val itemId = it.itemId
            when(itemId){
                R.id.home_item->{
                    binding.bottomNav.menu.findItem(R.id.exchange_item).setIcon(R.drawable.ic_calculate_unselected)
                    binding.bottomNav.menu.findItem(R.id.home_item).setIcon(R.drawable.ic_home_selected)
                    binding.viewPager.currentItem=0
                }
                R.id.exchange_item->{
                    binding.bottomNav.menu.findItem(R.id.home_item).setIcon(R.drawable.ic_home_unselected)
                    binding.bottomNav.menu.findItem(R.id.exchange_item).setIcon(R.drawable.calculate_selected)
                    binding.viewPager.currentItem=1
                }
            }
            true
        }
        return root
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}