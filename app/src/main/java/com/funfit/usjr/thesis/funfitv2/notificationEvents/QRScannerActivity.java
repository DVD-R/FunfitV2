package com.funfit.usjr.thesis.funfitv2.notificationEvents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Dj on 1/23/2016.
 */
public class QRScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        Log.d("BarcodeScanner", "text: " + rawResult.getText());
        Log.d("BarcodeScanner", rawResult.getBarcodeFormat().toString());

        Intent returnIntent = new Intent();
        returnIntent.putExtra("RAW_RESULT", rawResult.getText());

        setResult(RESULT_OK, returnIntent);
        finish();
    }
}