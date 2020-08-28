package tech.igrant.jizhang.framework

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import tech.igrant.jizhang.session.SessionRepo
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(filterName = "session", urlPatterns = ["/api/*"])
class SessionFilter(private val sessionRepo: SessionRepo) : Filter {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val req: HttpServletRequest = request as HttpServletRequest
        val resp: HttpServletResponse = response as HttpServletResponse
        val headerNames = req.headerNames.toList()
        if (!headerNames.contains("token") || !headerNames.contains("email")) {
            resp.sendError(HttpStatus.UNAUTHORIZED.value(), "没有访问该资源的权限，请登陆")
            return
        }
        val token = req.getHeader("token")
        val email = req.getHeader("email")
        logger.info(request.requestURI)
        logger.info(request.contextPath)
        if (sessionRepo.invalid(email = email, token = token)) {
            resp.sendError(HttpStatus.UNAUTHORIZED.value(), "没有访问该资源的权限，请登陆")
            return
        }
        chain?.doFilter(req, resp)
    }


}