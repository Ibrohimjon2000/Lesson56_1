package uz.mobiler.lesson56_1.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.mobiler.lesson56_1.R
import uz.mobiler.lesson56_1.database.AppDatabase
import uz.mobiler.lesson56_1.database.entity.Model
import uz.mobiler.lesson56_1.databinding.FragmentAboutBinding
import java.io.File

private const val ARG_PARAM1 = "model"
private const val ARG_PARAM2 = "param2"

class AboutFragment : Fragment() {
    private var param1: Model=Model(1,"","","",true,"")
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Model
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }
    private lateinit var binding: FragmentAboutBinding
    private var currentPhotoPath: String = ""
    private val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentAboutBinding.inflate(inflater,container,false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
                View.GONE
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.title = param1.name
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            img.setImageURI(Uri.fromFile(File(param1.modelPhotoPath)))
            name.text = param1.name
            description.text = param1.description
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Model, param2: String) =
            AboutFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) Navigation.findNavController(binding.root)
            .popBackStack()
        return super.onOptionsItemSelected(item)
    }
}