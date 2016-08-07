package com.ninogenio.memenator.shared.database.interactor

import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.database.model.MemeDbModel
import rx.Observable

/**
 * Created by gentra on 06/08/16.
 */
interface MemeDbInteractor {

    fun list(): Observable<List<MemeModel>>

    fun save(meme: MemeModel): Observable<MemeDbModel>
    fun delete(meme: MemeModel): Observable<Boolean>?

}