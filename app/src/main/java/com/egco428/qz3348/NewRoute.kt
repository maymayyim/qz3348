package com.egco428.qz3348

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_new_route.*

class NewRoute : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_route)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        startBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("text", routeText.text.toString())
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
