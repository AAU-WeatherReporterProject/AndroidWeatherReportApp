package at.laubi.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import at.laubi.weatherapp.data.MeasurementPoint
import at.laubi.weatherapp.data.RestService
import java.util.ArrayList

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.app_name)

        val lv = view.findViewById<ListView>(R.id.measurementPoints)

        val  restService = RestService(view.context)

        restService.requestLocations {
            val mapped = it.map(MeasurementPoint::location)
            lv.adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, mapped)
        }

        lv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val locationId = parent.getItemAtPosition(position) as String

            restService.requestDataPoints(locationId) {
                val bundle = Bundle()
                bundle.putString("location", locationId)
                bundle.putSerializable("dataPoints", ArrayList(it))

                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
            }
        }
    }
}