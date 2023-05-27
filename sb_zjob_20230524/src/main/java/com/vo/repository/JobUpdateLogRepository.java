package com.vo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vo.entity.JobUpdateLogEntity;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年11月24日
 *
 */
public interface JobUpdateLogRepository extends JpaRepository<JobUpdateLogEntity, Integer> {

	List<JobUpdateLogEntity> findByJobId(Integer jobId);

}
