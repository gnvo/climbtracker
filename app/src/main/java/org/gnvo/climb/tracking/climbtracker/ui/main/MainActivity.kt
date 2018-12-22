package org.gnvo.climb.tracking.climbtracker.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.opencsv.CSVWriter
import kotlinx.android.synthetic.main.activity_main.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithLocationAndGrades
import org.gnvo.climb.tracking.climbtracker.preferences.AppPreferencesHelper
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.AddEditAttemptActivity
import org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter.EntryAdapter
import java.io.CharArrayWriter
import java.io.Writer


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var appPrefs: AppPreferencesHelper
    private lateinit var adapter: EntryAdapter
    private lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        appPrefs = AppPreferencesHelper(context = this)

        button_add_attempt.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditAttemptActivity::class.java)
            startActivity(intent)
        }

        layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)

        adapter = EntryAdapter(appPrefs.getAlwaysShow())
        recycler_view.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                layoutManager.scrollToPositionWithOffset(positionStart, 0)
            }
        })

        viewModel.getAllAttemptsWithGrades().observeForever {
            adapter.submitList(it)
        }

        adapter.setOnItemClickListener(object : EntryAdapter.OnItemClickListener {
            override fun onItemClick(attemptListItem: AttemptListItem) {
                if (attemptListItem is AttemptWithLocationAndGrades) {
                    val intent = Intent(this@MainActivity, AddEditAttemptActivity::class.java)
                    intent.putExtra(AddEditAttemptActivity.EXTRA_ID, attemptListItem.attempt.id)
                    startActivity(intent)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.always_show_french -> {
                switchAlwaysShow(ALWAYS_SHOW_FRENCH)
                true
            }
            R.id.always_show_uuia -> {
                switchAlwaysShow(ALWAYS_SHOW_UIAA)
                true
            }
            R.id.always_show_yds -> {
                switchAlwaysShow(ALWAYS_SHOW_YDS)
                true
            }
            R.id.export_db -> {
                exportDB(CharArrayWriter())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun exportDB(writer: Writer) {
        val allAttempts = viewModel.getAllAttempts()
        allAttempts.observe(this, Observer {
            // write String Array
            val csvWriter = CSVWriter(writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)

//            csvWriter.writeNext(CSV_HEADER)

            for (attemptWithLocation in it!!) {
                val data = arrayOf<String>(
                    attemptWithLocation.attempt.climbStyle,
                    attemptWithLocation.attempt.outcome,
                    attemptWithLocation.attempt.routeGrade,
                    attemptWithLocation.attempt.routeType,
                    attemptWithLocation.attempt.instantAndZoneId.instant.toEpochMilli().toString(),
                    attemptWithLocation.attempt.instantAndZoneId.zoneId.id,
                    attemptWithLocation.attempt.routeName ?: "",
                    attemptWithLocation.attempt.comment ?: "",
                    attemptWithLocation.attempt.rating?.toString() ?: "",
                    attemptWithLocation.attempt.routeCharacteristics?.toString() ?: "",
                    attemptWithLocation.attempt.length?.toString() ?: "",
                    attemptWithLocation.attempt.tryNumber?.toString() ?: "",
                    attemptWithLocation.attempt.location?.toString() ?: "")

                csvWriter.writeNext(data)
            }
            Log.d("gnvog", "writer: $writer")
        })
    }

    private fun switchAlwaysShow(alwaysShowName: String) {
        val currentAlwaysShow = appPrefs.getAlwaysShow()
        val newAlwaysShow = if (currentAlwaysShow != null && currentAlwaysShow == alwaysShowName) {
            ALWAYS_SHOW_NONE
        } else {
            alwaysShowName
        }
        appPrefs.setAlwaysShow(newAlwaysShow)
        recycler_view.layoutManager = null
        adapter.alwaysShowGrade = newAlwaysShow
        recycler_view.recycledViewPool.clear()
        recycler_view.layoutManager = layoutManager
    }

    companion object {
        private const val ALWAYS_SHOW_FRENCH = "french"
        private const val ALWAYS_SHOW_UIAA= "uiaa"
        private const val ALWAYS_SHOW_YDS = "yds"
        private const val ALWAYS_SHOW_NONE = ""
    }
}
