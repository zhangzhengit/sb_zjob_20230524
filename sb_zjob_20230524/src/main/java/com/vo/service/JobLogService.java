package com.vo.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vo.dto.AddJobLogDTO;
import com.vo.entity.JobEntity;
import com.vo.entity.JobLogEntity;
import com.vo.entity.JobLogStatusEnum;
import com.vo.entity.JobResultEntity;
import com.vo.repository.JobLogRepository;
import com.vo.repository.JobRepository;
import com.votool.common.CR;

import cn.hutool.core.collection.CollUtil;

/**
 * job表对应的client的@ZJobTask标记的方法的执行日志
 *
 * @author zhangzhen
 * @date 2023年5月25日
 *
 */
@Service
public class JobLogService {

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private JobLogRepository jobLogRepository;

	@Transactional(rollbackFor = Exception.class)
	public synchronized CR<Object> add(final AddJobLogDTO addJobDTO) {

		final String jobName = addJobDTO.getJobName();
		final List<JobEntity> jelist = this.jobRepository.findByName(jobName);
		if (CollUtil.isEmpty(jelist)) {
			return CR.error("jobName不存在");
		}

		final JobLogStatusEnum en = JobLogStatusEnum.valueByStatus(addJobDTO.getStatus());
		if (Objects.isNull(en)) {
			return CR.error("status错误");
		}

		final JobEntity jobEntity = jelist.get(0);

		final JobLogEntity e = new JobLogEntity();
		e.setJobId(jobEntity.getId());
		e.setJobtaskId(addJobDTO.getJobId());
		e.setJobName(addJobDTO.getJobName());
		e.setJobLog(addJobDTO.getJobLog());
		e.setJobDescription(addJobDTO.getDescription());
		e.setJobHost(addJobDTO.getHost());
		e.setJobPort(addJobDTO.getPort());
		e.setExecutionTime(addJobDTO.getExecutionTime());
		e.setEndTime(addJobDTO.getEndTime());
		e.setExceptionTime(addJobDTO.getExceptionTime());
		e.setStatus(addJobDTO.getStatus());
		e.setCreateTime(new Date());

		this.jobLogRepository.save(e);

		return CR.ok();
	}

}
