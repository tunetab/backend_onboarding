package com.icerockdev.file.tests.fixture

import com.icerockdev.file.abstract.IFileType
import com.icerockdev.i18n.I18N

enum class TestFileType(private val type: Int, private val label: String) : IFileType {
    TEST_FILE(1, "Test first file"),
    TEST_SECOND_FILE(2, "Test second file");

    override fun getType(): Int {
        return type
    }

    override fun getLabel(i18n: I18N): String {
        return label
    }

}
