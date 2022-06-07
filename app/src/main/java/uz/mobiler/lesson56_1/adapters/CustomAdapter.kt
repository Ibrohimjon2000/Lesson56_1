package uz.mobiler.lesson56_1.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.mobiler.lesson56_1.R
import uz.mobiler.lesson56_1.database.entity.Model
import java.io.File

class CustomAdapter(
    var list: ArrayList<Model>,
    val listener: OnItemClickListener,
) : RecyclerView.Adapter<CustomAdapter.Vh>() {

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val isLike: ImageView = itemView.findViewById(R.id.like)
        val image: ImageView = itemView.findViewById(R.id.img)
        val edit: LinearLayout = itemView.findViewById(R.id.edit)
        val delete: LinearLayout = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.custom_item, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.title.text = list[position].name
        holder.image.setImageURI(Uri.fromFile(File(list[position].modelPhotoPath)))
        if (list[position].isLike) {
            holder.isLike.setImageResource(R.drawable.ic_heart_red)
        } else {
            holder.isLike.setImageResource(R.drawable.ic_heart_white)
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(
                list[position],
                position
            )
        }
        holder.edit.setOnClickListener {
            listener.onItemEdit(
                list[position],
                position
            )
        }
        holder.delete.setOnClickListener {
            listener.onItemDelete(
                list[position],
                position
            )
        }
        holder.isLike.setOnClickListener {
            listener.onItemLike(
                list[position],
                position,
                holder
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemClick(model: Model, position: Int)
        fun onItemEdit(model: Model, position: Int)
        fun onItemDelete(model: Model, position: Int)
        fun onItemLike(model: Model, position: Int, holder: Vh)
    }
}