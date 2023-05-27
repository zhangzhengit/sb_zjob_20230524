package com.vo.entity;

import java.util.Date;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年5月27日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class JobResultEntity extends JobEntity {

	/**
	 * 在线的host，多个
	 */
	private List<String> onlineHostList = Lists.newArrayList();

	/**
	 * 最近一次心跳时间
	 */
	private Date heartbeatTime;

	/**
	 * 根据修改时间和推送时间算出来的提示信息，前者晚于后者，则提示【有未推送的修改】
	 */
	private String updatePushRemark;
}
