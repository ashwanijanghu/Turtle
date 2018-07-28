package com.jango.turtle.ui.detail

import android.app.PendingIntent.getActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.jango.turtle.AppExecutors
import com.jango.turtle.BaseActivity
import com.jango.turtle.R
import com.jango.turtle.binding.CustomDataBindingComponent
import com.jango.turtle.databinding.ActivityDetailsBinding
import com.jango.turtle.di.Injectable
import com.jango.turtle.ui.common.PlaceDetails
import com.jango.turtle.ui.search.FavListener
import com.jango.turtle.ui.search.OnMapAndViewReadyListener
import com.jango.turtle.util.TConstants
import com.jango.turtle.util.autoClearedActivity
import com.jango.turtle.vo.Places
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject
import android.support.design.widget.AppBarLayout
import android.support.annotation.ColorInt
import android.content.res.Resources.Theme
import android.util.TypedValue





/**
 * Created by Ashwani on 18/06/18.
 */
class DetailsActivity : BaseActivity(),
        Injectable,
        FavListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener,
        GoogleMap.OnMapLoadedCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var map: GoogleMap

    lateinit var detailsViewModel: DetailsViewModel
    var dataBindingComponent: DataBindingComponent = CustomDataBindingComponent(this, Fragment())

    var binding by autoClearedActivity<ActivityDetailsBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_details)
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_details,
                dataBindingComponent
        )
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        detailsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DetailsViewModel::class.java)
        detailsViewModel.resources = resources
        init()
        favFab.setOnClickListener { view ->
            if(favFab.isSelected){
                favFab.isSelected = false
                detailsViewModel.updateFav(false,detailsViewModel.placesDetail?.id)
            } else{
                favFab.isSelected = true
                detailsViewModel.updateFav(true,detailsViewModel.placesDetail?.id)
            }
        }
        if(intent != null) {
            var name = intent.getStringExtra(TConstants.EXTRA_DATA_2)
            toolbar_layout.title = if (name.isBlank()) "Place Name" else name
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.detailsMap) as SupportMapFragment
        OnMapAndViewReadyListener(mapFragment, this)

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                //  Collapsed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = resources.getColor(R.color.app_red_dark)
                }

            } else {
                //Expanded
                setDefaultColor()

            }
        })
    }

    fun setDefaultColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val typedValue = TypedValue()
            theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)
            @ColorInt val color = typedValue.data
            window.statusBarColor = color
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                setDefaultColor()
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        setDefaultColor()
        super.onBackPressed()
    }

    fun init(){
        if(intent != null) run {
            var placeId: String = intent.getStringExtra(TConstants.EXTRA_DATA_1)
            detailsViewModel.setPlaceId(placeId)
        }
        detailsViewModel.placeData.observe(this, Observer {
            detailsViewModel.placesDetail = it!!
            binding.placeDetail = it
            favFab.isSelected = it.isFav
        })
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

    override fun addFav(place: Places?) {
    }

    override fun removeFav(place: Places?) {
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

    override fun onMarkerDragEnd(p0: Marker?) {
    }

    override fun onMarkerDragStart(p0: Marker?) {
    }

    override fun onMarkerDrag(p0: Marker?) {
    }

    override fun onInfoWindowClick(p0: Marker?) {
    }

    override fun onInfoWindowLongClick(p0: Marker?) {
    }

    override fun onInfoWindowClose(p0: Marker?) {
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
            setOnMarkerClickListener(this@DetailsActivity)
            setOnInfoWindowClickListener(this@DetailsActivity)
            setOnMarkerDragListener(this@DetailsActivity)
            setOnInfoWindowCloseListener(this@DetailsActivity)
            setOnInfoWindowLongClickListener(this@DetailsActivity)
            setOnMapLoadedCallback(this@DetailsActivity)

            // Override the default content description on the view, for accessibility mode.
            // Ideally this string would be localised.
            setContentDescription("Map with lots of markers.")
        }
    }

    override fun onMapLoaded() {
        addMarkersToMap(detailsViewModel.placesDetail!!)
    }

    fun addMarkersToMap(place: Places) {
        // create bounds that encompass every location we reference
        val boundsBuilder = LatLngBounds.Builder()
        boundsBuilder.include(LatLng(place.venue.location.lat!!,place.venue.location.lng))
        boundsBuilder.include(LatLng(resources.getString(R.string.seattle_lat).toDouble(),
                resources.getString(R.string.seattle_long).toDouble()))
        val bounds = boundsBuilder.build()
        with(map){
            moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
        }


        var thisPlaceDetails = PlaceDetails(place.id,
                LatLng(place.venue.location.lat,place.venue.location.lng),
                place.venue.name,place.venue.location.distance.toString())

        //add Seattle center as marker on map
        var seattlePD : PlaceDetails = PlaceDetails(TConstants.SEATTLLE_ID,
                LatLng(resources.getString(R.string.seattle_lat).toDouble(), resources.getString(R.string.seattle_long).toDouble()),
                "Seattle", "Seattle Center",
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        map.addMarker(MarkerOptions()
                .position(seattlePD.position)
                .title(seattlePD.title)
                .snippet(seattlePD.snippet)
                .icon(seattlePD.icon)
                .infoWindowAnchor(seattlePD.infoWindowAnchorX, seattlePD.infoWindowAnchorY)
                .draggable(seattlePD.draggable)
                .zIndex(seattlePD.zIndex))

        map.addMarker(MarkerOptions()
                .position(thisPlaceDetails.position)
                .title(thisPlaceDetails.title)
                .snippet("Distance : "+thisPlaceDetails.snippet)
                .icon(thisPlaceDetails.icon)
                .infoWindowAnchor(thisPlaceDetails.infoWindowAnchorX, thisPlaceDetails.infoWindowAnchorY)
                .draggable(thisPlaceDetails.draggable)
                .zIndex(thisPlaceDetails.zIndex))
        }
}
