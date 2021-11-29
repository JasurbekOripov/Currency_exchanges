package uz.juo.currencyexchanges.ui.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uz.juo.currencyexchanges.R
import uz.juo.currencyexchanges.databinding.FragmentHomeBinding
import uz.juo.currencyexchanges.databinding.FragmentPagerBinding
import uz.juo.currencyexchanges.models.DataX

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PagerFragment : Fragment() {
    private var _binding: FragmentPagerBinding? = null
    private val binding get() = _binding!!
    private var param1: DataX? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as DataX
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagerBinding.inflate(inflater, container, false)
        val root = binding.root
        binding.buyPrice.text = param1?.buyCourse.toString()
        binding.sellPrice.text = param1?.sellCourse.toString()
        binding.tv.text = param2
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: DataX, param2: String) =
            PagerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}