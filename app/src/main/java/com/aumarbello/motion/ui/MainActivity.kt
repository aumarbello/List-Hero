package com.aumarbello.motion.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.aumarbello.motion.R
import com.aumarbello.motion.data.Link
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var isNavComponentMode: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        isNavComponentMode = intent.getBooleanExtra(mode, true)

        val parent: LinearLayout = findViewById(R.id.parent)
        if (isNavComponentMode) {
            LayoutInflater.from(this).inflate(
                R.layout.host_fragment,
                parent,
                true
            )
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FirstFragment())
                .commit()
        }
    }

    fun openDetails(view: View, link: Link) {
        val args = Bundle().apply {
            putSerializable("item_link", link)
        }
        ViewCompat.setTransitionName(view, link.id)

        if (!isNavComponentMode) {
            openWithTransaction(view, link, args)
            return
        }

        val extra = FragmentNavigator.Extras.Builder()
            .addSharedElement(view, link.id)
            .build()

        findNavController(R.id.nav_host_fragment).navigate(R.id.SecondFragment, args, null, extra)
    }

    private fun openWithTransaction(
        view: View,
        link: Link,
        args: Bundle
    ) {
        val fragment = SecondFragment().apply {
            arguments = args
        }
        supportFragmentManager.beginTransaction()
            .addSharedElement(view, link.id)
            .replace(R.id.container, fragment)
            .addToBackStack(link.id)
            .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_toggle -> {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(mode, !isNavComponentMode)
                }
                startActivity(intent)
                finish()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private companion object {
        const val mode = "navigator_mode"
    }
}
