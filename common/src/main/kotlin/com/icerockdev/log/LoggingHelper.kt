package com.icerockdev.log

internal object LoggingHelper {
    fun replaceSecretFieldsValueInJsonString(
        originalString: String,
        secretFieldList: List<String>,
        replaceMask: String = "*****"
    ): String {
        var replacedString: String = originalString
        secretFieldList.forEach { field ->
            replacedString = replacedString.replace(
                Regex("\"$field\"\\s*:\\s*(\"[^\"]*\"|[^\\s,{}\"]*)", RegexOption.IGNORE_CASE),
                "\"$field\":\"$replaceMask\""
            )
        }

        return replacedString
    }
}
