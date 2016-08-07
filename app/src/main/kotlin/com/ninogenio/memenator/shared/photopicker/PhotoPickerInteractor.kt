package com.ninogenio.memenator.shared.photopicker

import android.content.Intent
import android.support.v4.app.Fragment
import rx.Observable

/**
 * Created by gentra on 06/08/16.
 */
interface PhotoPickerInteractor {

    fun requestPhotoPicker(requestCode: Int, fragment: Fragment? = null)

    fun handlePhotoPickerResult(requestCode: Int, resultCode: Int, data: Intent, savePath: String = ""): Observable<String>

}