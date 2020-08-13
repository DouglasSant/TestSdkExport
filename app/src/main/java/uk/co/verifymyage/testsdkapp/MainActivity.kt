package uk.co.verifymyage.testsdkapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import uk.co.verifymyage.sdk.IdActivity
import uk.co.verifymyage.sdk.VerifyMyAgeVerificationFlow
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** Called when the user taps the Send button */
    fun callSDK(view: View) {
        val intent = Intent(this, VerifyMyAgeVerificationFlow::class.java).apply {
            putExtra("EXTRA_MESSAGE", UUID.randomUUID().toString())
        }
        startActivity(intent)
    }

    fun callSDKToViewUserID(view: View) {
        val intent = Intent(this, IdActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", UUID.randomUUID().toString())
        }
        startActivity(intent)
    }

}