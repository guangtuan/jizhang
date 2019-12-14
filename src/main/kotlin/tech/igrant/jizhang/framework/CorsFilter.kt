package tech.igrant.jizhang.framework

import org.springframework.web.bind.annotation.RequestMethod
import java.util.*
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
        setHeader(resp)
        if (Objects.equals(req.method, RequestMethod.OPTIONS.toString())) {
            resp.status = 200;
            return;
        }
        chain?.doFilter(req, resp)
    }

    private fun setHeader(resp: HttpServletResponse) {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        resp.addHeader("Access-Control-Max-Age", "1800");
        resp.addHeader("Access-Control-Allow-Headers", "content-type, Origin, token");
    }
}