package uk.co.verifymyage.testsdkapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import uk.co.verifymyage.sdk.VerifyMyAgeSDKActivity
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val intent = Intent(this, VerifyMyAgeSDKActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", UUID.randomUUID().toString())
        }
        startActivity(intent)
    }

}