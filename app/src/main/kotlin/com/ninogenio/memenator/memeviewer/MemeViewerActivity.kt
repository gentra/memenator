package com.ninogenio.memenator.memeviewer

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.davemorrissey.labs.subscaleview.ImageSource
import com.ninogenio.memenator.R
import kotlinx.android.synthetic.main.activity_memeviewer.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

/**
 * Created by gentra on 07/08/16.
 */
class MemeViewerActivity : AppCompatActivity(), MemeViewerView {

    private var presenter: MemeViewerPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memeviewer)

        presenter = MemeViewerPresenter(this, this, intent.getStringExtra(EXTRA_FILE_PATH))
        displayImage(intent.getStringExtra(EXTRA_FILE_PATH))

        fab_share.onClick { presenter?.actionShare() }
        fab_delete.onClick { presenter?.actionDelete() }

        iv_image.onClick { // Hide the buttons when image is clicked
            if (fab_share.isShown && fab_delete.isShown) {
                fab_share.visibility = View.GONE
                fab_delete.visibility = View.GONE
            } else {
                fab_share.visibility = View.VISIBLE
                fab_delete.visibility = View.VISIBLE
            }
        }

        Handler().postDelayed(object: Runnable{ // Hide the buttons a moment after opening the image
            override fun run() {
                fab_share.visibility = View.GONE
                fab_delete.visibility = View.GONE
            }

        }, HIDE_BUTTON_DELAY)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.finish()
    }

    override fun displayImage(filePath: String) {
        Glide.with(this).load(filePath).asBitmap().into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                iv_image?.setImage(ImageSource.bitmap(resource))
            }

        })
    }

    override fun showMessage(text: String) = toast(text)

    override fun displayBigText(text: String) {
        if (text.isBlank()) return
        tv_full.text = text
        tv_full.visibility = View.VISIBLE
        fab_delete.isEnabled = false
        fab_share.isEnabled = false
    }

    override fun dismissBigText() {
        tv_full.text = ""
        tv_full.visibility = View.GONE
        fab_delete.isEnabled = true
        fab_share.isEnabled = true
    }

    companion object {
        val EXTRA_FILE_PATH = "${MemeViewerActivity::class.java.name}.filePath"

        val HIDE_BUTTON_DELAY = 1500L // milliseconds, 1.5 seconds
    }

}