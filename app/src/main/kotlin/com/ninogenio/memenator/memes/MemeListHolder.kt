package com.ninogenio.memenator.memes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ninogenio.memenator.R
import com.ninogenio.memenator.shared.core.model.MemeModel
import kotlinx.android.synthetic.main.list_image.view.*
import org.jetbrains.anko.onClick

/**
 * Created by gentra on 06/08/16.
 */
class MemeListHolder(val parent: ViewGroup, val listener: Listener) : RecyclerView.ViewHolder(createView(parent)) {

    fun bindView(position: Int, data: MemeModel) {
        if (data.filePath.isNotEmpty())
            Glide.with(parent.context).load(data.filePath).centerCrop().into(itemView.iv)

        itemView.iv.onClick { listener.onImageClick(data) }
    }

    companion object {
        fun createView(parent: ViewGroup) = LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false)

        interface Listener {

            fun onImageClick(data: MemeModel)

        }
    }

}