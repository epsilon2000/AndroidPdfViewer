/**
 * Copyright 2016 Bartosz Schiller
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.barteksc.pdfviewer.model

import android.graphics.Bitmap
import android.graphics.RectF

class PagePart(
    val page: Int,
    val renderedBitmap: Bitmap?,
    val pageRelativeBounds: RectF,
    val isThumbnail: Boolean,
    var cacheOrder: Int
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PagePart) {
            return false
        }
        val part = other
        return part.page == page && part.pageRelativeBounds.left == pageRelativeBounds.left && part.pageRelativeBounds.right == pageRelativeBounds.right && part.pageRelativeBounds.top == pageRelativeBounds.top && part.pageRelativeBounds.bottom == pageRelativeBounds.bottom
    }

    override fun hashCode(): Int {
        var result = page
        result = 31 * result + renderedBitmap.hashCode()
        result = 31 * result + pageRelativeBounds.hashCode()
        result = 31 * result + isThumbnail.hashCode()
        result = 31 * result + cacheOrder
        return result
    }
}