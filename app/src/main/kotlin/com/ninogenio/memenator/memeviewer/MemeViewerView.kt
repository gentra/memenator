package com.ninogenio.memenator.memeviewer

/**
 * Created by gentra on 07/08/16.
 */
interface MemeViewerView {
    fun displayImage(filePath: String)
    fun displayBigText(text: String)
    fun dismissBigText()
    fun showMessage(text: String)
}