package org.gnvo.climbing.tracking.climbingtracker.ui.addeditentry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_update_climb_entry.*
import org.gnvo.climbing.tracking.climbingtracker.R
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Attempt
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.AttemptWithDetails
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Location
import org.jetbrains.anko.attempt
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AddEditEntryActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID: String = "org.gnvo.climbing.tracking.climbingtracker.ui.main.EXTRA_ID"
        const val INVALID_ID: Long = -1
    }

    private lateinit var viewModel: AddEditViewModel
    private var formatterDate = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy")
    private var formatterTime = DateTimeFormatter.ofPattern("HH:mm")
    private var gradesMapper: Map<String, Long>? = null

    private var climbEntryIdFromIntentExtra: Long = INVALID_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_climb_entry)

        viewModel = ViewModelProviders.of(this).get(AddEditViewModel::class.java)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        if (intent.hasExtra(EXTRA_ID)) {
            title = getString(R.string.edit_attempt)
            climbEntryIdFromIntentExtra = intent.getLongExtra(EXTRA_ID, INVALID_ID)
            populateClimbEntryData()
        } else {
            title = getString(R.string.add_attempt)
            val now = LocalDateTime.now()
            button_date.text = now.format(formatterDate)
            button_time.text = now.format(formatterTime)
        }
        setDateTimeDialogs()
        setRouteGradesDialog()
        setDialog(button_route_type, R.array.route_type, "Select a route type")
        setDialog(button_climb_style, R.array.climb_styles, "Select a climb style")
        setDialog(button_outcome, R.array.outcome, "Select the attempt outcome")
    }

    private fun setRouteGradesDialog() {
        viewModel.getAllRouteGrades().observe(this, Observer { list ->
            val array = list?.map { it.french as CharSequence }?.toTypedArray()
            gradesMapper = list?.map{it.french!! to it.routeGradeId!!}?.toMap()
            button_grade.setOnClickListener {
                AlertDialog.Builder(this).setTitle("Pick a route grade").setItems(array) { _, which ->
                    button_grade.text = list?.get(which).toString()
                }.create().show()
            }
        })
    }

    private fun setDialog(button: Button, arrayId: Int, title: String) {
        button.setOnClickListener {
            AlertDialog.Builder(this).setTitle(title).setItems(arrayId) { dialog, which ->
                button.text = resources.getStringArray(arrayId)[which]
            }.create().show()
        }
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

    private fun populateClimbEntryData() {
        viewModel.getClimbingEntryFullById(climbEntryIdFromIntentExtra)
            .observe(this, Observer { attemptWithDetails: AttemptWithDetails? ->
                button_date.text = attemptWithDetails?.attempt?.datetime!!.format(formatterDate)
                button_time.text = attemptWithDetails.attempt.datetime!!.format(formatterTime)

                button_route_type.text = attemptWithDetails.attempt.routeType
                button_climb_style.text = attemptWithDetails.attempt.climbingStyle
                button_grade.text = attemptWithDetails.routeGrade.french
                button_outcome.text = attemptWithDetails.attempt.outcome

                edit_text_route_name.setText(attemptWithDetails.attempt.routeName)
                edit_text_area.setText(attemptWithDetails.attempt.location?.area)
                edit_text_sector.setText(attemptWithDetails.attempt.location?.sector)
                edit_text_comment.setText(attemptWithDetails.attempt.comment)

                rating_bar_rating.rating = attemptWithDetails.attempt.rating?.toFloat() ?: 0f
            })
    }

    private fun saveClimbingEntry() {
        val attempt = generateClimbEntryWithPitchesObject() ?: return
        when (climbEntryIdFromIntentExtra) {
            INVALID_ID -> {
                viewModel.insertAttempt(attempt)
                Toast.makeText(this, "Climb attempt created", Toast.LENGTH_LONG).show()
            }
            else -> {
                attempt.id = climbEntryIdFromIntentExtra
                viewModel.updateAttempt(attempt)
                Toast.makeText(this, "Climb attempt updated", Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }

    private fun generateClimbEntryWithPitchesObject(): Attempt? {
        val date = LocalDate.parse(button_date.text, formatterDate)
        val time = LocalTime.parse(button_time.text, formatterTime)
        val datetime = date.atTime(time)

        val routeType = button_route_type.text.toString()
        val climbStyle= button_climb_style.text.toString()
        val routeGrade = gradesMapper?.get(button_grade.text.toString()) ?: -1
        val outcome = button_outcome.text.toString()

        val attempt = Attempt(
            datetime = datetime,
            routeType = routeType,
            climbingStyle = climbStyle,
            routeGrade = routeGrade,
            outcome = outcome,
            location = Location()
        )

        //Todo: create tests to validate validations
        if (! resources.getStringArray(R.array.route_type).contains(attempt.routeType)){
            Toast.makeText(this, "Set route type", Toast.LENGTH_LONG).show()
            return null
        }
        if (! resources.getStringArray(R.array.climb_styles).contains(attempt.climbingStyle)){
            Toast.makeText(this, "Set climbing style", Toast.LENGTH_LONG).show()
            return null
        }
        if (attempt.routeGrade == INVALID_ID){
            Toast.makeText(this, "Set route grade", Toast.LENGTH_LONG).show()
            return null
        }
        if (! resources.getStringArray(R.array.outcome).contains(attempt.outcome)){
            Toast.makeText(this, "Set outcome", Toast.LENGTH_LONG).show()
            return null
        }

        attempt.routeName = getStringOrNull(edit_text_route_name.text)
        attempt.location!!.area = getStringOrNull(edit_text_area.text)
        attempt.location!!.sector = getStringOrNull(edit_text_sector.text)
        attempt.comment = getStringOrNull(edit_text_comment.text)

        if (rating_bar_rating.rating > 0)
            attempt.rating = rating_bar_rating.rating.toInt()

        return attempt
    }

    private fun getStringOrNull(text: Editable?): String? {
        return when {
            text.isNullOrEmpty() -> null
            else -> return text.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_climb_entry_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.save_climb_entry -> {
                saveClimbingEntry()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
