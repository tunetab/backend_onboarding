package com.icerockdev.file.abstract

import com.icerockdev.i18n.I18N

interface IFileType {
    fun getType(): Int
    fun getLabel(i18n: I18N): String
}
