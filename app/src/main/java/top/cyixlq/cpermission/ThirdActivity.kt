package top.cyixlq.cpermission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import top.cyixlq.cpermission.R

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
    }

    fun startMainActivity(v: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
