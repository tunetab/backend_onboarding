package com.icerockdev.encoding

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.web.filter.GenericFilterBean

class EncodingFilter : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        request?.characterEncoding = "UTF-8"
        response?.characterEncoding = "UTF-8"

        chain?.doFilter(request, response)
    }
}
