package com.ninogenio.memenator.memes

import android.content.Context
import com.ninogenio.memenator.editor.EditorActivity
import com.ninogenio.memenator.shared.core.interactor.MemeInteractorImpl
import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.rx.Presenter
import org.jetbrains.anko.startActivity
import rx.Subscriber
import java.util.*

/**
 * Created by gentra on 06/08/16.
 */
class MemesPresenter(private val context: Context, private val view: MemesView) : Presenter() {

    private val interactor = MemeInteractorImpl(context)
    val data = ArrayList<MemeModel>()

    fun reload() {
        addSubscription(interactor.list()
                .subscribeOn(scheduler.backgroundThread())
                .observeOn(scheduler.mainThread())
                .subscribe(object : Subscriber<List<MemeModel>>() {

                    override fun onStart() {
                        super.onStart()
                        view.showLoading(true)
                    }

                    override fun onError(e: Throwable?) {
                        if (e != null && e.message != null)
                            view.showMessage(e?.message!!)
                    }

                    override fun onNext(memes: List<MemeModel>?) {
                        if (memes == null) onError(Throwable("Sorry, error: Data null"))
                        data.clear()
                        data.addAll(memes!!)
                        view.refreshData()
                    }

                    override fun onCompleted() {
                        view.showLoading(false)
                    }

                }))
    }

    fun actionViewMeme(data: MemeModel) {
        interactor.view(data)
    }

    fun actionCaptureImage() {
        context.startActivity<EditorActivity>(EditorActivity.EXTRA_INT_SOURCE to EditorActivity.SOURCE_CAMERA)
    }

    fun actionPickPhoto() {
        context.startActivity<EditorActivity>(EditorActivity.EXTRA_INT_SOURCE to EditorActivity.SOURCE_PICKER)
    }

}