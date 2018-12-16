package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.dialog_location.view.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Location
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.Utils.Companion.getStringOrNull


class DialogLocationFragment : DialogFragment(), OnMapReadyCallback {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var mMapView: MapView? = null

    private lateinit var map: GoogleMap

    // Use this instance of the interface to deliver action events
    private var listener: DialogLocationListener? = null
    private lateinit var dialog: AlertDialog

    private lateinit var tietCoordinates: TextInputEditText
    private lateinit var tietArea: TextInputEditText
    private lateinit var tietSector: TextInputEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = activity?.let { it ->
            val builder = AlertDialog.Builder(it)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = it.layoutInflater.inflate(R.layout.dialog_location, null)

            tietCoordinates = dialogView.tiet_coordinates
            tietArea = dialogView.d_tiet_area
            tietSector = dialogView.d_tiet_sector

            builder.setView(dialogView)
                .setPositiveButton(
                    R.string.create
                ) { _, _ ->
                    // Send the positive button event back to the host activity
                    val area = getStringOrNull(tietArea.text)!!
                    val sector = getStringOrNull(tietSector.text)
                    val coordinates = getStringOrNull(tietCoordinates.text)

                    val location = Location(area = area)
                    location.sector = sector
                    val (latitude, longitude) = Utils.extractCoordinates(coordinates ?: "")
                    location.sector = sector
                    location.latitude = latitude?.value?.toDouble()
                    location.longitude = longitude?.value?.toDouble()

                    listener?.onDialogPositiveClick(location)
                }
                .setNegativeButton(
                    R.string.cancel
                    , null
                )

            val dialog = builder.create()

            dialog.setTitle(listener?.getTitle())

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).text = listener?.getPositiveButtonText()
                if (tietArea.text.isNullOrEmpty()) {
                    tietArea.error = "Cannot be empty"
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                }
            }
            val location = listener?.getLocation()
            location?.let { l ->
                dialogView.d_tiet_area.setText(l.area)
                l.sector?.let { dialogView.d_tiet_sector.setText(it) }
                if (l.latitude != null && l.longitude != null) {
                    dialogView.tiet_coordinates.setText(
                        getString(
                            R.string.coordinates_format,
                            location.latitude,
                            location.longitude
                        )
                    )
                }
            }

            mMapView = dialogView.map_view_map
            mMapView?.onCreate(savedInstanceState)
            mMapView?.getMapAsync(this)

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")

        tietArea.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    tietArea.error = "Cannot be empty"
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                }
            }
        })
        return dialog
    }

    interface DialogLocationListener {
        fun getTitle(): String
        fun getLocation(): Location?
        fun getPositiveButtonText(): String
        fun onDialogPositiveClick(location: Location)
    }

    fun setDialogLocationListener(listener: DialogLocationListener) {
        this.listener = listener
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
        if (latitude != null && longitude != null) {
            val coordinates = LatLng(latitude.value.toDouble(), longitude.value.toDouble())
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12f))
        }

        map.setOnCameraMoveListener {
            tietCoordinates.setText(
                getString(
                    R.string.coordinates_format,
                    googleMap.cameraPosition.target.latitude,
                    googleMap.cameraPosition.target.longitude
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