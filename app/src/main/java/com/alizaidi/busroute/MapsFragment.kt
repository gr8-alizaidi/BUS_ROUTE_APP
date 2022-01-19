package com.alizaidi.busroute

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.alizaidi.busroute.models.BusInfo
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alizaidi.busroute.Repository.repo
import com.alizaidi.busroute.models.Route
import com.alizaidi.busroute.models.Stops
import com.alizaidi.busroute.utils.DistanceCalculator
import com.alizaidi.busroute.utils.MapUtils
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlin.collections.ArrayList

class MapsFragment : Fragment(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    var intent1: Intent? = null
    var gpsStatus = false
    private lateinit var map: GoogleMap
    lateinit var container: ViewGroup
    private lateinit var businfo: ArrayList<BusInfo>
    private var stops: ArrayList<Stops> = ArrayList()
    lateinit var progressDialog: ProgressDialog
    val repo: repo = repo()
     var latlng: LatLng?=null
    private var currentLocation: Location? = null


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        markerlistener()
//        latlng= LatLng(28.77,77.105)
//        setMarker()


    }

    private fun setMarker() {

        val zoomLevel = 15f
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng!!, zoomLevel))
        map.addMarker(MarkerOptions().position(latlng!!))
        getBus()
        getStop()
    }


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_maps, container, false)
        this.container = container!!
        checkGpsStatus()

        return v
    }
    private fun checkGpsStatus() {
        locationManager = requireActivity().applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsStatus) {
            getLocation()
        } else {
            Toast.makeText(requireContext(),"Turn GPS On", Toast.LENGTH_SHORT).show()
            gpsStatus()
            getLocation()
        }
    }
    fun gpsStatus() {
        intent1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent1);
    }
    private fun getLocation() {
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        if(latlng==null) {

            latlng = LatLng(location.latitude, location.longitude)

            setMarker()
        }

        progressDialog.hide()
    }
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        searchView.setOnClickListener() {
            it.findNavController()
                .navigate(MapsFragmentDirections.actionMapsFragmentToSearchBusFragment())
        }
        gps_fab.setOnClickListener() {
            checkGpsStatus()
            progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle("Loading ")
            progressDialog.setMessage("Fetching GPS Location")
            progressDialog.show()

        }
        filter_fab.setOnClickListener(){
            Toast.makeText(requireContext(),"Filter Button Clicked",Toast.LENGTH_SHORT).show()

        }
            if(latlng==null) {
                progressDialog = ProgressDialog(requireContext())
                progressDialog.setTitle("Loading ")
                progressDialog.setMessage("Fetching GPS Location")
                progressDialog.show()
            }
        }




    private fun getStop() {
       val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Loading ")
        progressDialog.setMessage("Fetching Stop location")
        progressDialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            repo.getStop()
            if (repo.status == 1) {
                stops = repo.stops
                withContext(Dispatchers.Main) {
                    showStopsOnMap()
                    progressDialog.hide()
                }
            }
        }
    }

    private fun showStopsOnMap() {
        for (i in stops) {
            if (DistanceCalculator().CalculationByDistance(
                    LatLng(i.lat, i.lng),
                    latlng!!
                ) <= 2.0
            ) {
                var latlng = LatLng(i.lat, i.lng)
                val snippet = String.format(
                    i.name
                )
                map.addMarker(
                    MarkerOptions()
                        .position(latlng)
                        .title(i.id)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_2))
                )
            }
        }
        progressDialog.hide()
    }


    fun getBus() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.getBus()
            if (repo.status == 1) {
                businfo = repo.businfo

                withContext(Dispatchers.Main) {
                    showBusOnMap()
                }
            }
        }
    }


    private fun showBusOnMap() {
        for (i in businfo) {
            if (i.agency == "DTC" && i.ac == "nac")
                showBitmap(i, R.raw.img_green)
            if (i.agency == "DTC" && i.ac == "ac")
                showBitmap(i, R.raw.img_red)
            if (i.agency == "DIMTS" && i.ac == "nac")
                showBitmap(i, R.raw.img_orange)
            if (i.agency == "DIMTS" && i.ac != "nac")
                showBitmap(i, R.raw.img_blue)

        }
    }

    private fun showBitmap(i: BusInfo, img: Int) {
        var latlng = LatLng(i.lat, i.lng)
        val mapUtils = MapUtils()
        val bitmap: Bitmap = mapUtils.GetBitmapMarker(

            requireContext(),
            img,
            i.route.substring(0, 3)
        )!!

        map.addMarker(
            MarkerOptions()
                .position(latlng)
                .title(i.id)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        )
    }

    private fun markerlistener() {
        map.setOnMarkerClickListener {
            for (i in businfo) {
                if (i.id == it.title)
                    showBottomSheet(i)
            }
            for (i in stops) {
                if (i.id == it.title) {
                    it.showInfoWindow()
                }
            }
            true
        }
    }

    private fun showBottomSheet(i: BusInfo) {
        var route:Route
        val dia=ProgressDialog(requireContext())

        dia.setTitle("Loading ")
        dia.setMessage("Fetching Bus Info")
        dia.show()

        CoroutineScope(Dispatchers.IO).launch {
            repo.getRouteData()
            if(repo.status==1)
            {
                for(j in repo.routes)
                {
                    if(i.route==j.long_name)
                    {
                        route=j
                        withContext(Dispatchers.Main) {

                            (activity as MainActivity).bottomSheetData(i,route.end)
                            dia.hide()
                        }

                        break
                    }
                }

            }
        }

    }


}