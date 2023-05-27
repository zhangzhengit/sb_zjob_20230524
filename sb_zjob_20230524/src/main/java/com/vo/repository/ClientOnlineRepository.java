package com.vo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vo.entity.ClientOnlineEntity;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年5月27日
 *
 */
public interface ClientOnlineRepository extends JpaRepository<ClientOnlineEntity, Integer> {

	List<ClientOnlineEntity> findByHostAndPortAndJobId(String host,Integer port,Integer jobId);
	List<ClientOnlineEntity> findByJobId(Integer jobId);
}
