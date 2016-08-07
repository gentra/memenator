package com.ninogenio.memenator.shared.database

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by gentra on 06/08/16.
 */
object DbFactory {

    fun create(context: Context) = Realm.getInstance(RealmConfiguration.Builder(context).build())

}