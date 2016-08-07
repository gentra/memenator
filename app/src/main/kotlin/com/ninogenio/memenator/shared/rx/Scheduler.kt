package com.ninogenio.memenator.shared.rx

/**
 * Created by gentra on 06/08/16.
 */
interface Scheduler {

    fun mainThread(): rx.Scheduler

    fun backgroundThread(): rx.Scheduler

}