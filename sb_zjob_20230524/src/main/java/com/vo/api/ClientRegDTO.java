package com.vo.api;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册client信息到server
 *
 * @author zhangzhen
 * @date 2023年5月27日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ClientRegDTO implements Serializable {

	@NotNull(message = "port不能为空")
	private Integer port;

	@NotEmpty(message = "host不能为空")
	private String host;

	@NotEmpty(message = "jobNameList不能为空")
	private java.util.List<String> jobNameList;

	private Date heartbeatTime;

}
