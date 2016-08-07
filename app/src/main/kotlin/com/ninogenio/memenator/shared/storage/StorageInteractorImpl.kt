package com.ninogenio.memenator.shared.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Environment
import android.view.WindowManager
import com.ninogenio.memenator.R
import com.ninogenio.memenator.shared.util.FileUtils
import com.ninogenio.memenator.shared.util.TimeUtils
import rx.Observable
import java.io.File

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

    override fun saveImage(bitmap: Bitmap, savePath: String) = Observable.create<Boolean> { subscriber ->
        // TODO: save bitmap image to original file and thumbnail file
        // Get screen width
        val size = Point()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)
        val screenWidth = size.x
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