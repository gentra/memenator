package com.ninogenio.memenator.shared.database.interactor

import android.content.Context
import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.database.DbFactory
import com.ninogenio.memenator.shared.database.model.MemeDbModel
import rx.Observable

/**
 * Created by gentra on 06/08/16.
 */
class MemeDbInteractorImpl(context: Context) : MemeDbInteractor {

    private val db = DbFactory.create(context)

    override fun list() = Observable.create<List<MemeModel>> { subscriber ->
        db.where(MemeDbModel::class.java).findAllAsync().addChangeListener { results ->
            subscriber.onNext(results)
            subscriber.onCompleted()
        }
    }

    override fun save(meme: MemeModel) = Observable.create<MemeDbModel> { subscriber ->
        try {
            db.beginTransaction()
            val data = MemeDbModel()
            data.filePath = meme.filePath
            db.copyToRealm(data)
            db.commitTransaction()
            subscriber.onNext(data)
        } catch (e: IllegalArgumentException) {
            subscriber.onError(e)
        } finally {
            subscriber.onCompleted()
        }
    }

    override fun delete(meme: MemeModel) = Observable.create<Boolean> { subscriber ->
        db.where(MemeDbModel::class.java).equalTo("filePath", meme.filePath).findFirstAsync().addChangeListener<MemeDbModel> { result ->
            try {
                db.beginTransaction()
                result.deleteFromRealm()
                db.commitTransaction()
                subscriber.onNext(true)
            } catch (e: IllegalStateException) {
                subscriber.onNext(false)
                subscriber.onError(e)
            } finally {
                subscriber.onCompleted()
            }
        }
    }
}