package com.vo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 *
 * job表每次update的日志记录
 *
 * @author zhangzhen
 * @date 2022年11月24日
 *
 */
@Data
@Entity(name = "job_update_log")
public class JobUpdateLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer jobId;
	private String jobName;
	private String jobLog;
	private String oldValue;
	private String newValue;

	private Date createTime;

}
