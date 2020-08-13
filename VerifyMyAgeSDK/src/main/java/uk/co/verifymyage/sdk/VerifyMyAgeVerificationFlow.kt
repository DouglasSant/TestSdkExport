package uk.co.verifymyage.sdk

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity

public final class VerifyMyAgeVerificationFlow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verifying_age_screen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#f9ac19")))
        supportActionBar?.title = Html.fromHtml("<font color='#ffffff'>VerifyMyAge</font>")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#fbc359")
        };

        doAsync {
            //TODO: here we add the api call
            Thread.sleep(3000)
            val intent = Intent(this, VerificationsResultActivity::class.java).apply {
                putExtra("VERIFICATION_RESULT", true)
            }
            startActivity(intent)
        }.execute()


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }

}