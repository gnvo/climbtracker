package tracking.climbing.gnvo.org.climbingtracker

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import tracking.climbing.gnvo.org.climbingtracker.data.room.ClimbEntry
import tracking.climbing.gnvo.org.climbingtracker.ui.main.AddEditEntryActivity
import tracking.climbing.gnvo.org.climbingtracker.ui.main.MainViewModel
import tracking.climbing.gnvo.org.climbingtracker.ui.main.EntryAdapter



class MainActivity : AppCompatActivity() {

    val ADD_CLIMBING_ENTRY_REQUEST: Int = 1
    val EDIT_CLIMBING_ENTRY_REQUEST: Int = 2

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button_add_climb_entry.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditEntryActivity::class.java)
            startActivityForResult(intent, ADD_CLIMBING_ENTRY_REQUEST)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val adapter = EntryAdapter()
        recycler_view.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getAllClimbingEntries().observe(this, Observer {
            adapter.submitList(it!!)
        })

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.delete(adapter.getClimbingEntryAt(position))
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : EntryAdapter.OnItemClickListener {
            override fun onItemClick(climbEntry: ClimbEntry) {
                val intent = Intent(this@MainActivity, AddEditEntryActivity::class.java)
                intent.putExtra(AddEditEntryActivity.EXTRA_ID, climbEntry.id)
                intent.putExtra(AddEditEntryActivity.EXTRA_ROUTE_NAME, climbEntry.name)
                intent.putExtra(AddEditEntryActivity.EXTRA_COMMENT, climbEntry.comment)
//                intent.putExtra(AddEditEntryActivity.EXTRA_PRIORITY, climbEntry.priority )

                startActivityForResult(intent, EDIT_CLIMBING_ENTRY_REQUEST)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "ClimbEntry not saved", Toast.LENGTH_LONG).show()
            return
        }

        val routeName = data?.getStringExtra(AddEditEntryActivity.EXTRA_ROUTE_NAME)
        val description = data?.getStringExtra(AddEditEntryActivity.EXTRA_COMMENT)
        val priority = data?.getIntExtra(AddEditEntryActivity.EXTRA_PRIORITY, -1)
        val id = data?.getIntExtra(AddEditEntryActivity.EXTRA_ID, AddEditEntryActivity.INVALID_ID)

        when (requestCode) {
            ADD_CLIMBING_ENTRY_REQUEST -> {
//                val climbEntry = ClimbEntry(name = title!!, comments = description!!, priority = priority)
//                viewModel.insert(climbEntry)

                Toast.makeText(this, "ClimbEntry created", Toast.LENGTH_LONG).show()
            }
            EDIT_CLIMBING_ENTRY_REQUEST -> {
                if (id == AddEditEntryActivity.INVALID_ID) {
                    Toast.makeText(this, "ClimbEntry could not be updated", Toast.LENGTH_LONG).show()
                    return
                }

//                val climbEntry = ClimbEntry(name = title!!, comments = description!!, priority = priority)
//                climbEntry.id = id
//                viewModel.update(climbEntry)

                Toast.makeText(this, "ClimbEntry updated", Toast.LENGTH_LONG).show()
            }
        }
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
