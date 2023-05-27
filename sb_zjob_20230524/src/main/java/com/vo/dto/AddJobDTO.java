package com.vo.dto;

import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import com.vo.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年5月25日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AddJobDTO {


	@NotEmpty(message = "name不能为空")
	private String name;

	@NotEmpty(message = "cron不能为空")
	private String cron;


	/**
	 * @see StatusEnum
	 */
//	@NotNull(message = "status不能为空")
	private Integer status;

	private String remark;

}
