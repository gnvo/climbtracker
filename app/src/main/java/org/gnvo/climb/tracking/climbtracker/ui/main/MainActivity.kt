package org.gnvo.climb.tracking.climbtracker.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.AddEditAttemptActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

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
            adapter.submitList(it!!)
        })

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val attempt = adapter.getItemAt(position).attempt
                viewModel.deleteAttempt(attempt)
                Toast.makeText(this@MainActivity, "Deleted attempt", Toast.LENGTH_LONG).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : EntryAdapter.OnItemClickListener {
            override fun onItemClick(attemptWithGrades: AttemptWithGrades) {
                val intent = Intent(this@MainActivity, AddEditAttemptActivity::class.java)
                intent.putExtra(AddEditAttemptActivity.EXTRA_ID, attemptWithGrades.attempt.id)
                startActivity(intent)
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
//                viewModel.deleteAllAttempts()//TODO implement other menu options
                Toast.makeText(this, "Non existing funct", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
