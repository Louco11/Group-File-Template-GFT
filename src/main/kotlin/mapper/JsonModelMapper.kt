package mapper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import model.MainClassJson

object JsonModelMapper {

    fun mapToString(main: MainClassJson) = Gson().toJson(main)

    fun mapToMainClassXml(main: String) = GsonBuilder().create().fromJson(main, MainClassJson::class.java)
}