package com.vo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年5月27日
 *
 */
@Data
@Entity(name = "client_online")
public class ClientOnlineEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer jobId;
	private String host;
	private Integer port;
	private Date heartbeatTime;

}
