package com.ninogenio.memenator.shared.database.interactor

import com.ninogenio.memenator.shared.core.model.MemeModel
import rx.Observable

/**
 * Created by gentra on 06/08/16.
 */
interface MemeDbInteractor {

    fun list(): Observable<List<MemeModel>>

    fun save(meme: MemeModel): Observable<Boolean>
    fun delete(meme: MemeModel): Observable<Boolean>?

}