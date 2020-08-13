package uk.co.verifymyage.sdk

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facetec.zoom.sdk.ZoomIDScanResult
import com.facetec.zoom.sdk.ZoomSDK
import com.facetec.zoom.sdk.ZoomSessionResult
import uk.co.verifymyage.sdk.idscan.MainActivity
import uk.co.verifymyage.sdk.zoomprocessors.LivenessCheckProcessor
import uk.co.verifymyage.sdk.zoomprocessors.Processor
import uk.co.verifymyage.sdk.zoomprocessors.ZoomGlobalState


class VerifyOptionsActivity : AppCompatActivity() {

    var latestZoomSessionResult: ZoomSessionResult? = null
    var latestZoomIDScanResult: ZoomIDScanResult? = null
    var latestProcessor: Processor? = null

    // Handle error retrieving the Session Token from the server
    private val sessionTokenErrorCallback: Processor.SessionTokenErrorCallback =
        Processor.SessionTokenErrorCallback { Log.d("SessionTokenError", "DEPOIS") }

    fun onLivenessCheckPressed(v: View?) {
        latestProcessor = LivenessCheckProcessor(this, sessionTokenErrorCallback)
    }

    fun idScan(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verification_options_screen)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#f9ac19")))
        supportActionBar?.title = Html.fromHtml("<font color='#ffffff'>VerifyMyAge</font>")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#fbc359")
        };

        val message = intent.getStringExtra("EXTRA_MESSAGE")

        // Capture the layout's TextView and set the string as its text
        //findViewById<TextView>(R.id.textView).text = message

        val context = this

        Log.d("TESTE", "ANTES")
        ZoomSDK.initialize(
            context,
            ZoomGlobalState.DeviceLicenseKeyIdentifier,
            ZoomGlobalState.PublicFaceMapEncryptionKey,
            object: ZoomSDK.InitializeCallback(){
                override fun onCompletion(completed: Boolean) {
                    Toast.makeText(context, "$completed", Toast.LENGTH_LONG).show()
                    Log.d("TESTE", "TESTE $completed")
                }

            }
        )
        Log.d("TESTE", "DEPOIS")

//        apply {
//            text = message
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}