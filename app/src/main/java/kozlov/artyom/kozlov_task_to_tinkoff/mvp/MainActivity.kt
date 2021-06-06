package kozlov.artyom.kozlov_task_to_tinkoff.mvp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import kozlov.artyom.kozlov_task_to_tinkoff.R
import kozlov.artyom.kozlov_task_to_tinkoff.fragments.lastMVP.LastFragmentPresenter

class MainActivity : AppCompatActivity(), MainInterface.View {

    private var presenter: MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        
        tabLayout.addTab(tabLayout.newTab().setText("Последние"))
        tabLayout.addTab(tabLayout.newTab().setText("Лучшие"))
        tabLayout.addTab(tabLayout.newTab().setText("Горячие"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tabLayout.selectedTabPosition) {
                    0 -> {
                        Toast.makeText(this@MainActivity, "Tab " + tabLayout.selectedTabPosition, Toast.LENGTH_LONG).show();
                    }
                    1 -> {
                        Toast.makeText(this@MainActivity, "Tab " + tabLayout.selectedTabPosition, Toast.LENGTH_LONG).show()
                    }
                    2 -> {
                        Toast.makeText(this@MainActivity, "Tab " + tabLayout.selectedTabPosition, Toast.LENGTH_LONG).show()
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                
            }
        })






    }


}