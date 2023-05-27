package com.vo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * client中job执行的日志信息
 *
 * @author zhangzhen
 * @date 2023年5月25日
 *
 */
@Data
@Entity(name = "job_log")
public class JobLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * job表的id，对应一个job表的数据
	 */
	private Integer jobId;

	/**
	 * job表的id，同时对应@ZJobTask.name()属性值
	 */
	private String jobName;

	/**
	 * @ZJobTask标记的方法的执行的日志
	 */
	private String jobLog;

	/**
	 * @ZJobTask.id()属性值
	 */
	private String jobtaskId;

	/**
	 * @ZJobTask.description()属性值
	 */
	private String jobDescription;

	/**
	 * client的host
	 */
	private String jobHost;

	/**
	 * client的port
	 */
	private String jobPort;

	/**
	 * client的@ZJobTask标记的方法的开始执行时间
	 */
	private Date executionTime;

	/**
	 * client的@ZJobTask标记的方法的执行结束时间
	 */
	private Date endTime;

	/**
	 * client的@ZJobTask标记的方法的异常时间
	 */
	private Date exceptionTime;

	/**
	 * @see JobLogStatusEnum
	 */
	private Integer status;

	private Date createTime;

}
