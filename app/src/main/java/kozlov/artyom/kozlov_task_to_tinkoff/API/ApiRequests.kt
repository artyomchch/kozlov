package kozlov.artyom.kozlov_task_to_tinkoff.API

import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {

    @GET("/random?json=true")
    fun getRandomPosts(): Call<DevopsLife>

}