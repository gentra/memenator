package com.ninogenio.memenator.shared.util

import android.text.format.DateFormat
import java.util.*

/**
 * Created by gentra on 06/08/16.
 */
object TimeUtils {

    fun getTimestamp(format: String) = DateFormat.format(format, Calendar.getInstance()).toString()

    val FULL_TIME_FORMAT = "MM.dd.yyyy-kk.mm.ss"

}