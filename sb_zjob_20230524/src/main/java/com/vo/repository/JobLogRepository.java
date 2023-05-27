package com.vo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vo.entity.JobLogEntity;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年5月25日
 *
 */
public interface JobLogRepository extends JpaRepository<JobLogEntity, Integer>, JpaSpecificationExecutor<JobLogEntity> {

	List<JobLogEntity> findByJobId(Integer jobId);

}
