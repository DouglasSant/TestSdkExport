package uk.co.verifymyage.sdk

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facetec.zoom.sdk.ZoomIDScanResult
import com.facetec.zoom.sdk.ZoomSDK
import com.facetec.zoom.sdk.ZoomSessionResult
import uk.co.verifymyage.sdk.ZoomProcessors.LivenessCheckProcessor
import uk.co.verifymyage.sdk.ZoomProcessors.Processor
import uk.co.verifymyage.sdk.ZoomProcessors.ZoomGlobalState


class VerifyMyAgeSDKActivity : AppCompatActivity() {

    var latestZoomSessionResult: ZoomSessionResult? = null
    var latestZoomIDScanResult: ZoomIDScanResult? = null
    var latestProcessor: Processor? = null

    // Handle error retrieving the Session Token from the server
    var sessionTokenErrorCallback: Processor.SessionTokenErrorCallback =
        Processor.SessionTokenErrorCallback { Log.d("SessionTokenError", "DEPOIS") }

    fun onLivenessCheckPressed(v: View?) {
        latestProcessor = LivenessCheckProcessor(this, sessionTokenErrorCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vma_sdk_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#f9ac19")))
        supportActionBar?.title = Html.fromHtml("<font color='#ffffff'>VerifyMyAge</font>")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#fbc359")
        };

        val message = intent.getStringExtra("EXTRA_MESSAGE")

        // Capture the layout's TextView and set the string as its text
        findViewById<TextView>(R.id.textView).text = message

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