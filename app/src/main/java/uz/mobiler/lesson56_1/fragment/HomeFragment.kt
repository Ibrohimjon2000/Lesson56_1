package uz.mobiler.lesson56_1.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import uz.mobiler.lesson56_1.R
import uz.mobiler.lesson56_1.adapters.ViewPager2FragmentAdapter
import uz.mobiler.lesson56_1.databinding.CustomTabBinding
import uz.mobiler.lesson56_1.databinding.FragmentHomeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager2FragmentAdapter: ViewPager2FragmentAdapter
    val list = arrayOf(
        "Ogohlantiruvchi",
        "Imtiyozli",
        "Ta'qiqlovchi",
        "Buyuruvchi",
        "Axborot ishora",
        "Servis",
        "Qo'shimcha axborot"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
                View.VISIBLE
            actionBar?.setDisplayShowHomeEnabled(false)
            actionBar?.setDisplayHomeAsUpEnabled(false)
            actionBar?.title = "Yo’l belgilari"
            viewPager2FragmentAdapter =
                ViewPager2FragmentAdapter(this@HomeFragment, list.toList())
            binding.viewPager.adapter = viewPager2FragmentAdapter
            val tabLayoutMediator =
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    val customTabBinding =
                        CustomTabBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                    customTabBinding.title.text = list[position]
                    if (position == 0) {
                        customTabBinding.liner.setBackgroundResource(R.drawable.tab_selector)
                        customTabBinding.title.setTextColor(Color.parseColor("#005CA1"))
                    } else {
                        customTabBinding.liner.setBackgroundResource(R.drawable.tab_unselector)
                        customTabBinding.title.setTextColor(Color.WHITE)
                    }
                    tab.customView = customTabBinding.root
                    tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab) {
                            val binding = tab.customView?.let { CustomTabBinding.bind(it) }
                            binding?.liner?.setBackgroundResource(R.drawable.tab_selector)
                            binding?.title?.setTextColor(Color.parseColor("#005CA1"))
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab) {
                            val binding = tab.customView?.let { CustomTabBinding.bind(it) }
                            binding?.liner?.setBackgroundResource(R.drawable.tab_unselector)
                            binding?.title?.setTextColor(Color.WHITE)
                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {

                        }
                    })
                }
            tabLayoutMediator.attach()
        }
        return binding.root
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            Navigation.findNavController(binding.root).navigate(R.id.addFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}