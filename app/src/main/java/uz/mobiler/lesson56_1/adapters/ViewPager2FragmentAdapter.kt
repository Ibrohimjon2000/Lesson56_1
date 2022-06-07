package uz.mobiler.lesson56_1.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.mobiler.lesson56_1.fragment.HomeViewPagerFragment

class ViewPager2FragmentAdapter(
    fragment: Fragment,
    private val list: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return HomeViewPagerFragment.newInstance(list[position])
    }
}