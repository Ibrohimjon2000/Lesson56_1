package uz.mobiler.lesson56_1.database.dao

import androidx.room.*
import uz.mobiler.lesson56_1.database.entity.Model

@Dao
interface ModelDao {

    @Insert
    fun addModel(model: Model)

    @Delete
    fun deleteModel(model: Model)

    @Update
    fun editModel(model: Model)

    @Query("select*from model")
    fun getAllModels(): List<Model>

    @Query("select*from model where isLike=:isLike")
    fun getLikeModel(isLike:Boolean):List<Model>

    @Query("select*from model where type=:type")
    fun getTypeModel(type:String):List<Model>
}