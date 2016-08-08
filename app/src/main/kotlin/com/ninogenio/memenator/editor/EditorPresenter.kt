package com.ninogenio.memenator.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.ninogenio.memenator.R
import com.ninogenio.memenator.shared.camera.CameraInteractor
import com.ninogenio.memenator.shared.camera.CameraInteractorImpl
import com.ninogenio.memenator.shared.core.interactor.MemeInteractorImpl
import com.ninogenio.memenator.shared.core.model.MemeModel
import com.ninogenio.memenator.shared.photopicker.PhotoPickerInteractor
import com.ninogenio.memenator.shared.photopicker.PhotoPickerInteractorImpl
import com.ninogenio.memenator.shared.rx.Presenter
import rx.Observable
import rx.Subscriber
import rx.functions.Action1

/**
 * Created by gentra on 07/08/16.
 */
class EditorPresenter(private val context: Context, private val view: EditorView) : Presenter() {

    private var cameraInteractor: CameraInteractor? = null
    private var pickerInteractor: PhotoPickerInteractor? = null

    private var drawer: EditorDrawer? = null
    private var latestBitmap: Bitmap? = null // bitmap to be saved to the Storage and DB

    init {
        cameraInteractor = CameraInteractorImpl(context)
        pickerInteractor = PhotoPickerInteractorImpl(context)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Capture the result from the camera or from the photo picker
        addSubscription(Observable.just("").flatMap { result -> cameraInteractor?.handleCameraResult(requestCode, resultCode, data, "Camera") }
                .flatMap { result -> if (result.isNullOrBlank()) pickerInteractor?.handlePhotoPickerResult(requestCode, resultCode, data) else Observable.just(result) }
                .subscribeOn(scheduler.backgroundThread())
                .observeOn(scheduler.mainThread())
                .subscribe(object : Action1<String> {
                    // onNext
                    override fun call(imageFilePath: String?) {
                        if (!imageFilePath.isNullOrBlank())
                            Glide.with(context).load(imageFilePath).asBitmap().into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                    if (resource == null) return
                                    drawer = EditorDrawer(resource, context)
                                    view.setEditable(true)
                                    view.setSeekbarProgress(50) // Init the bitmap with text size of 50
                                }

                            })
                        else
                            (context as Activity).finish()
                    }

                }, object : Action1<Throwable> {
                    //onError
                    override fun call(e: Throwable?) {
                        if (e != null && e.message != null)
                            view.showMessage(e.message!!)
                    }

                })!!)
    }

    fun actionLaunchCamera() {
        cameraInteractor?.requestCamera(REQUEST_CAMERA, context.getString(R.string.constant_camera_save_dir_name))
    }

    fun actionLaunchPicker() {
        pickerInteractor?.requestPhotoPicker(REQUEST_PICKER)
    }

    fun actionSave() {
        if (latestBitmap == null) return view.showMessage(context.getString(R.string.text_error_meme_unmodified))
        val memeInteractor = MemeInteractorImpl(context)
        addSubscription(memeInteractor.save(latestBitmap!!)
                .subscribeOn(scheduler.backgroundThread())
                .observeOn(scheduler.mainThread())
                .subscribe(object : Subscriber<MemeModel>() {

                    override fun onNext(t: MemeModel?) {
                        if (t == null) return onError(Throwable("Save failed"))
                        view.showMessage(context.getString(R.string.text_save_success))
                        memeInteractor.view(t)
                        (context as Activity).finish()
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (e != null && e.message != null)
                            view.showMessage(e.message!!)
                    }

                }))
    }

    fun actionChangeText(topText: String, bottomText: String, textSize: Float) {
        drawer?.textSize = textSize
        addSubscription(drawer!!.generateBitmap(topText, bottomText)
                .subscribeOn(scheduler.backgroundThread())
                .observeOn(scheduler.mainThread())
                .subscribe(object : Subscriber<Bitmap>() {

                    override fun onNext(bitmap: Bitmap?) {
                        if (bitmap == null) return onError(Throwable("Error: Bitmap null"))
                        latestBitmap = bitmap
                        view.setBitmap(bitmap)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (e != null && e.message != null)
                            view.showMessage(e.message!!)
                    }

                }))
    }

    companion object {
        val REQUEST_CAMERA = 1
        val REQUEST_PICKER = 2
    }

}