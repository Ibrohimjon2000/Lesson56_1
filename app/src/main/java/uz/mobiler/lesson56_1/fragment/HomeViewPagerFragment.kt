package uz.mobiler.lesson56_1.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import uz.mobiler.lesson56_1.R
import uz.mobiler.lesson56_1.adapters.CustomAdapter
import uz.mobiler.lesson56_1.database.AppDatabase
import uz.mobiler.lesson56_1.database.entity.Model
import uz.mobiler.lesson56_1.databinding.FragmentHomeViewPagerBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeViewPagerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentHomeViewPagerBinding
    private lateinit var customAdapter: CustomAdapter
    private lateinit var modelList: ArrayList<Model>
    private val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeViewPagerBinding.inflate(inflater, container, false)
        binding.apply {
            modelList = ArrayList(appDatabase.modelDao().getTypeModel(param1.toString()))
            if (modelList.isNotEmpty()) {
                binding.lottie.visibility = View.INVISIBLE
            } else {
                binding.lottie.visibility = View.VISIBLE
            }
            customAdapter = CustomAdapter(modelList, object : CustomAdapter.OnItemClickListener {
                override fun onItemClick(model: Model, position: Int) {
                    val bundle = Bundle()
                    bundle.putSerializable("model", model)
                    Navigation.findNavController(binding.root).navigate(R.id.aboutFragment, bundle)
                }

                override fun onItemEdit(model: Model, position: Int) {
                    val bundle = Bundle()
                    bundle.putSerializable("model", model)
                    Navigation.findNavController(binding.root).navigate(R.id.editFragment, bundle)
                }

                override fun onItemDelete(model: Model, position: Int) {
                    AlertDialog.Builder(requireContext()).setCancelable(false)
                        .setMessage("Haqiqatda o’chirmoqchimisiz ?")
                        .setPositiveButton("Ha") { dialog, _ ->
                            appDatabase.modelDao().deleteModel(model)
                            modelList.remove(model)
                            customAdapter.notifyItemRemoved(position)
                            customAdapter.notifyItemRangeChanged(position, modelList.size)
                            if (modelList.isNotEmpty()) {
                                binding.lottie.visibility = View.INVISIBLE
                            } else {
                                binding.lottie.visibility = View.VISIBLE
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton("Yo’q") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }

                override fun onItemLike(
                    model: Model,
                    position: Int,
                    holder: CustomAdapter.Vh
                ) {
                    if (!model.isLike) {
                        holder.isLike.setImageResource(R.drawable.ic_heart_red)
                        model.isLike = true
                        val m = Model(
                            id = model.id,
                            name = model.name,
                            description = model.description,
                            type = model.type,
                            isLike = model.isLike,
                            modelPhotoPath = model.modelPhotoPath
                        )
                        appDatabase.modelDao().editModel(m)
                    } else {
                        holder.isLike.setImageResource(R.drawable.ic_heart_white)
                        model.isLike = false
                        val m = Model(
                            id = model.id,
                            name = model.name,
                            description = model.description,
                            type = model.type,
                            isLike = model.isLike,
                            modelPhotoPath = model.modelPhotoPath
                        )
                        appDatabase.modelDao().editModel(m)
                    }
                }

            })
            binding.rv.adapter = customAdapter
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            HomeViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}