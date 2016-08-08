package com.ninogenio.memenator.shared.core.interactor

import android.graphics.Bitmap
import com.ninogenio.memenator.shared.core.model.MemeModel
import rx.Observable

/**
 * Created by gentra on 06/08/16.
 */
interface MemeInteractor {

    fun list(): Observable<List<MemeModel>>

    fun view(meme: MemeModel)

    fun save(bitmap: Bitmap): Observable<MemeModel?>

    fun delete(meme: MemeModel): Observable<Boolean>

    fun share(meme: MemeModel)

}