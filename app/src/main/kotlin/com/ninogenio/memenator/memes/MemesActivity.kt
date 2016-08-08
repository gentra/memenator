package com.ninogenio.memenator.memes

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.ninogenio.memenator.R
import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.util.ColorUtils
import kotlinx.android.synthetic.main.activity_memes.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast

/**
 * Created by gentra on 06/08/16.
 */
class MemesActivity : AppCompatActivity(), MemesView {

    var presenter: MemesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memes)
        toolbar.title = getString(R.string.app_name)

        presenter = MemesPresenter(this, this)

        srl.setColorSchemeColors(*ColorUtils.REFRESH_COLOR_RES_IDS)
        srl.onRefresh { presenter?.reload() }

        rv.setHasFixedSize(false)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = MemesAdapter(presenter!!.data, object : MemeListHolder.Companion.Listener {
            override fun onImageClick(data: MemeModel) {
                // TODO: Set up transition animation
                presenter?.actionViewMeme(data)
            }
        })

        fab_source_camera.onClick { presenter?.actionCaptureImage() }
        fab_source_picker.onClick { presenter?.actionPickPhoto() }

        presenter?.reload()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.finish()
    }

    override fun refreshData() {
        rv.adapter.notifyDataSetChanged()
        showEmptyView(presenter?.data?.size == 0)
    }

    override fun showLoading(loading: Boolean) {
        srl.post { srl.isRefreshing = loading }
    }

    override fun showMessage(text: String) {
        toast(text)
    }

    fun showEmptyView(empty: Boolean) {
        val params = fam.layoutParams as CoordinatorLayout.LayoutParams
        if (empty) { // show empty view
            tv_empty.visibility = View.VISIBLE
            params.gravity = Gravity.CENTER
        } else { // dismiss empty view
            tv_empty.visibility = View.GONE
            params.gravity = Gravity.END
        }
    }

}