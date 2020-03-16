package cn.caojiantao.spider.configuration;

import cn.caojiantao.spider.RequestWrapper;
import cn.caojiantao.spider.ResponseWrapper;
import cn.caojiantao.spider.util.LogContext;
import cn.caojiantao.spider.util.NetUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author caojiantao
 */
@Slf4j
@WebFilter(urlPatterns = {"/*"})
public class SpiderFilter implements Filter {

    private List<String> excludePathList = Arrays.asList("/favicon.ico", "/index.html", "/css/*", "/js/*");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (excludePathList.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 追踪日志
        LogContext.setTraceId();
        // 包装流，可重复读取
        RequestWrapper requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        // 请求参数
        String traceId = LogContext.getTraceId();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String data = new String(requestWrapper.toByteArray());
        String query = request.getQueryString();
        String ip = NetUtils.getIpAddress(request);

        log.info("request traceId:{} method:{} uri:{} data:{} query:{} ip:{}", traceId, method, uri, data, query, ip);

        long t = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        // 响应参数
        String resp = new String(responseWrapper.toByteArray());
        // 注意重写了 responseWrapper 后，需要手动返回接口数据
        responseWrapper.getWriter().print(resp);
        long cost = System.currentTimeMillis() - t;

        log.info("response traceId:{} method:{} uri:{} data:{} query:{} ip:{} response:{} cost:{}", traceId, method, uri, data, query, ip, resp, cost);

        LogContext.clear();
    }
}
