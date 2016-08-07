package com.ninogenio.memenator.shared.rx

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by gentra on 06/08/16.
 */
class AppScheduler : Scheduler {

    override fun mainThread(): rx.Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun backgroundThread(): rx.Scheduler {
        return Schedulers.io()
    }
}