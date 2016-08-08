package com.ninogenio.memenator.shared.core.model

/**
 * Created by gentra on 06/08/16.
 */
interface MemeModel {

    var filePath: String
    var thumbFilePath: String

    companion object {
        class MemeModelImpl(override var filePath: String) : MemeModel {
            override var thumbFilePath: String = ""
        }
    }

}