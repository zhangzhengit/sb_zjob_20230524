package com.vo.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态 1启用- 2- 停用
 *
 * @author zhangzhen
 * @date 2022年11月15日
 *
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

	QI_YONG(1),

	TING_YONG(2),;

	private Integer status;

	private static Map<Integer, StatusEnum> map = new HashMap<>();

	public static StatusEnum valueByStatus(final Integer status) {
		return map.get(status);
	}

	static {
		final StatusEnum[] es = values();
		for (final StatusEnum v : es) {
			map.put(v.getStatus(), v);
		}
	}

}
