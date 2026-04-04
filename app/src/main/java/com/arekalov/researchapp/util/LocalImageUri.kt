package com.arekalov.researchapp.util

import android.net.Uri

private const val ASSET_FILE_PREFIX = "file:///android_asset/"

/**
 * Модель для Glide: локальные картинки из assets (`file:///android_asset/...`).
 * Сегменты пути кодируются (апострофы и т.д.), иначе [Uri.parse] может ломать загрузку.
 */
fun glideImageModel(storedUri: String): Any {
    if (!storedUri.startsWith(ASSET_FILE_PREFIX)) return storedUri
    val relative = storedUri.removePrefix(ASSET_FILE_PREFIX)
    if (relative.isEmpty()) return storedUri
    val encoded = relative.split("/").joinToString("/") { segment ->
        Uri.encode(segment)
    }
    return Uri.parse(ASSET_FILE_PREFIX + encoded)
}
