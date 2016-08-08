package com.ninogenio.memenator.shared.storage

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.ninogenio.memenator.R
import com.ninogenio.memenator.shared.util.FileUtils
import com.ninogenio.memenator.shared.util.TimeUtils
import rx.Observable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by gentra on 06/08/16.
 */
class StorageInteractorImpl(val context: Context) : StorageInteractor {

    override fun copyImage(filePath: String, savePath: String): Observable<String> {
        var processedSavePath = savePath.trim()
        // Remove first & last directory separator
        if (processedSavePath.startsWith("/"))
            processedSavePath = processedSavePath.substring(1, processedSavePath.length)
        if (processedSavePath.endsWith("/"))
            processedSavePath = processedSavePath.substring(processedSavePath.length - 1, processedSavePath.length)

        // Create directory if it doesn't exist yet
        val saveDir = File("${Environment.getExternalStorageDirectory().toString()}/${context.getString(R.string.app_name)}/$savePath/")
        saveDir.mkdirs()

        val filename = "${TimeUtils.getTimestamp(TimeUtils.FULL_TIME_FORMAT)}.jpg"
        val resultNewPath = Observable.just(File(saveDir.path, filename).path)

        FileUtils.copyFile(filePath, filename, saveDir.path)

        return resultNewPath
    }

    override fun saveImage(bitmap: Bitmap, savePath: String, scaleMultiplier: Float) = Observable.create<String> { subscriber ->
        val newBitmap: Bitmap
        if (scaleMultiplier != 1f) { // Scale the size if requested
            newBitmap = Bitmap.createScaledBitmap(bitmap,
                    (bitmap.width * scaleMultiplier).toInt(),
                    (bitmap.height * scaleMultiplier).toInt(),
                    true)
        } else {
            newBitmap = bitmap
        }

        val saveDir = File("${Environment.getExternalStorageDirectory().toString()}/${context.getString(R.string.app_name)}/$savePath/")
        saveDir.mkdirs()

        val newFile = File(saveDir.path, "${TimeUtils.getTimestamp(TimeUtils.FULL_TIME_FORMAT)}.jpg")
        val fos = FileOutputStream(newFile)
        try {
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        } catch (e: Exception) {
            subscriber.onError(e)
        } finally {
            try {
                fos.close()
                subscriber.onNext(newFile.path)
            } catch (e: IOException) {
                subscriber.onError(e)
            }
        }
        subscriber.onCompleted()
    }

    override fun deleteFile(filePath: String) = Observable.create<Boolean> { subscriber ->
        try {
            FileUtils.deleteFile("", filePath)
            subscriber.onNext(true)
        } catch (e: Exception) {
            subscriber.onError(e)
        } finally {
            subscriber.onCompleted()
        }
    }


}