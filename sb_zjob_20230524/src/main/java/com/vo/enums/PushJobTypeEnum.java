package com.vo.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 推送的job信息的类型
 *
 * @author zhangzhen
 * @date 2023年5月27日
 *
 */
@Getter
@AllArgsConstructor
public enum PushJobTypeEnum {

	EXECUTE_IMMEDIATEL(1, "立即执行job"),

	UPDATE_CONFIGURATION(2, "更新job信息"),

	;

	private Integer type;
	private String description;

	private static Map<Integer, PushJobTypeEnum> map = new HashMap<>();

	public static PushJobTypeEnum valueByType(final Integer status) {
		return map.get(status);
	}

	static {
		final PushJobTypeEnum[] es = values();
		for (final PushJobTypeEnum v : es) {
			map.put(v.getType(), v);
		}
	}
}
