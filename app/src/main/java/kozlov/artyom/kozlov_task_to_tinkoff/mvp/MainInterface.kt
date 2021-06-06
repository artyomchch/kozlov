package kozlov.artyom.kozlov_task_to_tinkoff.mvp

import android.content.Context
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kozlov.artyom.kozlov_task_to_tinkoff.API.DevopsLife

interface MainInterface {
    interface View{
        fun loadDescription()
        fun setDescription(text: String)
        fun setTabText()
        fun setImage(url: String)
        fun backButtonUnused()
        fun backButtonUsed()
        fun visibleProgressBar()
        fun invisibleProgressBar()
        fun showError()
        fun showErrorForPost()



    }

    interface Presenter{
        suspend fun getSecondPost()
        suspend fun getCashPost()
        fun getDataPostSource(data: DataStore<Preferences>)


    }

    interface Model{

        fun getDataPostSource(_data: DataStore<Preferences>)
        suspend fun save(key: String, value: String)
        suspend fun read(key: String): String?
    }
}