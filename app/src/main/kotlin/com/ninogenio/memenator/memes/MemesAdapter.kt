package com.ninogenio.memenator.memes

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ninogenio.memenator.shared.core.model.MemeModel

/**
 * Created by gentra on 06/08/16.
 */
class MemesAdapter(val data: List<MemeModel>, val listener: MemeListHolder.Companion.Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MemeListHolder(parent, listener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) =
            (holder as MemeListHolder).bindView(position, data[position])

    override fun getItemCount() = data.size
}