package com.ninogenio.memenator.shared.database.model

import com.ninogenio.memenator.shared.core.model.MemeModel
import io.realm.RealmObject
import java.util.*

/**
 * Created by gentra on 06/08/16.
 */
open class MemeDbModel : MemeModel, RealmObject() {

    var id: Int? = null
    override var filePath: String = ""
    var createdAt: Date = Date()

}