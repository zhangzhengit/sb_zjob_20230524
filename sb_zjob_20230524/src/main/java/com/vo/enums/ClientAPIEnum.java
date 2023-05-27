package com.vo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * ClientAPIEnum
 *
 * @author zhangzhen
 * @date 2023年5月27日
 *
 */
@Getter
@AllArgsConstructor
public enum ClientAPIEnum {

	RECEIVE("/receive","推送最新的job配置信息到client"),

	EXECUTE_IMMEDIATEL("/executeImmediatel","立即执行client的job"),

	;

	private String url;
	private String description;
}
