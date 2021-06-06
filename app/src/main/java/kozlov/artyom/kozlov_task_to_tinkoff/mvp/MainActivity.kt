package kozlov.artyom.kozlov_task_to_tinkoff.mvp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kozlov.artyom.kozlov_task_to_tinkoff.R
import kozlov.artyom.kozlov_task_to_tinkoff.databinding.ActivityMainBinding
import java.io.IOException
import java.util.prefs.Preferences

class MainActivity : AppCompatActivity(), MainInterface.View {

    private var presenter: MainPresenter? = null
    //view binding
    private lateinit var binding: ActivityMainBinding



    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by lazy {
        createDataStore(name = "posts")
    }

     //save
    private suspend fun save(key: String, value: String){
        val dataStoreKey = preferencesKey<String>(key)
         dataStore.edit { posts ->
             posts[dataStoreKey] = value

         }
    }

   // read (emit)
   private suspend fun read(key: String): String?{
       val dataStoreKey = preferencesKey<String>(key)
       val preferences = dataStore.data.first()
       return preferences[dataStoreKey]
   }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isOnline(baseContext)){
            presenter = MainPresenter(this)
            presenter!!.getDataPostSource(dataStore)
        }
        else{
            invisibleProgressBar()
            showError()
        }




        binding.buttonError.setOnClickListener{
            finish()
            startActivity(intent)
        }




        binding.next.setOnClickListener{
            lifecycleScope.launch {
                presenter!!.getSecondPost()
            }
            //binding.progress.visibility = View.VISIBLE
        }


        binding.back.setOnClickListener{
        lifecycleScope.launch {
            presenter!!.getCashPost()
            }
        }



        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (binding.tabLayout.selectedTabPosition) {
                    0 -> {
                        Toast.makeText(this@MainActivity, "Tab " + binding.tabLayout.selectedTabPosition, Toast.LENGTH_LONG).show()
                    }
                    1 -> {
                        lifecycleScope.launch {
                           save("1", "some file i need")
                        }
                    }
                    2 -> {
                        lifecycleScope.launch{
                            Toast.makeText(this@MainActivity, read("1"), Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                
            }
        })



    }

    override fun loadDescription() {
        binding.description.resetLoader()
    }


    override fun setDescription(text: String) {
        binding.description.setText(text)
    }

    override fun setTabText() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Последние"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Лучшие"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Горячие"))
    }

    @SuppressLint("CheckResult")
    override fun setImage(url: String) {
        Glide.with(binding.root)
            .asGif()
            .listener(object : RequestListener<GifDrawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progress.visibility = View.GONE

                    return false;
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progress.visibility = View.GONE
                    return false
                }
            })
            .fallback(R.drawable.ttrs)
            .load(url)
            .error(R.drawable.error)
            .into(binding.imageView)
    }

    override fun backButtonUnused() {
        binding.back.visibility = View.INVISIBLE
    }

    override fun backButtonUsed() {
        binding.back.visibility = View.VISIBLE
    }

    override fun visibleProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun invisibleProgressBar() {
        binding.progress.visibility = View.GONE
    }

    override fun showError() {
        Glide.with(this)
            .asGif()
            .centerCrop()
            .load(R.drawable.error)
            .into(binding.imageView)

        binding.description.setText("Произошла ошибка при загрузке данных. Проверьте подключение к сети.")
        binding.next.visibility = View.GONE
        binding.back.visibility = View.GONE
        binding.buttonError.visibility = View.VISIBLE
    }

    override fun showErrorForPost() {
        Glide.with(this)
            .asGif()
            .centerCrop()
            .load(R.drawable.error)
            .into(binding.imageView)

        binding.description.setText("Произошла ошибка при загрузке данных. Проверьте подключение к сети. \n Нажмите далее, чтоб загузить следующий пост")


    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


}