package com.icerockdev.file.tests.fixture

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.JpegWriter
import com.sksamuel.scrimage.nio.PngWriter
import java.io.ByteArrayInputStream
import org.springframework.boot.test.context.TestComponent

@TestComponent
class ImageFixtureBuilder {
    fun buildJpgImage(): ByteArrayInputStream {
        return ImmutableImage.create(1024, 1024).forWriter(JpegWriter()).stream()
    }

    fun buildPngImage(): ByteArrayInputStream {
        return ImmutableImage.create(1024, 1024).forWriter(PngWriter()).stream()
    }
}
