package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
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

    // Use this instance of the interface to deliver action events
    private lateinit var listener: DialogLocationListener
    private lateinit var dialog: AlertDialog

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequest: LocationRequest? = null
    private var locationUpdateState = false

    private lateinit var imageButtonLookForCoordinates: ImageButton
    private lateinit var progressBarLocation: ProgressBar
    private lateinit var editTextCoordinates: TextInputEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = activity?.let { it ->
            val builder = AlertDialog.Builder(it)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = it.layoutInflater.inflate(R.layout.dialog_location, null)

            builder.setView(dialogView)
                .setTitle("Title")
                .setPositiveButton(
                    R.string.create
                ) { _, _ ->
                    // Send the positive button event back to the host activity
                    val area = dialogView.edit_text_area.text
                    val location = if (!area.isNullOrEmpty()) {
                        val location = Location(area = area.toString())
                        val (latitude, longitude) = extractCoordinates(dialogView.edit_text_coordinates)
                        location.sector = Utils.getStringOrNull(dialogView.edit_text_sector.text)
                        location.latitude = latitude?.value?.toDouble()
                        location.longitude = longitude?.value?.toDouble()
                        location
                    } else {null}
                    listener.onDialogPositiveClick(location)
                }
                .setNegativeButton(
                    R.string.cancel
                    , null
                )

            imageButtonLookForCoordinates = dialogView.image_button_look_for_coordinates
            progressBarLocation = dialogView.progress_bar_location
            editTextCoordinates = dialogView.edit_text_coordinates

            prepareGeo()
            imageButtonLookForCoordinates.setOnClickListener {
                createLocationRequest()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")


        return dialog
    }

    private fun extractCoordinates(editTextCoordinates: TextInputEditText): Pair<MatchGroup?, MatchGroup?> {
        val matchResult = regexLocationExtractor.find(editTextCoordinates.text.toString())
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
                    editTextCoordinates.setText(
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
                "Could not get location from GPS on device. Make sure GPS and mobile network are enabled, also to improve precision move the phone some meters to improve the accuracy",
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
    }

    fun setDialogLocationListener(listener: DialogLocationListener) {
        this.listener = listener
    }
}