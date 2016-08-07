package com.ninogenio.memenator.shared.core.interactor

import android.content.Context
import android.graphics.Bitmap
import com.ninogenio.memenator.memeviewer.MemeViewerActivity
import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.database.interactor.MemeDbInteractorImpl
import com.ninogenio.memenator.shared.storage.StorageInteractorImpl
import org.jetbrains.anko.startActivity
import rx.Observable
import rx.functions.Func2

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
        return StorageInteractorImpl(context).deleteFile(meme.filePath).withLatestFrom(MemeDbInteractorImpl(context).delete(meme), object : Func2<Boolean, Boolean, Boolean> {

            override fun call(storageDeleted: Boolean?, dbDeleted: Boolean?): Boolean {
                if (storageDeleted == null || dbDeleted == null)
                    return false
                return storageDeleted && dbDeleted
            }

        })
    }

    override fun share(meme: MemeModel) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}