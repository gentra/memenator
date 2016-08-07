package com.ninogenio.memenator.shared.camera

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import rx.Observable

/**
 * Created by gentra on 06/08/16.
 */
interface CameraInteractor {
    fun requestCamera(requestCode: Int, saveDirName: String, fragment: Fragment? = null)
    fun handleCameraResult(requestCode: Int, resultCode: Int, data: Intent, savePath: String): Observable<String>
    fun saveInstanceState(outState: Bundle)
    fun restoreInstanceState(savedInstanceState: Bundle?)
}