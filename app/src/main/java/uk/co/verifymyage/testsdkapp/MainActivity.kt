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
            putExtra("API_ID", "P2mrkbJ4AZ")
            putExtra("API_KEY", "190fcbd0-a9e3-46cd-ae1a-097f7be0ccd3")
            putExtra("API_SECRET", "0906e33f5266e9134c9fff1338b53d914fea93b7a314eb3333488772a643c3fe")
            putExtra("id", "123")
            putExtra("first_name", "Fname")
            putExtra("last_name", "Lname")
            putExtra("email", "email")
            putExtra("phone", "44070000000")
        }
        startActivity(intent)
    }

    fun callSDKToViewUserID(view: View) {
        val intent = Intent(this, IdActivity::class.java).apply {
            putExtra("API_ID", "P2mrkbJ4AZ")
            putExtra("API_KEY", "190fcbd0-a9e3-46cd-ae1a-097f7be0ccd3")
            putExtra("API_SECRET", "0906e33f5266e9134c9fff1338b53d914fea93b7a314eb3333488772a643c3fe")
        }
        startActivity(intent)
    }

}