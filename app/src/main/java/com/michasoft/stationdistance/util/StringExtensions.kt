package com.michasoft.stationdistance.util

import org.apache.commons.lang3.StringUtils


/**
 * Converts diacritical marks to their equivalents
 */
fun String.normalize(): String {
    return StringUtils.stripAccents(this)
}