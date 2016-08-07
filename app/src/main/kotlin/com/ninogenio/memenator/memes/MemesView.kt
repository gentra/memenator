package com.ninogenio.memenator.memes

/**
 * Created by gentra on 06/08/16.
 */
interface MemesView {
    fun showLoading(loading: Boolean)
    fun showMessage(text: String)
    fun refreshData()
}