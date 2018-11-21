package org.gnvo.climbing.tracking.climbingtracker.ui.addeditentry

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_update_climb_entry.*
import org.gnvo.climbing.tracking.climbingtracker.R
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryFull
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Pitch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddEditEntryActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID: String = "org.gnvo.climbing.tracking.climbingtracker.ui.main.EXTRA_ID"
        const val INVALID_ID: Long = -1
    }

    private lateinit var viewModel: AddEditViewModel
    private var formatterDate = DateTimeFormatter.ofPattern("EEE, d MMM yyyy")
    private var formatterTime = DateTimeFormatter.ofPattern("HH:mm")

    private var climbEntryIdFromIntentExtra: Long = INVALID_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_climb_entry)

        viewModel = ViewModelProviders.of(this).get(AddEditViewModel::class.java)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        if (intent.hasExtra(EXTRA_ID)) {
            title = getString(R.string.edit_climb_entry)
            climbEntryIdFromIntentExtra = intent.getLongExtra(EXTRA_ID, INVALID_ID)
            populateClimbEntryData()
        } else {
            title = getString(R.string.add_climb_entry)
            val now = LocalDateTime.now()
            button_date.text = now.format(formatterDate)
            button_time.text = now.format(formatterTime)
        }
        setDateTimeDialogs()
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
                    button_date.text = LocalDate.of(year, monthOfYear+1, dayOfMonth).format(formatterDate)
                },
                date.year,
                date.monthValue-1,
                date.dayOfMonth
            )
            datePickerDialog.show()
        }

    }

    private fun populateClimbEntryData() {
        viewModel.getClimbingEntryFullById(climbEntryIdFromIntentExtra)
            .observe(this, Observer { climbEntryFull: ClimbEntryFull? ->
                button_date.text = climbEntryFull?.datetime!!.format(formatterDate)
                button_time.text = climbEntryFull.datetime.format(formatterTime)


                when (climbEntryFull.routeType) {
                    getString(R.string.sport) -> radio_group_route_type.check(radio_button_sport.id)
                    getString(R.string.trad) -> radio_group_route_type.check(radio_button_trad.id)
                    getString(R.string.indoors) -> radio_group_route_type.check(radio_button_indoors.id)
                }

                edit_text_route_name.setText(climbEntryFull.name)
                edit_text_area.setText(climbEntryFull.area)
                edit_text_sector.setText(climbEntryFull.sector)
                edit_text_comment.setText(climbEntryFull.comment)

                rating_bar_rating.rating = climbEntryFull.rating?.toFloat() ?: 0f
            })
    }

    private fun saveClimbingEntry() {
        val climbEntryWithPitches = generateClimbEntryWithPitchesObject()
        when (climbEntryIdFromIntentExtra) {
            INVALID_ID -> {
                viewModel.insertClimbEntry(climbEntryWithPitches!!)
                Toast.makeText(this, "ClimbEntry created", Toast.LENGTH_LONG).show()
            }
            else -> {
                climbEntryWithPitches?.climbEntry?.id = climbEntryIdFromIntentExtra
                viewModel.updateClimbEntry(climbEntryWithPitches!!)
                Toast.makeText(this, "ClimbEntry updated", Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }

    private fun generateClimbEntryWithPitchesObject(): ClimbEntryWithPitches? {
        val date = LocalDate.parse(button_date.text, formatterDate)
        val time = LocalTime.parse(button_time.text, formatterTime)
        val climbEntryWithPitches = ClimbEntryWithPitches(
            ClimbEntry(
                datetime = date.atTime(time)
            )
        )
        edit_text_route_name.text.let {
            if (!it!!.isEmpty()) climbEntryWithPitches.climbEntry?.name = it.toString()
        }
        edit_text_area.text.let {
            if (!it!!.isEmpty()) climbEntryWithPitches.climbEntry?.area = it.toString()
        }
        edit_text_sector.text.let {
            if (!it!!.isEmpty()) climbEntryWithPitches.climbEntry?.sector = it.toString()
        }
        edit_text_comment.text.let {
            if (!it!!.isEmpty()) climbEntryWithPitches.climbEntry?.comment = it.toString()
        }

        radio_group_route_type.checkedRadioButtonId.let {
            if (it != -1) {
                val checkedRouteType: RadioButton? = findViewById(it)
                climbEntryWithPitches.climbEntry?.routeType = checkedRouteType?.text.toString()
            }
        }

        if (rating_bar_rating.rating > 0)
            climbEntryWithPitches.climbEntry?.rating = rating_bar_rating.rating.toInt()

        climbEntryWithPitches.pitches = listOf(
            Pitch(pitchNumber = 3, routeGradeId = 4, attemptOutcome = "Onsight", climbingStyle = "Lead"),
            Pitch(pitchNumber = 2, routeGradeId = 5, attemptOutcome = "Flash", climbingStyle = "Follow"),
            Pitch(pitchNumber = 1, routeGradeId = 16, attemptOutcome = "Onsight", climbingStyle = "Lead")
        )
        return climbEntryWithPitches
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
