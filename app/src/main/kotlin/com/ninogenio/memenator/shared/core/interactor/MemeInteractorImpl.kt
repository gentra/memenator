package com.ninogenio.memenator.shared.core.interactor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.ninogenio.memenator.R
import com.ninogenio.memenator.memeviewer.MemeViewerActivity
import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.database.interactor.MemeDbInteractorImpl
import com.ninogenio.memenator.shared.storage.StorageInteractorImpl
import org.jetbrains.anko.startActivity
import rx.Observable

/**
 * Created by gentra on 06/08/16.
 */
class MemeInteractorImpl(private val context: Context) : MemeInteractor {

    override fun list() = MemeDbInteractorImpl(context).list()

    override fun view(meme: MemeModel) {
        context.startActivity<MemeViewerActivity>(MemeViewerActivity.EXTRA_FILE_PATH to meme.filePath)
    }

    override fun save(bitmap: Bitmap): Observable<MemeModel> {
        // Save to Storage then Database
        return StorageInteractorImpl(context).saveImage(bitmap, "Meme")
                .flatMap { filePath ->
                    val data: MemeModel = MemeModel.Companion.MemeModelImpl(filePath)
                    MemeDbInteractorImpl(context).save(data)
                }
    }

    override fun delete(meme: MemeModel): Observable<Boolean> {
        // Delete from Storage and Database
        return StorageInteractorImpl(context).deleteFile(meme.filePath).flatMap { deleted ->
            if (deleted) // if file successfully deleted, delete the db
                MemeDbInteractorImpl(context).delete(meme)
            else // failed to delete
                Observable.just(false)
        }
    }

    override fun share(meme: MemeModel) {
        context.startActivity(Intent.createChooser(
                Intent().setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_STREAM, Uri.parse(meme.filePath))
                        .setType("image/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                , context.getString(R.string.text_share_to))
        )
    }

}