package com.vo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.vo.enums.PushJobTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 立即执行job
 *
 * @author zhangzhen
 * @date 2023年5月27日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ExecuteImmediatelJobDTO {

	@NotNull(message = "id不能为空")
	@Min(value = 1, message = "id最小为1")
	private Integer id;

	/**
	 * @see PushJobTypeEnum
	 */
	@NotNull(message = "type不能为空")
	private Integer type;

	/**
	 * 推送的json内容
	 */
	private String content;
}
