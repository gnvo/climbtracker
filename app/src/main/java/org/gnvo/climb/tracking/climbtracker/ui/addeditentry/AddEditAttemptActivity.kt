package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_update_attempt.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters.GenericAdapterMultipleSelection
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters.GenericAdapterSingleSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class AddEditAttemptActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID: String = "org.gnvo.climb.tracking.climbtracker.ui.addeditentry.EXTRA_ID"
        const val INVALID_ID: Long = -1

        const val SAVED_POSITION_CLIMB_STYLES = "position_climb_styles"
        const val SAVED_POSITION_OUTCOME = "position_outcome"
        const val SAVED_POSITION_ROUTE_GRADE = "position_route_grade"
        const val SAVED_POSITION_ROUTE_TYPE = "position_route_type"
        const val SAVED_POSITION_ROUTE_CHARACTERISTICS = "position_route_characteristics"
    }

    private lateinit var viewModel: AddEditViewModel
    private var formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss VV")

    private var attemptIdFromIntentExtra: Long = INVALID_ID

    private var locations: Map<String, Map<String, Location>>? = null

    private var isRestored = false

    private var dialogLocationFragment: DialogLocationFragment? = null

    private lateinit var adapterClimbStyles: GenericAdapterSingleSelection
    private lateinit var adapterOutcome: GenericAdapterSingleSelection
    private lateinit var adapterRouteType: GenericAdapterSingleSelection
    private lateinit var adapterRouteGrade: GenericAdapterSingleSelection
    private lateinit var adapterRouteCharacteristics: GenericAdapterMultipleSelection

    private var savedInstanceStateClimbStyles: String? = null
    private var savedInstanceStateOutcome: String? = null
    private var savedInstanceStateRouteGrade: String? = null
    private var savedInstanceStateRouteType: String? = null
    private var savedInstanceStateRouteCharacteristics: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_attempt)

        viewModel = ViewModelProviders.of(this).get(AddEditViewModel::class.java)

        savedInstanceStateClimbStyles = savedInstanceState?.getString(SAVED_POSITION_CLIMB_STYLES)
        savedInstanceStateOutcome = savedInstanceState?.getString(SAVED_POSITION_OUTCOME)
        savedInstanceStateRouteGrade = savedInstanceState?.getString(SAVED_POSITION_ROUTE_GRADE)
        savedInstanceStateRouteType = savedInstanceState?.getString(SAVED_POSITION_ROUTE_TYPE)
        savedInstanceStateRouteCharacteristics =
                savedInstanceState?.getStringArrayList(SAVED_POSITION_ROUTE_CHARACTERISTICS)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        if (intent.hasExtra(EXTRA_ID)) {
            title = getString(R.string.edit_attempt)
            attemptIdFromIntentExtra = intent.getLongExtra(EXTRA_ID, INVALID_ID)
            restoreAttemptData()
        } else {
            title = getString(R.string.add_attempt)

            val currentDateTime = ZonedDateTime.now()

            text_view_date_time.text = formatterDateTime.format(currentDateTime)
            partiallyRestoreAttemptDataFromLastAttemptEntry()
        }

        setDateTimeDialogs()

        adapterClimbStyles = GenericAdapterSingleSelection()
        setAdapterToRecyclerViewSingleSelection(
            recycler_view_climb_style,
            adapterClimbStyles,
            resources.getStringArray(R.array.climb_styles).toCollection(ArrayList())
        )

        adapterOutcome = GenericAdapterSingleSelection()
        setAdapterToRecyclerViewSingleSelection(
            recycler_view_outcome,
            adapterOutcome,
            resources.getStringArray(R.array.outcome).toCollection(ArrayList())
        )

        adapterRouteType = GenericAdapterSingleSelection()
        setAdapterToRecyclerViewSingleSelection(
            recycler_view_route_type,
            adapterRouteType,
            resources.getStringArray(R.array.route_type).toCollection(ArrayList())
        )

        adapterRouteGrade = GenericAdapterSingleSelection()
        setAdapterToRecyclerViewSingleSelection(
            recycler_view_route_grade,
            adapterRouteGrade,
            viewModel.getAllRouteGrades().keys.toCollection(ArrayList())
        )
        adapterRouteGrade.setScroller(object : GenericAdapterSingleSelection.Scroller {
            override fun scroll(selectedPosition: Int) {
                (recycler_view_route_grade.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    selectedPosition,
                    0
                )
            }
        })

        adapterRouteCharacteristics = GenericAdapterMultipleSelection()
        setAdapterToRecyclerViewMultipleSelection(
            recycler_view_route_characteristics,
            adapterRouteCharacteristics,
            resources.getStringArray(R.array.route_characteristic).toCollection(ArrayList())
        )

        locationPreparation()
    }

    private fun locationPreparation() {
        button_add_location.setOnClickListener {
            dialogLocationFragment = DialogLocationFragment()
            dialogLocationFragment!!.setDialogLocationListener(object : DialogLocationFragment.DialogLocationListener {
                override fun getPositiveButtonText(): String {
                    return getString(R.string.create)
                }

                override fun getTitle(): String {
                    return getString(R.string.add_location)
                }

                override fun getLocation(): Location? {
                    return Location(area = tiet_area.text.toString())
                }

                override fun onDialogPositiveClick(location: Location) {
                    tiet_area.setText(location.area)
                    val sector: String = location.sector ?: ""
                    tiet_sector.setText(sector)

                    viewModel.insertLocation(location)
                }
            })
            dialogLocationFragment!!.show(supportFragmentManager, "DialogLocation")
        }

        button_edit_location.isEnabled = false
        button_edit_location.setImageResource(R.drawable.ic_edit_grayedout)
        button_edit_location.setOnClickListener {
            var locationId: Long? = null
            dialogLocationFragment = DialogLocationFragment()
            dialogLocationFragment!!.setDialogLocationListener(object : DialogLocationFragment.DialogLocationListener {
                override fun getPositiveButtonText(): String {
                    return getString(R.string.update)
                }

                override fun getTitle(): String {
                    return getString(R.string.edit_location)
                }

                override fun getLocation(): Location? {
                    val location = locations?.get(tiet_area.text.toString())?.get(tiet_sector.text.toString())
                    locationId = location?.locationId
                    return location
                }

                override fun onDialogPositiveClick(location: Location) {
                    tiet_area.setText(location.area)
                    val sector: String = location.sector ?: ""
                    tiet_sector.setText(sector)

                    location.locationId = locationId
                    viewModel.updateLocation(location)
                }
            })
            dialogLocationFragment!!.show(supportFragmentManager, "DialogLocation")
        }

        viewModel.getAllLocations().observe(this, Observer { locations ->
            this.locations = locations
        })

        tiet_area.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                button_edit_location.isEnabled = true
                button_edit_location.setImageResource(R.drawable.ic_edit)
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        })

        tiet_area.setOnClickListener {
            val areas = locations?.keys?.toTypedArray() ?: arrayOf()
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.area)
                .setItems(areas) { _, which ->
                    tiet_area.setText(areas[which])
                    val sector = locations?.get(areas[which])?.keys?.toList()?.first()
                    tiet_sector.setText(sector)
                }
            builder.create()
            builder.show()
        }

        tiet_sector.setOnClickListener {
            val area = tiet_area.text.toString()
            val sectors =
                locations?.get(area)?.keys?.toTypedArray() ?: arrayOf()
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.sector)
                .setItems(sectors) { _, which ->
                    tiet_sector.setText(sectors[which])
                }
            builder.create()
            builder.show()
        }

        tryNumberButtonsListeners()
    }

    private fun tryNumberButtonsListeners() {
        button_try_plus.setOnClickListener {
            val newNumber = edit_text_try_number.text.toString().toInt() + 1
            edit_text_try_number.setText(newNumber.toString(), TextView.BufferType.EDITABLE)
        }
        button_try_minus.setOnClickListener {
            val newNumber = edit_text_try_number.text.toString().toInt() - 1
            edit_text_try_number.setText(newNumber.toString(), TextView.BufferType.EDITABLE)
        }
    }

    private fun setAdapterToRecyclerViewSingleSelection(
        recycler_view: RecyclerView,
        genericAdapter: GenericAdapterSingleSelection,
        items: ArrayList<String>
    ) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        recycler_view.adapter = genericAdapter

        genericAdapter.setItems(items)
    }

    private fun setAdapterToRecyclerViewMultipleSelection(
        recycler_view: RecyclerView,
        genericAdapter: GenericAdapterMultipleSelection,
        items: ArrayList<String>
    ) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        recycler_view.adapter = genericAdapter

        genericAdapter.setItems(items)
    }

    private fun setDateTimeDialogs() {
        button_image_date.setOnClickListener {
            var zonedDateTime = ZonedDateTime.parse(text_view_date_time.text, formatterDateTime)
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    zonedDateTime = zonedDateTime.with(LocalDate.of(year, monthOfYear + 1, dayOfMonth))
                    text_view_date_time.text = formatterDateTime.format(zonedDateTime)
                },
                zonedDateTime.year,
                zonedDateTime.monthValue - 1,
                zonedDateTime.dayOfMonth
            ).show()
        }
        button_image_time.setOnClickListener {
            var zonedDateTime = ZonedDateTime.parse(text_view_date_time.text, formatterDateTime)
            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    run {
                        zonedDateTime = zonedDateTime.with(LocalTime.of(hourOfDay, minute))
                        text_view_date_time.text = formatterDateTime.format(zonedDateTime)
                    }
                },
                zonedDateTime.hour,
                zonedDateTime.minute,
                false
            )
            timePickerDialog.show()
        }
    }

    private fun restoreAttemptData() {
        viewModel.getAttemptWithLocationById(attemptIdFromIntentExtra)
            .observe(this, Observer { attemptWithLocation: AttemptWithLocation? ->
                if (!isRestored) {
                    isRestored = true
                    val storedZonedDateTime =
                        attemptWithLocation!!.attempt.instantAndZoneId.instant.atZone(attemptWithLocation.attempt.instantAndZoneId.zoneId)
                    text_view_date_time.text = formatterDateTime.format(storedZonedDateTime)

                    adapterClimbStyles.setSelected(
                        savedInstanceStateClimbStyles ?: attemptWithLocation.attempt.climbStyle
                    )
                    adapterOutcome.setSelected(
                        savedInstanceStateOutcome ?: attemptWithLocation.attempt.outcome
                    )
                    adapterRouteGrade.setSelected(
                        savedInstanceStateRouteGrade ?: attemptWithLocation.attempt.routeGrade
                    )
                    adapterRouteType.setSelected(
                        savedInstanceStateRouteType ?: attemptWithLocation.attempt.routeType
                    )
                    adapterRouteCharacteristics.setSelected(savedInstanceStateRouteCharacteristics ?: ArrayList(attemptWithLocation.attempt.routeCharacteristics ?: emptyList()))

                    attemptWithLocation.location?.let {
                        tiet_area.setText(it.area)
                        tiet_sector.setText(it.sector)
                    }

                    edit_text_try_number.setText(attemptWithLocation.attempt.tryNumber?.toString())
                    edit_text_route_name.setText(attemptWithLocation.attempt.routeName)
                    edit_text_length.setText(attemptWithLocation.attempt.length?.toString())
                    edit_text_comment.setText(attemptWithLocation.attempt.comment)

                    rating_bar_rating.rating = attemptWithLocation.attempt.rating?.toFloat() ?: 0f
                }
            })
    }

    private fun partiallyRestoreAttemptDataFromLastAttemptEntry() {
        viewModel.getLastAttemptWithLocation()
            .observe(this, Observer { attemptWithLocation: AttemptWithLocation? ->
                if (!isRestored) {
                    isRestored = true
                    adapterClimbStyles.setSelected(
                        savedInstanceStateClimbStyles ?: attemptWithLocation?.attempt?.climbStyle
                    )
                    adapterOutcome.setSelected(
                        savedInstanceStateOutcome ?: attemptWithLocation?.attempt?.outcome
                    )
                    adapterRouteGrade.setSelected(
                        savedInstanceStateRouteGrade ?: attemptWithLocation?.attempt?.routeGrade
                    )
                    adapterRouteType.setSelected(
                        savedInstanceStateRouteType ?: attemptWithLocation?.attempt?.routeType
                    )
                    adapterRouteCharacteristics.setSelected(savedInstanceStateRouteCharacteristics ?: arrayListOf())
                    attemptWithLocation?.location?.let {
                        tiet_area.setText(it.area)
                        tiet_sector.setText(it.sector)
                    }
                }
            })
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
        val zonedDateTime = ZonedDateTime.parse(text_view_date_time.text, formatterDateTime)

        val climbStyle = adapterClimbStyles.getSelected()
        val outcome = adapterOutcome.getSelected()
        val routeGrade = adapterRouteGrade.getSelected()
        val routeType = adapterRouteType.getSelected()

        if (routeType == null) {
            Toast.makeText(this, "Set route type. Eg. Sport", Toast.LENGTH_LONG).show()
            return null
        }
        if (climbStyle == null) {
            Toast.makeText(this, "Set climb style. Eg. Lead", Toast.LENGTH_LONG).show()
            return null
        }
        if (routeGrade == null) {
            Toast.makeText(this, "Set route grade. Eg. 7a", Toast.LENGTH_LONG).show()
            return null
        }
        if (outcome == null) {
            Toast.makeText(this, "Set outcome. Eg. Onsight", Toast.LENGTH_LONG).show()
            return null
        }

        val attempt = Attempt(
            instantAndZoneId = InstantAndZoneId(zonedDateTime.toInstant(), zonedDateTime.zone),
            routeType = routeType,
            climbStyle = climbStyle,
            routeGrade = routeGrade,
            outcome = outcome
        )

        val area = tiet_area.text.toString()
        val sector = tiet_sector.text.toString()
        val locationId = locations?.get(area)?.get(sector)?.locationId

        attempt.location = locationId

        attempt.tryNumber = Utils.getStringOrNull(edit_text_try_number.text)?.toInt()
        attempt.routeName = Utils.getStringOrNull(edit_text_route_name.text)
        attempt.length = Utils.getStringOrNull(edit_text_length.text)?.toInt()

        attempt.comment = Utils.getStringOrNull(edit_text_comment.text)
        attempt.routeCharacteristics = adapterRouteCharacteristics.getSelected()

        if (rating_bar_rating.rating > 0)
            attempt.rating = rating_bar_rating.rating.toInt()

        return attempt
    }

    private fun deleteAttempt() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.delete_attempt_question))
            .setIcon(R.drawable.ic_delete_forever)
            .setPositiveButton(
                R.string.delete
            ) { _, _ ->
                viewModel.deleteAttemptById(attemptIdFromIntentExtra)
                finish()
            }
            .setNegativeButton(
                R.string.cancel
                , null
            ).create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_edit_attempt_menu, menu)
        if (attemptIdFromIntentExtra == INVALID_ID)
            menu?.findItem(R.id.delete_attempt)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.save_attempt -> {
                saveAttempt()
                true
            }
            R.id.delete_attempt -> {
                deleteAttempt()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        dialogLocationFragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(SAVED_POSITION_OUTCOME, adapterOutcome.getSelected())
        outState?.putString(SAVED_POSITION_CLIMB_STYLES, adapterClimbStyles.getSelected())
        outState?.putString(SAVED_POSITION_ROUTE_GRADE, adapterRouteGrade.getSelected())
        outState?.putString(SAVED_POSITION_ROUTE_TYPE, adapterRouteType.getSelected())
        outState?.putStringArrayList(SAVED_POSITION_ROUTE_CHARACTERISTICS, adapterRouteCharacteristics.getSelected())
        super.onSaveInstanceState(outState)
    }
}
