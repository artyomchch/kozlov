package kozlov.artyom.kozlov_task_to_tinkoff.mvp


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozlov.artyom.kozlov_task_to_tinkoff.API.ApiRequests
import kozlov.artyom.kozlov_task_to_tinkoff.API.DevopsLife
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.io.StringReader


class MainPresenter(_view: MainInterface.View): MainInterface.Presenter {
    private var view: MainInterface.View = _view
    private var model: MainInterface.Model = MainModel()
    private var BASE_URL = "https://developerslife.ru/"
    private var counter = -1
    private var casher = -1


    private var dataPost: DevopsLife? = null

    init {
        view.backButtonUnused()
        view.loadDescription()
        view.setTabText()
        getPostFromUrl()
    }


    override suspend fun getSecondPost() {
        if (counter < casher){
            counter++
            getPostFromCash()
            view.invisibleProgressBar()

        }
        else {
            getPostFromUrl()
            view.loadDescription()
            view.backButtonUsed()
            view.visibleProgressBar()
        }


    }

    override fun getDataPostSource(data: DataStore<Preferences>){
        model.getDataPostSource(data)
    }

    override suspend fun getCashPost() {
        counter--
        getPostFromCash()
        if (counter == 0){
            view.backButtonUnused()
        }

    }

    private suspend fun getPostFromCash(){


        val positions: List<String> = model.read(counter.toString())!!.split("+++++")
        val desc = positions[0]
        val url = positions[1]

        view.setDescription(desc)
        view.setImage(url)


    }






    private fun getPostFromUrl(){
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response: Response<DevopsLife> = api.getRandomPosts().awaitResponse()
            if (response.isSuccessful){
                counter++
               dataPost = response.body()!!

                model.save(counter.toString(), dataPost!!.description + "+++++" + dataPost!!.gifURL)
                casher++

                withContext(Dispatchers.Main){
                    view.setDescription(dataPost!!.description)
                    view.setImage(dataPost!!.gifURL)
                }

            }
        }
    }
}