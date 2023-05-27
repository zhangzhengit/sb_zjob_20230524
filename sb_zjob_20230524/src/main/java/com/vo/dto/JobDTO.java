package com.vo.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
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
 * @date 2023年5月24日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class JobDTO {

	@NotNull(message = "id不能为空")
	private Integer id;

	@NotEmpty(message = "name不能为空")
	private String name;

	@NotEmpty(message = "cron不能为空")
	private String cron;

	/**
	 * @see StatusEnum
	 */
	@NotNull(message = "status不能为空")
	private Integer status;

	private Date createTime;
}
