package com.egco428.qz3348

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_main.view.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 101
    lateinit var dataReference: DatabaseReference
    lateinit var msgList: MutableList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataReference = FirebaseDatabase.getInstance().getReference("map")
        msgList = mutableListOf()

        // show normal
        main_listview.setOnItemClickListener { adapterView, view, position, id ->
            

            val intent = Intent(this, MapsActivity2::class.java)




            startActivity(intent)
        }

        dataReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if(dataSnapshot!!.exists()){
                    msgList.clear()
                    for(i in dataSnapshot.children){
                        val message = i.getValue(Message::class.java)
                        msgList.add(message!!)
                    }
                    val adapter = MessageAdapter(applicationContext, R.layout.row_main, msgList)
                    main_listview.adapter = adapter
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_new -> {
                val intent = Intent(this, NewRoute::class.java)
                startActivity(intent)

                return true
            }
            R.id.main_listview -> {

            }
        }

        return super.onOptionsItemSelected(item)

    }

}

