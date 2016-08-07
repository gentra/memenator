package com.ninogenio.memenator.shared.core.interactor

import android.content.Context
import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.database.interactor.MemeDbInteractorImpl
import com.ninogenio.memenator.shared.storage.StorageInteractorImpl
import rx.Observable
import rx.functions.Func2

/**
 * Created by gentra on 06/08/16.
 */
class MemeInteractorImpl(private val context: Context) : MemeInteractor {

    override fun list() = MemeDbInteractorImpl(context).list()

    override fun view(meme: MemeModel) {
        // TODO: Open MemeViewer page
    }

    override fun save(meme: MemeModel): Observable<Boolean> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(meme: MemeModel): Observable<Boolean> {
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