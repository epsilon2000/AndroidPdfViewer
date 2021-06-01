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
package com.github.barteksc.pdfviewer

import android.os.AsyncTask
import com.github.barteksc.pdfviewer.source.DocumentSource
import com.shockwave.pdfium.PdfiumCore
import com.shockwave.pdfium.util.Size
import java.lang.ref.WeakReference

internal class DecodingAsyncTask(
    private val docSource: DocumentSource,
    password: String?,
    private val userPages: IntArray?,
    pdfView: PDFView,
    pdfiumCore: PdfiumCore
) : AsyncTask<Void?, Void?, Throwable?>() {
    private var cancelled = false
    private val pdfViewReference: WeakReference<PDFView> = WeakReference(pdfView)
    private val pdfiumCore: PdfiumCore
    private var password: String? = null
    private var pdfFile: PdfFile? = null
    override fun doInBackground(vararg params: Void?): Throwable? {
        return try {
            val pdfView = pdfViewReference.get()
            if (pdfView != null) {
                val pdfDocument = docSource.createDocument(pdfView.context, pdfiumCore, password)
                pdfFile = PdfFile(
                    pdfiumCore,
                    pdfDocument,
                    pdfView.pageFitPolicy,
                    getViewSize(pdfView),
                    userPages,
                    pdfView.isSwipeVertical,
                    pdfView.spacingPx,
                    pdfView.isAutoSpacingEnabled,
                    pdfView.isFitEachPage
                )
                null
            } else {
                NullPointerException("pdfView == null")
            }
        } catch (t: Throwable) {
            t
        }
    }

    private fun getViewSize(pdfView: PDFView): Size {
        return Size(pdfView.width, pdfView.height)
    }

    override fun onPostExecute(t: Throwable?) {
        val pdfView = pdfViewReference.get()
        if (pdfView != null) {
            if (t != null) {
                pdfView.loadError(t)
                return
            }
            if (!cancelled) {
                pdfView.loadComplete(pdfFile)
            }
        }
    }

    override fun onCancelled() {
        cancelled = true
    }

    init {
        this.password = password
        this.pdfiumCore = pdfiumCore
    }
}