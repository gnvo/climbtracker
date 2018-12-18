package org.gnvo.climb.tracking.climbtracker.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.gnvo.climb.tracking.climbtracker.R
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptListItem
import org.gnvo.climb.tracking.climbtracker.data.room.pojo.AttemptWithGrades
import org.gnvo.climb.tracking.climbtracker.ui.addeditentry.AddEditAttemptActivity
import org.gnvo.climb.tracking.climbtracker.ui.main.views.adapter.EntryAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

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

        viewModel.getAllAttemptsWithGrades().observe(this, Observer {
            adapter.submitList(it)
        })

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
