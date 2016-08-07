package com.ninogenio.memenator.editor

import android.support.v7.app.AppCompatActivity

/**
 * Created by gentra on 06/08/16.
 */
class EditorActivity : AppCompatActivity() {

    companion object {
        val EXTRA_INT_SOURCE = "${EditorActivity::class.java.name}.source"

        val SOURCE_CAMERA = 0
        val SOURCE_PICKER = 1
    }
}