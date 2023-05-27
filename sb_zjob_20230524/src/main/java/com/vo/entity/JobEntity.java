package com.vo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.vo.enums.StatusEnum;

import lombok.Data;
/**
 *
 *
 * @author zhangzhen
 * @date 2023年5月24日
 *
 */
@Data
@Entity(name = "job")
public class JobEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String cron;

	/**
	 * @see StatusEnum
	 */
	private Integer status;

	private Date createTime;
	private Date updateTime;
	private Date pushTime;

	private String remark;
}
