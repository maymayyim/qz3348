package com.egco428.qz3348

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng

/**
 * Created by 6272user on 11/30/2017 AD.
 */

class Message (val id: String, val name: String, val listOfLat: ArrayList<Double>, val listOfLng: ArrayList<Double>, val color : String){
    constructor() : this("","", ArrayList<Double>(),ArrayList<Double>(),"")
}