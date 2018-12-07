package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_add_update_attempt.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Attempt
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.Location
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.RouteGrade
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters.GenericAdapter
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters.GenericAdapterMultipleSelection
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters.GenericAdapterSingleSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class AddEditAttemptActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID: String = "org.gnvo.climb.tracking.climbtracker.ui.addeditentry.EXTRA_ID"
        const val INVALID_ID: Long = -1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        val regexLocationExtractor = """([+-]?(?:[0-9]*[.])?[0-9]+)\s*,\s*([+-]?(?:[0-9]*[.])?[0-9]+)""".toRegex()
    }

    private lateinit var viewModel: AddEditViewModel
    private var formatterDate = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy")
    private var formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequest: LocationRequest? = null
    private var locationUpdateState = false

    private var attemptIdFromIntentExtra: Long = INVALID_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_attempt)

        viewModel = ViewModelProviders.of(this).get(AddEditViewModel::class.java)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        if (intent.hasExtra(EXTRA_ID)) {
            title = getString(R.string.edit_attempt)
            attemptIdFromIntentExtra = intent.getLongExtra(EXTRA_ID, INVALID_ID)
            restoreAttemptData()
        } else {
            title = getString(R.string.add_attempt)
            val now = LocalDateTime.now()
            button_date.text = now.format(formatterDate)
            button_time.text = now.format(formatterTime)
            partiallyRestoreAttemptDataFromLastAttemptEntry()
        }

        setDateTimeDialogs()

        setRouteGrades()

        setAdapterToRecyclerViewSingleSelection(
            recycler_view_climb_style,
            GenericAdapterSingleSelection<String>(),
            resources.getStringArray(R.array.climb_styles).toCollection(ArrayList())
        )

        setAdapterToRecyclerViewSingleSelection(
            recycler_view_outcome,
            GenericAdapterSingleSelection<String>(),
            resources.getStringArray(R.array.outcome).toCollection(ArrayList())
        )

        setAdapterToRecyclerViewSingleSelection(
            recycler_view_route_type,
            GenericAdapterSingleSelection<String>(),
            resources.getStringArray(R.array.route_type).toCollection(ArrayList())
        )
        setAdapterToRecyclerViewMultipleSelection(
            recycler_view_route_characteristics,
            GenericAdapterMultipleSelection<String>(),
            resources.getStringArray(R.array.route_characteristic).toCollection(ArrayList())
        )

        prepareGeo()
        image_button_look_for_coordinates.setOnClickListener {
            createLocationRequest()
        }
    }

    private fun setRouteGrades() {
        val routeGradeAdapter = GenericAdapterSingleSelection<RouteGrade>()
        routeGradeAdapter.setCustomFormatter(object : GenericAdapter.CustomFormatter<RouteGrade> {
            override fun format(item: RouteGrade): String {
                return "${item.french}"
            }
        })
        routeGradeAdapter.setScroller(object : GenericAdapterSingleSelection.Scroller {
            override fun scroll(selectedPosition: Int) {
                val position = when (selectedPosition) {
                    RecyclerView.NO_POSITION -> 9 //if nothing is selected scroll down a bit
                    else -> selectedPosition
                }
                (recycler_view_route_grade.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
            }
        })
        recycler_view_route_grade.layoutManager = LinearLayoutManager(this)
        recycler_view_route_grade.setHasFixedSize(true)

        recycler_view_route_grade.adapter = routeGradeAdapter

        viewModel.getAllRouteGrades().observe(this, Observer {
            routeGradeAdapter.setItems(ArrayList(it!!))
        })
    }

    private fun <T> setAdapterToRecyclerViewSingleSelection(
        recycler_view: RecyclerView,
        genericAdapter: GenericAdapterSingleSelection<T>,
        items: ArrayList<T>
    ) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        recycler_view.adapter = genericAdapter

        genericAdapter.setItems(items)
    }

    private fun <T> setAdapterToRecyclerViewMultipleSelection(
        recycler_view: RecyclerView,
        genericAdapter: GenericAdapterMultipleSelection<T>,
        items: ArrayList<T>
    ) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        recycler_view.adapter = genericAdapter

        genericAdapter.setItems(items)
    }

    private fun setDateTimeDialogs() {
        button_time.setOnClickListener {
            val time = LocalTime.parse(button_time.text, formatterTime)
            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    run {
                        button_time.text = LocalTime.of(hourOfDay, minute).format(formatterTime)
                    }
                },
                time.hour,
                time.minute,
                false
            )
            timePickerDialog.show()
        }
        button_date.setOnClickListener {
            val date = LocalDate.parse(button_date.text, formatterDate)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    button_date.text = LocalDate.of(year, monthOfYear + 1, dayOfMonth).format(formatterDate)
                },
                date.year,
                date.monthValue - 1,
                date.dayOfMonth
            )
            datePickerDialog.show()
        }
    }

    private fun restoreAttemptData() {
        viewModel.getAttemptWithGradesById(attemptIdFromIntentExtra)
            .observe(this, Observer { attemptWithGrades: AttemptWithGrades? ->
                button_date.text = attemptWithGrades?.attempt?.datetime!!.format(formatterDate)
                button_time.text = attemptWithGrades.attempt.datetime.format(formatterTime)

                (recycler_view_climb_style.adapter as GenericAdapterSingleSelection<String>).setSelected(
                    attemptWithGrades.attempt.climbStyle
                )
                (recycler_view_outcome.adapter as GenericAdapterSingleSelection<String>).setSelected(attemptWithGrades.attempt.outcome)
                (recycler_view_route_grade.adapter as GenericAdapterSingleSelection<RouteGrade>).setSelected(
                    attemptWithGrades.routeGrade
                )
                (recycler_view_route_type.adapter as GenericAdapterSingleSelection<String>).setSelected(
                    attemptWithGrades.attempt.routeType
                )
                attemptWithGrades.attempt.routeCharacteristics?.let {
                    (recycler_view_route_characteristics.adapter as GenericAdapterMultipleSelection<String>).setSelected(
                        it
                    )
                }

                edit_text_route_name.setText(attemptWithGrades.attempt.routeName)
                edit_text_length.setText(attemptWithGrades.attempt.length?.toString())
                edit_text_area.setText(attemptWithGrades.location?.area)
                edit_text_sector.setText(attemptWithGrades.location?.sector)
                if (attemptWithGrades.location?.latitude != null && attemptWithGrades.location.longitude != null)
                    edit_text_coordinates.setText(getString(R.string.coordinates_format, attemptWithGrades.location.latitude, attemptWithGrades.location.longitude))
                edit_text_comment.setText(attemptWithGrades.attempt.comment)

                rating_bar_rating.rating = attemptWithGrades.attempt.rating?.toFloat() ?: 0f
            })
    }

    private fun partiallyRestoreAttemptDataFromLastAttemptEntry() {
        viewModel.getLastAttemptWithGrades()
            .observe(this, Observer { attemptWithGrades: AttemptWithGrades? ->
                (recycler_view_climb_style.adapter as GenericAdapterSingleSelection<String>).setSelected(
                    attemptWithGrades?.attempt?.climbStyle
                )
                (recycler_view_outcome.adapter as GenericAdapterSingleSelection<String>).setSelected(attemptWithGrades?.attempt?.outcome)
                (recycler_view_route_grade.adapter as GenericAdapterSingleSelection<RouteGrade>).setSelected(
                    attemptWithGrades?.routeGrade
                )
                (recycler_view_route_type.adapter as GenericAdapterSingleSelection<String>).setSelected(
                    attemptWithGrades?.attempt?.routeType
                )

                edit_text_area.setText(attemptWithGrades?.location?.area)
                edit_text_sector.setText(attemptWithGrades?.location?.sector)
            }
            )
    }

    private fun saveAttempt() {
        val attempt = generateAttempt() ?: return
        when (attemptIdFromIntentExtra) {
            INVALID_ID -> {
                viewModel.insertAttempt(attempt)
                Toast.makeText(this, "Climb attempt created", Toast.LENGTH_LONG).show()
            }
            else -> {
                attempt.id = attemptIdFromIntentExtra
                viewModel.updateAttempt(attempt)
                Toast.makeText(this, "Climb attempt updated", Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }

    private fun generateAttempt(): Attempt? {
        val date = LocalDate.parse(button_date.text, formatterDate)
        val time = LocalTime.parse(button_time.text, formatterTime)
        val datetime = date.atTime(time)

        val climbStyle =
            (recycler_view_climb_style.adapter as GenericAdapterSingleSelection<String>).getSelected()
        val outcome =
            (recycler_view_outcome.adapter as GenericAdapterSingleSelection<String>).getSelected()
        val routeGradeId =
            (recycler_view_route_grade.adapter as GenericAdapterSingleSelection<RouteGrade>).getSelected()?.routeGradeId
                ?: INVALID_ID
        val routeType =
            (recycler_view_route_type.adapter as GenericAdapterSingleSelection<String>).getSelected()

        if (routeType == null) {
            Toast.makeText(this, "Set route type. Eg. Sport", Toast.LENGTH_LONG).show()
            return null
        }
        if (climbStyle == null) {
            Toast.makeText(this, "Set climb style. Eg. Lead", Toast.LENGTH_LONG).show()
            return null
        }
        if (routeGradeId == INVALID_ID) {
            Toast.makeText(this, "Set route grade. Eg. 7a", Toast.LENGTH_LONG).show()
            return null
        }
        if (outcome == null) {
            Toast.makeText(this, "Set outcome. Eg. Onsight", Toast.LENGTH_LONG).show()
            return null
        }

        val (latitude, longitude) = extractCoordinates()

        val attempt = Attempt(
            datetime = datetime,
            routeType = routeType,
            climbStyle = climbStyle,
            routeGrade = routeGradeId,
            outcome = outcome
        )
        val area = edit_text_area.text
        if (!area.isNullOrEmpty()) {
            val location = Location(area = edit_text_area.text.toString())
            location.sector = getStringOrNull(edit_text_sector.text)
            location.latitude = latitude?.value?.toDouble()
            location.longitude = longitude?.value?.toDouble()
            Log.d("gnvog", "location: $location")
        }

        attempt.routeName = getStringOrNull(edit_text_route_name.text)
        attempt.length = getStringOrNull(edit_text_length.text)?.toInt()

        attempt.comment = getStringOrNull(edit_text_comment.text)
        attempt.routeCharacteristics =
                (recycler_view_route_characteristics.adapter as GenericAdapterMultipleSelection<String>).getSelected()

        if (rating_bar_rating.rating > 0)
            attempt.rating = rating_bar_rating.rating.toInt()

        return attempt
    }

    private fun extractCoordinates(): Pair<MatchGroup?, MatchGroup?> {
        val matchResult = regexLocationExtractor.find(edit_text_coordinates.text.toString())
        return Pair(matchResult?.groups?.get(1), matchResult?.groups?.get(2))
    }

    private fun prepareGeo() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                if (p0.lastLocation.accuracy < 15) { //accuracy < 10mts
                    edit_text_coordinates.setText(
                        getString(
                            R.string.coordinates_format,
                            p0.lastLocation.latitude,
                            p0.lastLocation.longitude
                        )
                    )
                    Toast.makeText(
                        this@AddEditAttemptActivity,
                        "Coordinates accuracy: ${p0.lastLocation.accuracy}mts.",
                        Toast.LENGTH_LONG
                    ).show()
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
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            Toast.makeText(this, "Could not get location from GPS on device. Make sure GPS and mobile network are enabled, also to improve precision move the phone some meters to improve the accuracy. Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    private fun startLocationUpdates() {
        image_button_look_for_coordinates.visibility = View.GONE
        progress_bar_location.visibility = View.VISIBLE

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        image_button_look_for_coordinates.visibility = View.VISIBLE
        progress_bar_location.visibility = View.GONE
    }

    public override fun onResume() {
        super.onResume()
        if (locationUpdateState) {
            startLocationUpdates()
        }
    }

    private fun getStringOrNull(text: Editable?): String? {
        return when {
            text.isNullOrEmpty() -> null
            else -> return text.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_edit_attempt_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.save_attempt -> {
                saveAttempt()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
