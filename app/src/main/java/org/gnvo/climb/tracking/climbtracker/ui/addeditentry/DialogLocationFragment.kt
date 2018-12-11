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
import android.widget.*
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.dialog_location.view.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Location

class DialogLocationFragment : DialogFragment() {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        val regexLocationExtractor = """([+-]?(?:[0-9]*[.])?[0-9]+)\s*,\s*([+-]?(?:[0-9]*[.])?[0-9]+)""".toRegex()
    }

    private lateinit var location: Location

    private var availableLocations: List<Location>? = null

    private lateinit var mutableMapAvailableLocations: MutableMap<String, MutableMap<String, Location>>

    // Use this instance of the interface to deliver action events
    private var listener: DialogLocationListener? = null
    private lateinit var dialog: AlertDialog

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequest: LocationRequest? = null
    private var locationUpdateState = false

    private lateinit var imageButtonLookForCoordinates: ImageButton
    private lateinit var progressBarLocation: ProgressBar
    private lateinit var tietCoordinates: TextInputEditText
    private lateinit var autoCompleteTextViewArea: AutoCompleteTextView
    private lateinit var autoCompleteTextViewSector: AutoCompleteTextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = activity?.let { it ->
            val builder = AlertDialog.Builder(it)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = it.layoutInflater.inflate(R.layout.dialog_location, null)

            imageButtonLookForCoordinates = dialogView.image_button_look_for_coordinates
            progressBarLocation = dialogView.progress_bar_location
            tietCoordinates = dialogView.tiet_coordinates
            autoCompleteTextViewArea = dialogView.auto_complete_text_view_area
            autoCompleteTextViewSector = dialogView.auto_complete_text_view_sector

            builder.setView(dialogView)
                .setTitle("Title")
                .setPositiveButton(
                    R.string.create
                ) { _, _ ->
                    // Send the positive button event back to the host activity
                    val area = autoCompleteTextViewArea.text
                    val location = if (!area.isNullOrEmpty()) {
                        val location = Location(area = area.toString())
                        val (latitude, longitude) = extractCoordinates()
                        location.sector = Utils.getStringOrNull(autoCompleteTextViewSector.text)
                        location.latitude = latitude?.value?.toDouble()
                        location.longitude = longitude?.value?.toDouble()
                        location.locationId = mutableMapAvailableLocations[location.area]?.get(location.sector)?.locationId
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

            prepareGeo()
            imageButtonLookForCoordinates.setOnClickListener {
                createLocationRequest()
            }

            val dialog = builder.create()

            listener?.onDialogPopulate(dialogView)

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")


        return dialog
    }

    //Todo this method is being called when exiting the add/edit attempt activity, because the LiveData of this data is being updated. For now checking if the context is null
    private fun populateAreaAndSectors() {
        context?.let { context ->
            mutableMapAvailableLocations =
                    availableLocations?.map { it.area to mutableMapOf<String, Location>() }!!.toMap().toMutableMap()

            availableLocations?.let {
                for (availableLocation in it) {
                    val sector = availableLocation.sector ?: ""
                    mutableMapAvailableLocations[availableLocation.area]!![sector] = availableLocation
                }
            }

            val adapterAreas = ArrayAdapter<String>(
                context, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                mutableMapAvailableLocations.keys.toTypedArray() // Array
            )

            autoCompleteTextViewArea.setAdapter(adapterAreas)
            autoCompleteTextViewArea.threshold = 1
            autoCompleteTextViewArea.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) autoCompleteTextViewArea.showDropDown()
            }

            autoCompleteTextViewArea.setOnDismissListener {
                autoCompleteTextViewSector.setText("")
                tietCoordinates.setText("")
                setSectorAdapter(mutableMapAvailableLocations[autoCompleteTextViewArea.text.toString()])
            }

            autoCompleteTextViewSector.threshold = 1
            autoCompleteTextViewSector.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    autoCompleteTextViewSector.showDropDown()
                }
            }
            autoCompleteTextViewSector.setOnDismissListener {
                val l = mutableMapAvailableLocations[autoCompleteTextViewArea.text.toString()]?.get(
                    autoCompleteTextViewSector.text.toString()
                )

                l?.let {
                    tietCoordinates.setText(
                        getString(
                            R.string.coordinates_format,
                            l.latitude,
                            l.longitude
                        )
                    )
                }
            }
        }
    }

    private fun setSectorAdapter(mutableMapAvailableLocationSectors: MutableMap<String, Location>?) {
        mutableMapAvailableLocationSectors?.let {
            val adapterSector = ArrayAdapter<String>(
                context as Context,
                android.R.layout.simple_dropdown_item_1line,
                it.keys.toTypedArray()
            )
            autoCompleteTextViewSector.setAdapter(adapterSector)
        }
    }

    private fun extractCoordinates(): Pair<MatchGroup?, MatchGroup?> {
        val matchResult = regexLocationExtractor.find(tietCoordinates.text.toString())
        return Pair(matchResult?.groups?.get(1), matchResult?.groups?.get(2))
    }

    private fun prepareGeo() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                Toast.makeText(
                    activity,
                    "Coordinates accuracy: ${locationResult.lastLocation.accuracy}mts.",
                    Toast.LENGTH_SHORT
                ).show()

                if (locationResult.lastLocation.accuracy < 15) { //accuracy < 10mts
                    tietCoordinates.setText(
                        getString(
                            R.string.coordinates_format,
                            locationResult.lastLocation.latitude,
                            locationResult.lastLocation.longitude
                        )
                    )
                    stopLocationUpdates()
                    locationUpdateState = false
                }
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest!!.interval = 10000
        locationRequest!!.fastestInterval = 5000
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        val client = LocationServices.getSettingsClient(activity as Activity)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            Toast.makeText(
                activity,
                "Could not get location from GPS on device. Make sure GPS and mobile network are enabled, also to improve precision move the phone some meters to improve the accuracy. Error: $e",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startLocationUpdates() {
        imageButtonLookForCoordinates.visibility = View.GONE
        progressBarLocation.visibility = View.VISIBLE

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
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        imageButtonLookForCoordinates.visibility = View.VISIBLE
        progressBarLocation.visibility = View.GONE
    }

    interface DialogLocationListener {
        fun onDialogPositiveClick(location: Location?)
        fun onDialogPopulate(dialog: View)
    }

    fun setDialogLocationListener(listener: DialogLocationListener) {
        this.listener = listener
    }

    fun setLocations(locations: List<Location>?) {
        this.availableLocations = locations
        populateAreaAndSectors()
    }
}