package cn.caojiantao.spider.configuration;

import cn.caojiantao.spider.util.NetUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author caojiantao
 */
@Slf4j
public class SpiderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        String ip = NetUtils.getIpAddress(request);
        log.info("ip:{} uri:{} request start", ip, uri);

        long t = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        long cost = System.currentTimeMillis() - t;

        log.info("ip:{} uri:{} cost:{} request end", ip, uri, cost);
    }
}
