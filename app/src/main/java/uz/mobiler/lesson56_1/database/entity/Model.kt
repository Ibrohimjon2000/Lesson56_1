package uz.mobiler.lesson56_1.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Model(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var description: String,
    var type: String,
    var isLike: Boolean =false,
    @ColumnInfo(name = "image_path")
    var modelPhotoPath: String
):Serializable