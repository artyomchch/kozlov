package kozlov.artyom.kozlov_task_to_tinkoff.mvp

import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.migrations.SharedPreferencesMigration

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kozlov.artyom.kozlov_task_to_tinkoff.API.DevopsLife
import java.io.IOException


class MainModel: MainInterface.Model {
    private lateinit var dataPosts: DataStore<Preferences>


    override suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataPosts.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    override suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataPosts.data.first()
        return preferences[dataStoreKey]
    }




    override fun getDataPostSource(_data: DataStore<Preferences>) {
       dataPosts = _data
    }




}