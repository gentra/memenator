package com.ninogenio.memenator.editor

import android.graphics.Bitmap

/**
 * Created by gentra on 07/08/16.
 */
interface EditorView {
    fun setBitmap(bitmap: Bitmap)
    fun showMessage(text: String)
    fun setEditable(editable: Boolean)
}