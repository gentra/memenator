package com.ninogenio.memenator.shared.storage

import android.graphics.Bitmap
import rx.Observable

/**
 * Created by gentra on 06/08/16.
 */
interface StorageInteractor {

    fun copyImage(filePath: String, savePath: String): Observable<String>

    fun saveImage(bitmap: Bitmap, savePath: String): Observable<String>

    fun deleteFile(filePath: String): Observable<Boolean>

}