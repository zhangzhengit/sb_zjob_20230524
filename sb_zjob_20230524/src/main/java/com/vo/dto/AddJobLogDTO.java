package com.vo.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.vo.entity.JobLogStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * client推送job执行日志的对象
 *
 * @author zhangzhen
 * @date 2023年5月25日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AddJobLogDTO {

	@NotEmpty(message = "host不能为空")
	private String host;

	@NotNull(message = "port不能为空")
	private String port;

	@NotEmpty(message = "jobId不能为空")
	private String jobId;

	@NotEmpty(message = "jobName不能为空")
	private String jobName;

	@NotEmpty(message = "jobLog不能为空")
	private String jobLog;

	@NotEmpty(message = "description不能为空")
	private String description;

	@NotNull(message = "executionTime不能为空")
	private Date executionTime;

	@NotNull(message = "endTime不能为空")
	private Date endTime;

	/**
	 * 此值可为空，因为不一定出现异常
	 */
	private Date exceptionTime;

	/**
	 * @see JobLogStatusEnum
	 */
	@NotNull(message = "status不能为空")
	private Integer status;

}
