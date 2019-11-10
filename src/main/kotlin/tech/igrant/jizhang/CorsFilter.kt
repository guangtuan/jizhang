package tech.igrant.jizhang

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(filterName = "cors", urlPatterns = ["*"])
class CorsFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val req: HttpServletRequest = request as HttpServletRequest
        val resp: HttpServletResponse = response as HttpServletResponse
        resp.addHeader("Access-Control-Allow-Origin", "*")
        resp.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
        resp.addHeader("Access-Control-Allow-Headers", "Content-type,token")
        chain?.doFilter(req, resp)
    }
}