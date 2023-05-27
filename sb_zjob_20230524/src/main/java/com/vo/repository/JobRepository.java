package com.vo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vo.entity.JobEntity;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年5月24日
 *
 */
public interface JobRepository extends JpaRepository<JobEntity, Integer>{

	List<JobEntity> findByName(String name);

	List<JobEntity> findByNameIn(List<String> nameList);

}
