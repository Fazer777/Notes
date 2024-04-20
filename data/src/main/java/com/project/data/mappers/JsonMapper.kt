package com.project.data.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type

internal abstract class JsonMapper {
    companion object{
        fun <T : Serializable> mapListToJson(list : List<T>?) : String{
            val gson = Gson()
            return gson.toJson(list)
        }

        fun <T> mapFromJsonArray(respInArray: String, clazz: Class<T>): List<T> {
            val listType: Type = TypeToken.getParameterized(List::class.java, clazz).type
            return Gson().fromJson(respInArray, listType)
        }
    }
}