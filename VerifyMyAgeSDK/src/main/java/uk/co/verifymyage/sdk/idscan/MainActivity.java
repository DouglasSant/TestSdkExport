/**
 Copyright (c) 2012-2017, Smart Engines Ltd
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice,
 this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 * Neither the name of the Smart Engines Ltd nor the names of its
 contributors may be used to endorse or promote products derived from this
 software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package uk.co.verifymyage.sdk.idscan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import biz.smartengines.smartid.swig.RecognitionResult;
import biz.smartengines.smartid.swig.StringVector2d;
import uk.co.verifymyage.sdk.R;


/**
 * Main sample activity for documents recognition with Smart IDReader Android SDK
 */
public class MainActivity extends AppCompatActivity
                          implements SmartIDCallback, View.OnClickListener {

    boolean processing = false; ///< Whether an active recognition session is processing frames
    private final int REQUEST_CAMERA_PERMISSION = 1;

    /** Important!
        Setting enabled document types for the recognition session
        according to available document types for your delivery
        these types will be passed to SessionSettings
        with which RecognitionEngine's SpawnSession(...) method is called.
        Internally you can specify a concrete document type or a
        wildcard expression (for convenience) to enable or disable multiple types.
        If exception is thrown please read the exception message. */

    // Enabled document mask (all documents by default, unusable with some bundles)
    String document_mask = "*";
    // Recognition session timeout
    String time_out = "5.0";

    private static SmartIDView view = new SmartIDView();
    private Button button;
    private ImageButton selector;

    final static String nameVariableKey = "DOC_VARIABLE";
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(nameVariableKey, document_mask);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        document_mask=savedInstanceState.getString(nameVariableKey);
        TextView Doc = (TextView) findViewById(R.id.textViewDoc);
        Doc.setText(document_mask);
    }

    public String[] SelectDocs(){

        StringVector2d selectdocs = view.getDocumentsList();

        ArrayList<String> docs = new ArrayList<>();

        for (int i=0; i < selectdocs.size(); i++) {
            for (int j=0;j<selectdocs.get(i).size(); j++) {
                docs.add(selectdocs.get(i).get(j));

            }
        }

        return docs.toArray(new String[docs.size()]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_idscan);

        button = (Button) findViewById(R.id.button);
        selector = (ImageButton) findViewById(R.id.selector);

        initEngine();

        if (permission(Manifest.permission.CAMERA) == true) {
            request(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void initEngine() {
        try {
            view.initializeEngine(this, this);
        } catch (Exception e) {
            Log.d("smartid", "Engine initialization failed: " + e.toString());
        }

        SurfaceView preview = (SurfaceView) findViewById(R.id.preview);
        RelativeLayout drawing = (RelativeLayout) findViewById(R.id.drawing);
        view.setSurface(preview, drawing);
    }

    @Override
    public void onClick(View v)  {
        if (v.getId() == R.id.button) {
            button.setOnClickListener(null); // Disable button
            selector.setOnClickListener(null);

            if (processing == false) {
                view.startRecognition(document_mask, time_out);
            } else {
                view.stopRecognition();
            }
        }

        if(v.getId() == R.id.selector) {

            showList();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast t = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean permission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        return result != PackageManager.PERMISSION_GRANTED;
    }

    public void request(String permission, int request_code) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, request_code);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                boolean granted = false;
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) { // Permission is granted
                        granted = true;
                    }
                }
                if (granted == true) {
                    view.updatePreview();
                } else {
                    toast("Please enable Camera permission.");
                }
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void initialized(boolean engine_initialized) {
        if (engine_initialized == true) {
            button.setOnClickListener(this);
            selector.setOnClickListener(this);
            showNameDoc();
        }
    }

    @Override
    public void recognized(RecognitionResult result) {
        // The result is terminal when the engine decides that the recognition result has had
        // enough information and ready to produce result, or when the session is timed out
        if(result.IsTerminal() == true) {
            view.stopRecognition();

            if(result.GetDocumentType().isEmpty() == true) {
                toast("Document not found."); // No result has been returned on any frame
                return;
            }

            MainResultStore.instance.setResult(result);
            Intent intent = new Intent(this, MainResultView.class);
            startActivity(intent);
        }
    }

    @Override
    public void started() {
        processing = true;
        button.setText("CANCEL");
        button.setOnClickListener(this);
        selector.setOnClickListener(null);
    }

    @Override
    public void stopped() {
        processing = false;
        button.setText("START");
        button.setOnClickListener(this);
        selector.setOnClickListener(this);
    }

    @Override
    public void error(String message) {
        toast(message);
    }

    private void showNameDoc() {
        TextView Doc = (TextView) findViewById(R.id.textViewDoc);
        Doc.setText("Document: " + document_mask);
    }

    private void showList() {

        final String[] sdocs = SelectDocs();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose document");
        builder.setItems(sdocs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                document_mask = sdocs[item];
                showNameDoc();
            }
        });

        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
