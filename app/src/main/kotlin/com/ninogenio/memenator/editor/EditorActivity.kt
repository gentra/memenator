package com.ninogenio.memenator.editor

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
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
        setEditable(false)

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
                presenter?.actionChangeText(text.toString(), et_bottom.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        et_bottom.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                presenter?.actionChangeText(et_top.text.toString(), text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        fab_save.onClick { presenter?.actionSave() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null)
            presenter?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.finish()
    }

    override fun showMessage(text: String) {
        toast(text)
    }

    override fun setBitmap(bitmap: Bitmap) {
        iv.imageBitmap = bitmap
        // TODO: Find best practice to set bitmap
    }

    override fun setEditable(editable: Boolean) {
        et_top.enabled = editable
        et_bottom.enabled = editable
        fab_save.isEnabled = editable
    }

    companion object {
        val EXTRA_INT_SOURCE = "${EditorActivity::class.java.name}.source"

        val SOURCE_CAMERA = 0
        val SOURCE_PICKER = 1
    }
}