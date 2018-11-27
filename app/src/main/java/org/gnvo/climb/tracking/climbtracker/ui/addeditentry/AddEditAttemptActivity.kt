package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_update_attempt.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.*
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.adapters.GenericAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AddEditAttemptActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID: String = "org.gnvo.climb.tracking.climbtracker.ui.addeditentry.EXTRA_ID"
        const val INVALID_ID: Long = -1
    }

    private lateinit var viewModel: AddEditViewModel
    private var formatterDate = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy")
    private var formatterTime = DateTimeFormatter.ofPattern("HH:mm")

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
        }
        setDateTimeDialogs()
        setAdapterToRecyclerView(recycler_view_climb_style, GenericAdapter(), viewModel.getAllClimbStyles())
        setAdapterToRecyclerView(recycler_view_outcome, GenericAdapter(), viewModel.getAllOutcomes())
        val routeGradeAdapter = GenericAdapter<RouteGrade>()
        routeGradeAdapter.setCustomFormatter(object : GenericAdapter.CustomFormatter<RouteGrade> {
            override fun format(item: RouteGrade): String {
                return "${item.french}"
            }
        })
        routeGradeAdapter.setScroller(object : GenericAdapter.Scroller {
            override fun scroll(selectedPosition: Int) {
                val position = when (selectedPosition) {
                    RecyclerView.NO_POSITION -> 9 //if nothing is selected scroll down a bit
                    else -> selectedPosition
                }
                (recycler_view_route_grade.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
            }
        })
        setAdapterToRecyclerView(recycler_view_route_grade, routeGradeAdapter, viewModel.getAllRouteGrades())
        setAdapterToRecyclerView(recycler_view_route_type, GenericAdapter(), viewModel.getAllRouteTypes())
        setAdapterToRecyclerView(recycler_view_route_characteristics, GenericAdapter(), viewModel.getAllRouteCharacteristics())
    }

    private fun <T>setAdapterToRecyclerView(recycler_view: RecyclerView, genericAdapter: GenericAdapter<T>, liveData: LiveData<List<T>>) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        recycler_view.adapter = genericAdapter

        liveData.observe(this, Observer {
            genericAdapter.setItems(it!!)
        })
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
        viewModel.getAttemptWithDetailsById(attemptIdFromIntentExtra)
            .observe(this, Observer { attemptWithDetails: AttemptWithDetails? ->
                button_date.text = attemptWithDetails?.attempt?.datetime!!.format(formatterDate)
                button_time.text = attemptWithDetails.attempt.datetime.format(formatterTime)

                (recycler_view_climb_style.adapter as GenericAdapter<ClimbStyle>).setSelected(attemptWithDetails.climbStyle)
                (recycler_view_outcome.adapter as GenericAdapter<Outcome>).setSelected(attemptWithDetails.outcome)
                (recycler_view_route_grade.adapter as GenericAdapter<RouteGrade>).setSelected(attemptWithDetails.routeGrade)
                (recycler_view_route_type.adapter as GenericAdapter<RouteType>).setSelected(attemptWithDetails.routeType)

                edit_text_route_name.setText(attemptWithDetails.attempt.routeName)
                edit_text_length.setText(attemptWithDetails.attempt.length?.toString())
                edit_text_area.setText(attemptWithDetails.attempt.location?.area)
                edit_text_sector.setText(attemptWithDetails.attempt.location?.sector)
                edit_text_comment.setText(attemptWithDetails.attempt.comment)

                rating_bar_rating.rating = attemptWithDetails.attempt.rating?.toFloat() ?: 0f
            })
    }

    private fun saveAttempt() {
        val attempt = generateAttemptWithDetails() ?: return
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

    private fun generateAttemptWithDetails(): Attempt? {
        val date = LocalDate.parse(button_date.text, formatterDate)
        val time = LocalTime.parse(button_time.text, formatterTime)
        val datetime = date.atTime(time)

        val climbStyleId= (recycler_view_climb_style.adapter as GenericAdapter<ClimbStyle>).getSelected()?.climbStyleId ?: INVALID_ID
        val outcomeId= (recycler_view_outcome.adapter as GenericAdapter<Outcome>).getSelected()?.outcomeId ?: INVALID_ID
        val routeGradeId= (recycler_view_route_grade.adapter as GenericAdapter<RouteGrade>).getSelected()?.routeGradeId ?: INVALID_ID
        val routeTypeId= (recycler_view_route_type.adapter as GenericAdapter<RouteType>).getSelected()?.routeTypeId ?: INVALID_ID

        //Todo: create tests to validate validations
        if (routeTypeId == INVALID_ID){
            Toast.makeText(this, "Set route type. Eg. Sport", Toast.LENGTH_LONG).show()
            return null
        }
        if (climbStyleId == INVALID_ID){
            Toast.makeText(this, "Set climb style. Eg. Lead", Toast.LENGTH_LONG).show()
            return null
        }
        if (routeGradeId == INVALID_ID){
            Toast.makeText(this, "Set route grade. Eg. 7a", Toast.LENGTH_LONG).show()
            return null
        }
        if (outcomeId == INVALID_ID){
            Toast.makeText(this, "Set outcome. Eg. Onsight", Toast.LENGTH_LONG).show()
            return null
        }

        val attempt = Attempt(
            datetime = datetime,
            routeType = routeTypeId,
            climbStyle = climbStyleId,
            routeGrade = routeGradeId,
            outcome = outcomeId,
            location = Location()
        )

        attempt.routeName = getStringOrNull(edit_text_route_name.text)
        attempt.length = getStringOrNull(edit_text_length.text)?.toInt()
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
        menuInflater.inflate(R.menu.add_edit_attempt_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.save_attempt-> {
                saveAttempt()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
