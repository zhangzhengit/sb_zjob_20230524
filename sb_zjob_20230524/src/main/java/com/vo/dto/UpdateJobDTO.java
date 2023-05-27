package com.vo.dto;

import javax.validation.constraints.Min;
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
 * @date 2023年5月25日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UpdateJobDTO {

	@NotNull(message = "id不能为空")
	@Min(value = 1,message = "id最小为1")
	private Integer id;

//	@NotEmpty(message = "name不能为空")
	private String name;

//	@NotEmpty(message = "cron不能为空")
	private String cron;

	/**
	 * @see StatusEnum
	 */
//	@NotNull(message = "status不能为空")
	private Integer status;

	private String remark;

}
