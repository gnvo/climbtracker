package org.gnvo.climb.tracking.climbtracker.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptHeader
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.AddEditAttemptActivity
import org.gnvo.climb.tracking.climbtracker.ui.main.views.ViewHolderHeader
import org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter.EntryAdapter
import org.jetbrains.anko.custom.onUiThread
import org.jetbrains.anko.doAsync
import org.threeten.bp.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var formatterDate = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_add_attempt.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditAttemptActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)

        val adapter = EntryAdapter()
        recycler_view.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                layoutManager.scrollToPositionWithOffset(positionStart, 0)
            }
        })

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getAllAttemptsWithGrades().observe(this, Observer {
            doAsync {
                val list: MutableList<AttemptListItem> = mutableListOf()
                if (it?.isNotEmpty()!!){
                    var currentDate: String? = null
                    for (attemptWithGrades in it){
                        val zonedDateTime = attemptWithGrades.attempt.instantAndZoneId.instant.atZone(attemptWithGrades.attempt.instantAndZoneId.zoneId)
                        val attemptDate = zonedDateTime.format(formatterDate)
                        if (attemptDate != currentDate)
                            list.add(AttemptHeader(date = attemptDate))
                        list.add(attemptWithGrades)
                        currentDate = attemptDate
                    }
                }
                runOnUiThread{
                    adapter.submitList(list)
                }
            }
        })

        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                    if (viewHolder is ViewHolderHeader) return 0
                    return super.getSwipeDirs(recyclerView, viewHolder)
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val attemptWithGrades = adapter.getItemAt(position)
                    if (attemptWithGrades is AttemptWithGrades) {
                        viewModel.deleteAttempt(attemptWithGrades.attempt)
                        Toast.makeText(this@MainActivity, "Deleted attempt", Toast.LENGTH_LONG).show()
                    }
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : EntryAdapter.OnItemClickListener {
            override fun onItemClick(attemptListItem: AttemptListItem) {
                if (attemptListItem is AttemptWithGrades) {
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
            R.id.delete_all_climb_entries -> {
//                viewModel.deleteAllAttempts()
                Toast.makeText(this, "Non existing funct", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
