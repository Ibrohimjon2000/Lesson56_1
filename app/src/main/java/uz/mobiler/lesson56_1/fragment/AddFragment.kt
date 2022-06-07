package uz.mobiler.lesson56_1.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.mobiler.lesson56_1.R
import uz.mobiler.lesson56_1.database.AppDatabase
import uz.mobiler.lesson56_1.database.entity.Model
import uz.mobiler.lesson56_1.databinding.FragmentAddBinding
import java.io.File
import java.io.FileOutputStream

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentAddBinding
    private var currentPhotoPath: String = ""
    private val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(requireContext())
    }
    private var type1 = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
                View.GONE
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.title = "Yo’l belgisini qo’shish"
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            img.setOnClickListener {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.type,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            type.adapter = adapter
            type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    type1 = p0?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
            add.setOnClickListener {
                if (name.toString().isNotEmpty() && description.toString()
                        .isNotEmpty() && currentPhotoPath.isNotEmpty()
                ) {
                    val name = name.text.toString()
                    val description = description.text.toString()
                    val model = Model(
                        name = name,
                        description = description,
                        type = type1,
                        isLike = false,
                        modelPhotoPath = currentPhotoPath
                    )
                    appDatabase.modelDao().addModel(model)
                    Navigation.findNavController(binding.root).popBackStack()
                }else Toast.makeText(
                    requireContext(),
                    "Ma'lumotlar to'liq kiritilmagan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) Navigation.findNavController(binding.root)
            .popBackStack()
        return super.onOptionsItemSelected(item)
    }

    private fun takePhotoFromGalleryNewMethod() {
        takePhotoResult.launch("image/*")
    }

    private val takePhotoResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) return@registerForActivityResult
            binding.img.setImageURI(uri)
            val openInputStream = activity?.contentResolver?.openInputStream(uri)
            val file = File(activity?.filesDir, "${System.currentTimeMillis()}.jpg")
            val fileOutputStream = FileOutputStream(file)
            currentPhotoPath = file.absolutePath.toString()
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                takePhotoFromGalleryNewMethod()
            } else {

            }
        }
}