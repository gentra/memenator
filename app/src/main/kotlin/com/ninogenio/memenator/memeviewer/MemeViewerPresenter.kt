package com.ninogenio.memenator.memeviewer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ninogenio.memenator.R
import com.ninogenio.memenator.shared.core.interactor.MemeInteractorImpl
import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.rx.Presenter
import rx.Subscriber

/**
 * Created by gentra on 07/08/16.
 */
class MemeViewerPresenter(private val context: Context, private val view: MemeViewerView, private val filePath: String) : Presenter() {

    fun actionShare() {
        context.startActivity(Intent.createChooser(
                Intent().setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath))
                        .setType("image/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                , context.getString(R.string.text_share_to))
        )
    }

    fun actionDelete() {
        addSubscription(MemeInteractorImpl(context)
                .delete(MemeModel.Companion.MemeModelImpl(filePath))
                .subscribeOn(scheduler.backgroundThread())
                .observeOn(scheduler.mainThread())
                .subscribe(object : Subscriber<Boolean>() {

                    override fun onStart() {
                        super.onStart()
                        view.displayBigText("${context.getString(R.string.text_deleting)}...")
                    }

                    override fun onNext(t: Boolean?) {
                        view.showMessage(context.getString(R.string.text_delete_success))
                        // Finish activity
                        (context as Activity).finish()
                    }

                    override fun onError(e: Throwable?) {
                        if (e != null && e.message != null)
                            view.showMessage(e.message!!)
                    }

                    override fun onCompleted() {
                        view.dismissBigText()
                    }

                }))
    }

}