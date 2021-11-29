package uz.juo.currencyexchanges.ui.rate

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.juo.currencyexchanges.R
import uz.juo.currencyexchanges.adapters.ViewPager2Adapter
import uz.juo.currencyexchanges.databinding.FragmentHomeBinding
import uz.juo.currencyexchanges.databinding.FragmentRateBinding
import uz.juo.currencyexchanges.databinding.TabItemBinding
import uz.juo.currencyexchanges.models.DataX
import uz.juo.currencyexchanges.ui.home.HomeViewModel
import uz.juo.currencyexchanges.utils.FlipVerticalPageTransformer
import uz.juo.currencyexchanges.utils.NetworkHelper
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class RateFragment : Fragment() {

    lateinit var pagerAdapter: ViewPager2Adapter
    lateinit var infoList: ArrayList<DataX>
    lateinit var networkHelper: NetworkHelper
    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentRateBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        networkHelper = NetworkHelper(requireContext())
        val progressBar = binding.spinKit as ProgressBar
        val doubleBounce: Sprite = FadingCircle()
        progressBar.indeterminateDrawable = doubleBounce
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab!!.customView
                val bind = customView?.let { TabItemBinding.bind(it) }
                bind?.linear?.background = ContextCompat.getDrawable(requireContext(), R.drawable.tab_item_back)
                bind?.itemTv?.setTextColor(Color.parseColor("#FFFFFF"))
                binding.viewpager.scrollTo(binding.viewpager.currentItem, tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView1 = tab!!.customView
                val bind1 = TabItemBinding.bind(customView1!!)
                bind1.linear.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tab_item_round)
                bind1.itemTv.setTextColor(Color.parseColor("#B3B3B3"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        laodData()
    }

    private fun getData(): String {
        val d = Date()
        val s: CharSequence = DateFormat.format("dd.MM.yyyy ", d.time)
        return s.toString()
    }

    private fun setAnim(position: Float, view: View) {
        val MIN_SCALE = 0.85f
        val MIN_ALPHA = 0.5f
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> {
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    val scaleFactor = Math.max(MIN_SCALE, 1 - kotlin.math.abs(position).toFloat())
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+Infinity]
                    alpha = 0f
                }
            }
        }
    }

    private fun setTabs() {
        for (i in 0 until binding.tab.tabCount) {
            val tab_item: TabItemBinding =
                TabItemBinding.inflate(LayoutInflater.from(requireContext()))
            tab_item.itemTv.text = infoList[i].currency
            binding.tab.getTabAt(i)?.customView = tab_item.root
            if (i != 0) {
                tab_item.linear.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tab_item_round)
                tab_item.itemTv.setTextColor(Color.parseColor("#B3B3B3"))

            } else {
                tab_item.linear.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tab_item_back)
                tab_item.itemTv.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }
    }

    private fun laodData() {
        infoList = ArrayList()
        if (networkHelper.isNetworkConnected()) {
            lifecycleScope.launch {
                homeViewModel.getData().observe(viewLifecycleOwner, {
                    binding.spinKit.visibility=View.INVISIBLE
                    infoList = it.data as ArrayList<DataX>
                    setData()
                })
            }
        } else {
            view?.let { Snackbar.make(it, "No internet connection", Snackbar.LENGTH_SHORT).show() }
        }
    }

    private fun setData() {
        pagerAdapter = ViewPager2Adapter(infoList, getData(), requireActivity())
        binding.viewpager.adapter = pagerAdapter
        TabLayoutMediator(binding.tab, binding.viewpager) { tab, i ->
            tab.text = infoList[i].currency
        }.attach()
        setTabs()
        binding.viewpager.setPageTransformer { page, pos -> setAnim(pos, page) }
        val wormDotsIndicator = binding.springDotsIndicator
        wormDotsIndicator.setViewPager2(binding.viewpager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}