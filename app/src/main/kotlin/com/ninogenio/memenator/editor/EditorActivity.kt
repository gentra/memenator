package com.ninogenio.memenator.editor

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import com.ninogenio.memenator.R
import kotlinx.android.synthetic.main.activity_editor.*
import org.jetbrains.anko.enabled
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

/**
 * Created by gentra on 06/08/16.
 */
class EditorActivity : AppCompatActivity(), EditorView {

    private var presenter: EditorPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        presenter = EditorPresenter(this, this)
        showLoading(true)

        // Check which Meme source to be used
        when (intent.extras.get(EXTRA_INT_SOURCE)) {
            SOURCE_CAMERA -> presenter?.actionLaunchCamera()
            SOURCE_PICKER -> presenter?.actionLaunchPicker()
            else -> {
                showMessage(getString(R.string.text_error_source_unidentified))
                finish()
            }
        }

        et_top.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                presenter?.actionChangeText(text.toString(), et_bottom.text.toString(), sb_text_size.progress.toFloat())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        et_bottom.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                presenter?.actionChangeText(et_top.text.toString(), text.toString(), sb_text_size.progress.toFloat())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        sb_text_size.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                presenter?.actionChangeText(et_top.text.toString(), et_bottom.text.toString(), progress.toFloat())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        fab_save.onClick { presenter?.actionSave() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.finish()
    }

    override fun setSeekbarProgress(progress: Int) {
        sb_text_size.progress = progress
    }

    override fun showLoading(loading: Boolean) {
        if (loading)
            tv_loading.visibility = View.VISIBLE
        else
            tv_loading.visibility = View.GONE

        setEditable(!loading)
    }

    override fun showMessage(text: String) {
        toast(text)
    }

    override fun setBitmap(bitmap: Bitmap) {
        iv.imageBitmap = bitmap
    }

    override fun setEditable(editable: Boolean) {
        et_top.enabled = editable
        et_bottom.enabled = editable
        fab_save.isEnabled = editable
        sb_text_size.isEnabled = editable
    }

    companion object {
        val EXTRA_INT_SOURCE = "${EditorActivity::class.java.name}.source"

        val SOURCE_CAMERA = 0
        val SOURCE_PICKER = 1
    }
}