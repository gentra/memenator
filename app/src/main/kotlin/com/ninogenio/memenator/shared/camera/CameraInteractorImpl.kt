package com.ninogenio.memenator.shared.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.ninogenio.memenator.R
import com.ninogenio.memenator.shared.util.TimeUtils
import rx.Observable
import java.io.File

/**
 * Created by gentra on 06/08/16.
 */
class CameraInteractorImpl(val context: Context) : CameraInteractor {

    private var requestCode = -1
    private var capturedImageUri: Uri = Uri.EMPTY

    override fun requestCamera(requestCode: Int, saveDirName: String, fragment: Fragment?) {
        // Create the directory if it doesn't exist yet
        val saveDir = File("${Environment.getExternalStorageDirectory().toString()}/${context.getString(R.string.app_name)}/$saveDirName/")
        saveDir.mkdirs()

        val saveFile = File(saveDir.path, "${TimeUtils.getTimestamp(TimeUtils.FULL_TIME_FORMAT)}.jpg")
        capturedImageUri = Uri.fromFile(saveFile)

        // Launch the camera intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri)
        if (fragment != null) fragment.startActivityForResult(intent, requestCode)
        else (context as Activity).startActivityForResult(intent, requestCode)
        this.requestCode = requestCode
    }

    override fun handleCameraResult(requestCode: Int, resultCode: Int, data: Intent, savePath: String): Observable<String> {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {
            val result = Observable.just(File(capturedImageUri.path).path)
            capturedImageUri = Uri.EMPTY
            return result
        }
        return Observable.just("")
    }

    override fun saveInstanceState(outState: Bundle) {
        if (capturedImageUri != Uri.EMPTY && !TextUtils.isEmpty(capturedImageUri.path))
            outState.putString(STATE_CAPTURED_IMAGE_PATH, capturedImageUri.path)
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null)
            capturedImageUri = Uri.Builder().path(savedInstanceState.getString(STATE_CAPTURED_IMAGE_PATH)).build()
    }

    companion object {
        val STATE_CAPTURED_IMAGE_PATH = "capturedImageUriPath"
    }

}