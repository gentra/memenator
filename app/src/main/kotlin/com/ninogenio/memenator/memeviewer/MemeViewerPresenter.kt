package com.ninogenio.memenator.memeviewer

import android.app.Activity
import android.content.Context
import com.ninogenio.memenator.R
import com.ninogenio.memenator.shared.core.interactor.MemeInteractorImpl
import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.rx.Presenter
import rx.Subscriber

/**
 * Created by gentra on 07/08/16.
 */
class MemeViewerPresenter(private val context: Context, private val view: MemeViewerView, private val filePath: String) : Presenter() {

    private val interactor = MemeInteractorImpl(context)
    private val data = MemeModel.Companion.MemeModelImpl(filePath)

    fun actionShare() {
        interactor.share(data)
    }

    fun actionDelete() {
        // Don't use background thread because of Realm restrictions
        // http://stackoverflow.com/questions/37045280/realm-observable-not-finishing-when-using-rx-java-amb-or-switchifempty
        addSubscription(interactor
                .delete(data)
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