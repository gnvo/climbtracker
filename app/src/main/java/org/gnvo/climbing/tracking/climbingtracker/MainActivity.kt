package org.gnvo.climbing.tracking.climbingtracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.gnvo.climbing.tracking.climbingtracker.data.room.pojo.*
import org.gnvo.climbing.tracking.climbingtracker.ui.main.AddEditEntryActivity
import org.gnvo.climbing.tracking.climbingtracker.ui.main.MainViewModel
import org.gnvo.climbing.tracking.climbingtracker.ui.main.EntryAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_add_climb_entry.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditEntryActivity::class.java)
            startActivity(intent)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val adapter = EntryAdapter()
        recycler_view.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getAllClimbingEntriesFull().observe(this, Observer {
            adapter.submitList(it!!)
        })

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteClimbEntryById(adapter.getClimbingEntryAt(position).climbEntryId)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : EntryAdapter.OnItemClickListener {
            override fun onItemClick(climbEntryFull: ClimbEntryFull) {
                val intent = Intent(this@MainActivity, AddEditEntryActivity::class.java)
                intent.putExtra(AddEditEntryActivity.EXTRA_ID, climbEntryFull.climbEntryId)
                startActivity(intent)
            }
        })

        viewModel.getAllClimbingEntriesFull().observe(this, Observer { climbEntriesFull: List<ClimbEntryFull>? ->
            Log.d("gnvo", climbEntriesFull.toString())
        })
        viewModel.getAllRouteGrades().observe(this, Observer { routeGrades: List<RouteGrade>? ->
            Log.d("gnvo", routeGrades.toString())
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete_all_climb_entries -> {
                viewModel.deleteAllClimbingEntries()
                Toast.makeText(this, "All climb entries deleted", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
