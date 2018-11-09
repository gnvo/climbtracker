package tracking.climbing.gnvo.org.climbingtracker.ui.main

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*
import tracking.climbing.gnvo.org.climbingtracker.R

class AddEditEntryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID: String = "tracking.climbing.gnvo.org.climbingtracker.ui.main.EXTRA_ID"
        const val EXTRA_TITLE: String = "tracking.climbing.gnvo.org.climbingtracker.ui.main.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION: String = "tracking.climbing.gnvo.org.climbingtracker.ui.main.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY: String = "tracking.climbing.gnvo.org.climbingtracker.ui.main.EXTRA_PRIORITY"
        const val INVALID_ID: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        number_picker_priority.minValue = 1
        number_picker_priority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit note")
            edit_text_title.setText(intent.getStringExtra(EXTRA_TITLE))
            edit_text_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            number_picker_priority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
        } else {
            setTitle("Add note")
        }
    }

    private fun saveClimbingEntry() {
        val description = edit_text_description.text.toString().trim()
        val title = edit_text_title.text.toString().trim()
        if (description.isEmpty() || title.isEmpty()) {
            Toast.makeText(this, "Pleas enter name and comments", Toast.LENGTH_LONG).show()
            return
        }

        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, number_picker_priority.value)

        val id = intent.getIntExtra(EXTRA_ID, INVALID_ID)
        if (id != INVALID_ID)
            data.putExtra(EXTRA_ID, id )

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.save_note -> {
                saveClimbingEntry()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
