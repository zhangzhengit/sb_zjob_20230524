package com.vo.entity;

import java.util.HashMap;
import java.util.Map;

import com.vo.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 执行状态 1-正常 2-失败
 *
 * @author zhangzhen
 * @date 2023年5月27日
 *
 */
@Getter
@AllArgsConstructor
public enum JobLogStatusEnum {

	SUCCESS(1),


	EXCEPTION(2),
	;

	private Integer status;


	private static Map<Integer, JobLogStatusEnum> map = new HashMap<>();


	public static JobLogStatusEnum valueByStatus(final Integer status) {
		return map.get(status);
	}

	static {
		final JobLogStatusEnum[] es = values();
		for (final JobLogStatusEnum v : es) {
			map.put(v.getStatus(), v);
		}
	}
}
