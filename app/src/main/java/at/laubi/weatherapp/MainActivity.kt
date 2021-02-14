package at.laubi.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import at.laubi.weatherapp.data.RestService

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

    }
}