package com.vo.dto;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.vo.enums.StatusEnum;

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
public class PushJobDTO {

	@NotNull(message = "id不能为空")
	private Integer id;

	private String name;

	private String cron;

	/**
	 * @see StatusEnum
	 */
	private Integer status;

}
