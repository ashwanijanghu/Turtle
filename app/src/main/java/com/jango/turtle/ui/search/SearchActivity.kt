package com.jango.turtle.ui.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.jango.turtle.AppExecutors
import com.jango.turtle.BaseActivity
import com.jango.turtle.R
import com.jango.turtle.TurtleApp
import com.jango.turtle.binding.CustomDataBindingComponent
import com.jango.turtle.databinding.ActivitySearchBinding
import com.jango.turtle.di.Injectable
import com.jango.turtle.ui.common.PlaceDetails
import com.jango.turtle.ui.detail.DetailsActivity
import com.jango.turtle.util.TConstants
import com.jango.turtle.util.autoClearedActivity
import com.jango.turtle.vo.Places
import com.jango.turtle.vo.Status
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject
/**
 * Created by Ashwani on 11/06/18.
 */
class SearchActivity :
        BaseActivity(),
        Injectable,
        FavListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener,
        GoogleMap.OnMapLoadedCallback
{
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var map: GoogleMap

    var dataBindingComponent: DataBindingComponent = CustomDataBindingComponent(this, Fragment())

    var binding by autoClearedActivity<ActivitySearchBinding>()

    var adapter by autoClearedActivity<PlacesListAdapter>()

    lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_search,
                dataBindingComponent
        )
        binding.searchResultView.visibility = View.GONE
        searchViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SearchViewModel::class.java)
        searchViewModel.resources = resources
        setToolbar()

        fab.setOnClickListener { view ->
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/
            if(mapView.visibility == View.GONE){
                mapView.visibility = View.VISIBLE
                listView.visibility = View.GONE
                if(searchViewModel.placesList.isNotEmpty()) {
                    //addMarkersToMap(searchViewModel.placesList)
                    //onMapReady(map)
                }
            }
        }

        fabMap.setOnClickListener { view ->
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/
            if(listView.visibility == View.GONE){
                mapView.visibility = View.GONE
                listView.visibility = View.VISIBLE
            }
        }

        iv_night_mode.setOnClickListener {
            (this.application as TurtleApp).toggleNightMode()
            recreate()
        }

        helpList.layoutManager = GridLayoutManager(this, 4,GridLayout.VERTICAL, false)
        helpList.addItemDecoration(GridSpacingItemDecoration(4, resources.getDimensionPixelSize(R.dimen.dp16), false))
        setHelpData()
        initPlacesRecyclerView()
        val placesListAdapter = PlacesListAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                favListener = this
        ){ place ->
            //Toast.makeText(this,"Clicked  "+place.venue.name,Toast.LENGTH_SHORT).show()
            openDetailActivity(place.id,place.venue.name)
        }
        binding.resultList.adapter = placesListAdapter
        adapter = placesListAdapter
        initSearchInputListener()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.placesMap) as SupportMapFragment
        OnMapAndViewReadyListener(mapFragment, this)
    }

    override fun onResume() {
        super.onResume()
        binding.resultList.adapter.notifyDataSetChanged()
    }

    private fun initSearchInputListener() {
        binding.searchView.setOnClickListener(View.OnClickListener {
            binding.searchView.isIconified = false;
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                doSearch(query)
                return false
            }
        })
    }

    private fun doSearch(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.searchResultView.visibility = View.GONE
        searchViewModel.setPlacesQuery(query)
    }

    private fun initPlacesRecyclerView() {
        searchViewModel.placesResult.observe(this, Observer { result->
            if(result!!.status == Status.SUCCESS) {
                binding.progressBar.visibility = View.GONE
                if(result?.data != null) {
                    val count = result?.data?.size
                    if (count!! > 0) {
                        searchViewModel.placesList = result.data
                        binding.searchResultView.visibility = View.VISIBLE
                        binding.searchHelpView.visibility = View.GONE
                        Log.d("ashwani", "places size : $count")
                        binding.searchResource = result
                        adapter.submitList(searchViewModel.placesList)
                    } else {
                        Toast.makeText(this,"No places found for ${searchViewModel.placesQuery.value}",Toast.LENGTH_SHORT).show()
                        binding.searchHelpView.visibility = View.VISIBLE
                    }
                } else{
                    Toast.makeText(this,"No places found for ${searchViewModel.placesQuery.value}",Toast.LENGTH_SHORT).show()
                    binding.searchHelpView.visibility = View.VISIBLE
                }
            } else if(result!!.status == Status.LOADING){
                binding.progressBar.visibility = View.VISIBLE
            } else{
                Toast.makeText(this,"No places found for ${searchViewModel.placesQuery.value}",Toast.LENGTH_SHORT).show()
                binding.searchHelpView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

        })
    }

    private fun setHelpData() {
        //crating an arraylist to store helpOptions using the data class user
        val helpOptions = ArrayList<HelpOption>()

        //adding some dummy data to the list
        helpOptions.add(HelpOption(R.drawable.ic_cake,"Cake","Cake"))
        helpOptions.add(HelpOption(R.drawable.ic_cappuccino,"Cappuccino","Cappuccino"))
        helpOptions.add(HelpOption(R.drawable.ic_cheesecake,"Cheesecake","Cheesecake"))
        helpOptions.add(HelpOption(R.drawable.ic_coffee,"Coffee","Coffee"))
        helpOptions.add(HelpOption(R.drawable.ic_donut,"Donut","Donut"))
        helpOptions.add(HelpOption(R.drawable.ic_ice_cream,"Ice Cream","Ice Cream"))
        helpOptions.add(HelpOption(R.drawable.ic_latte,"Latte","Latte"))
        helpOptions.add(HelpOption(R.drawable.ic_waffle,"Waffle","Waffle"))
        helpOptions.add(HelpOption(R.drawable.ic_chicken,"Chicken","Chicken"))
        helpOptions.add(HelpOption(R.drawable.ic_beer,"Beer","Beer"))
        helpOptions.add(HelpOption(R.drawable.ic_hot_dog,"Hot Dog","Hot Dog"))
        helpOptions.add(HelpOption(R.drawable.ic_fish,"Fish","Fish"))

        //creating our adapter
        val adapter = HelpListAdapter(
                this,helpOptions
        ) { option ->
            //Toast.makeText(this,"Clicked  "+option.name,Toast.LENGTH_SHORT).show()
            binding.searchView.setQuery("", false)
            binding.searchView.clearFocus()
            doSearch(option.query)
        }

        //now adding the adapter to recyclerview
        helpList.adapter = adapter
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_home)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar_home.navigationIcon = null
    }

    override fun addFav(place: Places?) {
        //Toast.makeText(this,"Fav added  "+place?.venue?.name,Toast.LENGTH_SHORT).show()
        searchViewModel.updateFav(true,place?.id)
    }

    override fun removeFav(place: Places?) {
        //Toast.makeText(this,"Fav removed  "+place?.venue?.name,Toast.LENGTH_SHORT).show()
        searchViewModel.updateFav(false,place?.id)
    }

    internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {

        // These are both view groups containing an ImageView with id "badge" and two
        // TextViews with id "title" and "snippet".
        private val window: View = layoutInflater.inflate(R.layout.custom_info_window, null)
        private val contents: View = layoutInflater.inflate(R.layout.custom_info_contents, null)

        override fun getInfoWindow(marker: Marker): View? {
             render(marker, window)
            return window
        }

        override fun getInfoContents(marker: Marker): View? {
            render(marker, contents)
            return contents
        }

        private fun render(marker: Marker, view: View) {

            // Set the title and snippet for the custom info window
            val title: String? = marker.title
            val titleUi = view.findViewById<TextView>(R.id.title)

            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                titleUi.text = SpannableString(title).apply {
                    setSpan(ForegroundColorSpan(resources.getColor(R.color.text_grey_heading)), 0, length, 0)
                }
            } else {
                titleUi.text = ""
            }

            val snippet: String? = marker.snippet
            val snippetUi = view.findViewById<TextView>(R.id.snippet)
            snippetUi.text = snippet
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.zIndex = marker?.zIndex?.plus(1.0f)!!
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val duration = 1500

        val interpolator = BounceInterpolator()

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = Math.max(
                        1 - interpolator.getInterpolation(elapsed.toFloat() / duration), 0f)
                marker?.setAnchor(0.5f, 1.0f + 2 * t)

                // Post again 16ms later.
                if (t > 0.0) {
                    handler.postDelayed(this, 16)
                }
            }
        })
        return false
    }

    override fun onMarkerDragEnd(p0: Marker?) {
    }

    override fun onMarkerDragStart(p0: Marker?) {
    }

    override fun onMarkerDrag(p0: Marker?) {
    }

    override fun onInfoWindowClick(marker : Marker) {
        //Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show()
        var placeDetails = searchViewModel.markerTracker[marker.id]
        if(placeDetails != null) {
            openDetailActivity(placeDetails.id, placeDetails.title)
        }
    }

    override fun onInfoWindowClose(marker : Marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show()
    }

    override fun onInfoWindowLongClick(marker : Marker) {
        //Toast.makeText(this, "Info Window long click", Toast.LENGTH_SHORT).show()
        var placeDetails = searchViewModel.markerTracker[marker.id]
        if(placeDetails != null) {
            openDetailActivity(placeDetails.id, placeDetails.title)
        }
    }

    override fun onMapLoaded() {
        Log.d("Ashwani","map loaded")
        if(searchViewModel.placesList.isNotEmpty()) {
            addMarkersToMap(searchViewModel.placesList)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return

        with(map) {
            // Hide the zoom controls as the button panel will cover it.
            uiSettings.isZoomControlsEnabled = false

            // Setting an info window adapter allows us to change the both the contents and
            // look of the info window.
            setInfoWindowAdapter(CustomInfoWindowAdapter())

            // Set listeners for marker events.  See the bottom of this class for their behavior.
            setOnMarkerClickListener(this@SearchActivity)
            setOnInfoWindowClickListener(this@SearchActivity)
            setOnMarkerDragListener(this@SearchActivity)
            setOnInfoWindowCloseListener(this@SearchActivity)
            setOnInfoWindowLongClickListener(this@SearchActivity)
            setOnMapLoadedCallback(this@SearchActivity)

            // Override the default content description on the view, for accessibility mode.
            // Ideally this string would be localised.
            setContentDescription("Map with lots of markers.")
        }

        // include all places we have markers for on the map
        /*if(searchViewModel.placesList != null && searchViewModel.placesList.isNotEmpty()){
            addMarkersToMap(searchViewModel.placesList)
        }*/
    }

    fun addMarkersToMap(data: List<Places>) {
        // create bounds that encompass every location we reference
        val boundsBuilder = LatLngBounds.Builder()

        data.map { place -> boundsBuilder.include(LatLng(place.venue.location.lat!!,place.venue.location.lng)) }
        val bounds = boundsBuilder.build()
        with(map){
            moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
        }
        val list : MutableList<PlaceDetails> = mutableListOf<PlaceDetails>()
        for (place in data){
            list.add(PlaceDetails(place.id,LatLng(place.venue.location.lat!!,place.venue.location.lng),
                    place.venue.name,
                    place.getDistance()))
        }

        //add Seattle center as marker on map
        var seattlePD : PlaceDetails = PlaceDetails(TConstants.SEATTLLE_ID,
            LatLng(resources.getString(R.string.seattle_lat).toDouble(), resources.getString(R.string.seattle_long).toDouble()),
                "Seattle","Seattle Center",
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        map.addMarker(MarkerOptions()
                .position(seattlePD.position)
                .title(seattlePD.title)
                .snippet(seattlePD.snippet)
                .icon(seattlePD.icon)
                .infoWindowAnchor(seattlePD.infoWindowAnchorX, seattlePD.infoWindowAnchorY)
                .draggable(seattlePD.draggable)
                .zIndex(seattlePD.zIndex))

        for (placeDetails in list){
            var marker = map.addMarker(MarkerOptions()
                    .position(placeDetails.position)
                    .title(placeDetails.title)
                    .snippet("Distance : "+placeDetails.snippet)
                    .icon(placeDetails.icon)
                    .infoWindowAnchor(placeDetails.infoWindowAnchorX, placeDetails.infoWindowAnchorY)
                    .draggable(placeDetails.draggable)
                    .zIndex(placeDetails.zIndex))
            searchViewModel.markerTracker.put(marker.id,placeDetails)
        }
    }

    fun openDetailActivity(placeId : String, placeName:String){
        var intent:Intent = Intent(this,DetailsActivity::class.java)
        intent.putExtra(TConstants.EXTRA_DATA_1,placeId)
        intent.putExtra(TConstants.EXTRA_DATA_2,placeName)
        startActivity(intent)
    }
}
