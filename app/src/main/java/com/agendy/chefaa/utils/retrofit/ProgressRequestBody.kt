package com.agendy.chefaa.utils.retrofit

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.IOException

class ProgressRequestBody(
    private val file: File,
    private val contentType: String,
    private val progressListener: ((Long, Long) -> Unit)
) : RequestBody() {

    override fun contentType(): MediaType? {
        return contentType.toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return file.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val source = file.source()
        var totalBytesWritten: Long = 0
        var readBytes: Long
        while (source.read(sink.buffer, SEGMENT_SIZE).also { readBytes = it } != -1L) {
            totalBytesWritten += readBytes
            sink.flush()
            progressListener(totalBytesWritten, file.length())
        }
        source.close()
    }

    companion object {
        private const val SEGMENT_SIZE = 2048L
    }
}
