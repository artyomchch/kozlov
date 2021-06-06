package kozlov.artyom.kozlov_task_to_tinkoff.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kozlov.artyom.kozlov_task_to_tinkoff.fragments.bestMVP.BestFragment
import kozlov.artyom.kozlov_task_to_tinkoff.fragments.HotFragment
import kozlov.artyom.kozlov_task_to_tinkoff.fragments.lastMVP.LastFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
        FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0 -> {
                LastFragment()
            }
            1 -> {
                BestFragment()
            }
            2 -> {
                HotFragment()
            }
            else -> {
                Fragment()
            }
        }
    }

}