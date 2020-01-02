package cn.caojiantao.spider;

import com.github.caojiantao.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author caojiantao
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultDTO handleException(Exception e) {
        log.error("全局异常捕获：", e);
        return ResultDTO.failure(e.getMessage());
    }
}
