package com.vo.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.votool.common.CR;
import com.votool.exception.NotLoginException;

import cn.hutool.core.util.StrUtil;


/**
 *
 *
 * @author zhangzhen
 * @date 2022年6月30日
 *
 */
@RestControllerAdvice
public class ZEControllerAdvice {


	@SuppressWarnings("static-method")
	@ExceptionHandler({ Exception.class }) // Exceptionr.classs的作用是代表异常的类型，这里Exception就代表全部的类型
	public Object handlerException(final Exception e, final HttpServletRequest request,
			final HttpServletResponse response) {

		e.printStackTrace();

		if (this.isAjax(request)) {
			return CR.error(e.getMessage());
		}

		// 未登录，去往登录页面
		if (e instanceof NotLoginException) {
			final ModelAndView mv = new ModelAndView();
			mv.addObject("message", "");
			mv.setViewName("login");
			return mv;
		}

		final ModelAndView mv = new ModelAndView();
		final String errorMessage = e.getMessage();
		final String message = StrUtil.isEmpty(errorMessage)
				? (e.getClass().getCanonicalName() + " - " + e.getStackTrace()[0])
				: errorMessage;
		mv.addObject("message", message);
		mv.setViewName("error_html");
		return mv;
	}

	/**
	 * 判断网络请求是否为ajax
	 *
	 * @param req
	 * @return
	 */
	private boolean isAjax(final HttpServletRequest req) {
		final String contentTypeHeader = req.getHeader("Content-Type");
		final String acceptHeader = req.getHeader("Accept");
		final String xRequestedWith = req.getHeader("X-Requested-With");
		return (contentTypeHeader != null && contentTypeHeader.contains("application/json"))
				|| (acceptHeader != null && acceptHeader.contains("application/json"))
				|| "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
	}


}
