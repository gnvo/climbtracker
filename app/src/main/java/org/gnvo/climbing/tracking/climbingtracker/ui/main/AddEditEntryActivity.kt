package org.gnvo.climbing.tracking.climbingtracker.ui.main

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_add_update_climb_entry.*
import org.gnvo.climbing.tracking.climbingtracker.R
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntry
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.ClimbEntryWithPitches
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.Pitch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddEditEntryActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID: String = "org.gnvo.climbing.tracking.climbingtracker.ui.main.EXTRA_ID"
        const val INVALID_ID: Long = -1
    }

    private lateinit var viewModel: MainViewModel
    private var formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss")

    private var climbEntryWithPitches: ClimbEntryWithPitches = ClimbEntryWithPitches()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_climb_entry)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        if (intent.hasExtra(EXTRA_ID)) {
            climbEntryWithPitches?.climbEntry?.id = intent.getLongExtra(EXTRA_ID, INVALID_ID)
//            title = getString(R.string.edit_climb_entry)
        } else {
            title = getString(R.string.add_climb_entry)
            button_datetime.text = LocalDateTime.now().format(formatter)
        }
        button_datetime.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, "timePicker")
        }
    }

    private fun saveClimbingEntry() {
        climbEntryWithPitches.climbEntry = ClimbEntry(
            datetime = LocalDateTime.parse(button_datetime.text, formatter),
            routeType = "Sport"
        )
        climbEntryWithPitches.climbEntry?.name = edit_text_route_name?.text.toString()
        climbEntryWithPitches.pitches = listOf(
            Pitch(pitchNumber = 3, routeGradeId = 4),
            Pitch(pitchNumber = 2, routeGradeId = 5),
            Pitch(pitchNumber = 1, routeGradeId = 16)
        )
        viewModel.insertClimbEntry(climbEntryWithPitches!!)

//        val description = edit_text_description.text.toString().trim()
//        val title = edit_text_title.text.toString().trim()
//        if (description.isEmpty() || title.isEmpty()) {
//            Toast.makeText(this, "Pleas enter name and comments", Toast.LENGTH_LONG).show()
//            return
//        }
//
//        val data = Intent()
//        data.putExtra(EXTRA_ROUTE_NAME, title)
//        data.putExtra(EXTRA_COMMENT, description)
////        data.putExtra(EXTRA_PRIORITY, number_picker_priority.value)
//
//        val id = intent.getIntExtra(EXTRA_ID, INVALID_ID)
//        if (id != INVALID_ID)
//            data.putExtra(EXTRA_ID, id )
//
//        setResult(Activity.RESULT_OK, data)
        finish()
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

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

            val newFragment = DatePickerFragment()
            newFragment.show(fragmentManager, "datePicker")
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
        }
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(activity, this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            // Do something with the date chosen by the user
        }
    }
}
