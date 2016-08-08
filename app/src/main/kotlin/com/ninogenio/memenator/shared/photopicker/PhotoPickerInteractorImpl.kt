package com.ninogenio.memenator.shared.photopicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.ninogenio.memenator.shared.storage.StorageInteractorImpl
import com.ninogenio.memenator.shared.util.FileUtils
import rx.Observable

/**
 * Created by gentra on 06/08/16.
 */
class PhotoPickerInteractorImpl(val context: Context) : PhotoPickerInteractor {

    private var requestCode = -1

    override fun requestPhotoPicker(requestCode: Int, fragment: Fragment?) {
        val intent = Intent(Intent.ACTION_PICK).setType("image/*")
        if (fragment != null) fragment.startActivityForResult(intent, requestCode)
        else (context as Activity).startActivityForResult(intent, requestCode)
        this.requestCode = requestCode
    }

    override fun handlePhotoPickerResult(requestCode: Int, resultCode: Int, data: Intent?, savePath: String): Observable<String> {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK && data?.data != null) {
            val filePath = FileUtils.getPath(context, data?.data!!)
            if (filePath != null && savePath != "") return StorageInteractorImpl(context).copyImage(filePath, savePath)
            else if (filePath != null) return Observable.just(filePath)
        }
        return Observable.just("")
    }


}