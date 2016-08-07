package com.ninogenio.memenator.shared.rx

import rx.Subscription
import java.util.*

/**
 * Created by gentra on 06/08/16.
 */
open class Presenter {

    private val subscriptions = ArrayList<Subscription>()
    protected var scheduler: AppScheduler

    init {
        scheduler = AppScheduler()
    }

    protected fun addSubscription(subscription: Subscription) {
        subscriptions.add(subscription)
    }

    open fun finish() {
        for (sub in subscriptions) {
            sub.unsubscribe()
        }
    }

}