package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.dialog_location.view.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Location


class DialogLocationFragment : DialogFragment(), OnMapReadyCallback {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var mMapView: MapView? = null

    private lateinit var map: GoogleMap

    private var locationsHierarchical: Map<String, Map<String, Location>>? = null

    // Use this instance of the interface to deliver action events
    private var listener: DialogLocationListener? = null
    private lateinit var dialog: AlertDialog

    private lateinit var tietCoordinates: TextInputEditText
    private lateinit var autoCompleteTextViewArea: AutoCompleteTextView
    private lateinit var autoCompleteTextViewSector: AutoCompleteTextView

    private var marker: Marker? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = activity?.let { it ->
            val builder = AlertDialog.Builder(it)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = it.layoutInflater.inflate(R.layout.dialog_location, null)

            tietCoordinates = dialogView.tiet_coordinates
            autoCompleteTextViewArea = dialogView.auto_complete_text_view_area
            autoCompleteTextViewSector = dialogView.auto_complete_text_view_sector

            builder.setView(dialogView)
                .setTitle(getString(R.string.location_add_edit))
                .setPositiveButton(
                    R.string.create
                ) { _, _ ->
                    // Send the positive button event back to the host activity
                    val area = autoCompleteTextViewArea.text
                    val location = if (!area.isNullOrEmpty()) {
                        val location = Location(area = area.toString())
                        val (latitude, longitude) = Utils.extractCoordinates(tietCoordinates.text.toString())
                        location.sector = Utils.getStringOrNull(autoCompleteTextViewSector.text)
                        location.latitude = latitude?.value?.toDouble()
                        location.longitude = longitude?.value?.toDouble()
                        location.locationId =
                                locationsHierarchical?.get(location.area)?.get(location.sector)?.locationId
                        location
                    } else {
                        null
                    }
                    listener?.onDialogPositiveClick(location)
                }
                .setNegativeButton(
                    R.string.cancel
                    , null
                )

            val dialog = builder.create()

            listener?.onDialogPopulate(dialogView)

            mMapView = dialogView.map_view_map
            mMapView?.onCreate(savedInstanceState)
            mMapView?.getMapAsync(this)

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")


        return dialog
    }

    //Todo this method is being called when exiting the add/edit attempt activity, because the LiveData of this data is being updated. For now checking if the context is null
    private fun populateAreaAndSectors() {
        context?.let { context ->
            locationsHierarchical?.let { locations ->

            val adapterAreas = ArrayAdapter<String>(
                context, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                locations.keys.toTypedArray() // Array
            )

            autoCompleteTextViewArea.setAdapter(adapterAreas)
            autoCompleteTextViewArea.threshold = 1
            autoCompleteTextViewArea.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) autoCompleteTextViewArea.showDropDown()
            }

            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

            autoCompleteTextViewArea.setOnItemClickListener { _, view, _, _ ->
                autoCompleteTextViewSector.setText("")
                setSectorAdapter(locations[autoCompleteTextViewArea.text.toString()])
                imm!!.hideSoftInputFromWindow(view.applicationWindowToken, 0)
            }

            autoCompleteTextViewSector.threshold = 1
            autoCompleteTextViewSector.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    autoCompleteTextViewSector.showDropDown()
                }
            }
            autoCompleteTextViewSector.setOnItemClickListener { _, view, _, _ ->
                imm!!.hideSoftInputFromWindow(view.applicationWindowToken, 0)
                val l = locations[autoCompleteTextViewArea.text.toString()]?.get(
                    autoCompleteTextViewSector.text.toString()
                )

                l?.let {
                    l.latitude?.let { latitude ->
                        l.longitude?.let { longitude ->
                            tietCoordinates.setText(
                                getString(
                                    R.string.coordinates_format,
                                    latitude,
                                    longitude
                                )
                            )
                            val position = LatLng(latitude, longitude)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12f))
                            marker?.let { marker ->
                                marker.position = position
                            }
                        }
                    }
                }
            }
            }
        }
    }

    private fun setSectorAdapter(mutableMapAvailableLocationSectors: Map<String, Location>?) {
        mutableMapAvailableLocationSectors?.let {
            val adapterSector = ArrayAdapter<String>(
                context as Context,
                android.R.layout.simple_dropdown_item_1line,
                it.keys.toTypedArray()
            )
            autoCompleteTextViewSector.setAdapter(adapterSector)
        }
    }

    interface DialogLocationListener {
        fun onDialogPositiveClick(location: Location?)
        fun onDialogPopulate(dialog: View)
    }

    fun setDialogLocationListener(listener: DialogLocationListener) {
        this.listener = listener
    }

    fun setLocations(locations: Map<String, Map<String, Location>>?) {
        this.locationsHierarchical = locations
        populateAreaAndSectors()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        if (ActivityCompat.checkSelfPermission(
                activity as Activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        map.isMyLocationEnabled = true
        map.uiSettings.isZoomControlsEnabled = true

        val (latitude, longitude) = Utils.extractCoordinates(tietCoordinates.text.toString())
        marker = if (latitude != null && longitude != null) {
            val coordinates = LatLng(latitude.value.toDouble(), longitude.value.toDouble())
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12f))
            map.addMarker(MarkerOptions().position(coordinates))
        } else {
            map.addMarker(MarkerOptions().position(googleMap.cameraPosition.target))
        }

        map.setOnCameraMoveListener {
            marker!!.position = googleMap.cameraPosition.target
            tietCoordinates.setText(
                getString(
                    R.string.coordinates_format,
                    marker!!.position.latitude,
                    marker!!.position.longitude
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }
}