package com.vo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vo.repository.JobUpdateLogRepository;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年11月24日
 *
 */
@Service
public class JobUpdateLogService {

	@Autowired
	private JobUpdateLogRepository jobUpdateLogRepository;

}
