package com.vo.service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vo.api.ClientRegDTO;
import com.vo.entity.ClientOnlineEntity;
import com.vo.entity.JobEntity;
import com.vo.repository.ClientOnlineRepository;
import com.vo.repository.JobRepository;

import cn.hutool.core.collection.CollUtil;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年5月27日
 *
 */
@Service
public class ClientOnlineService implements InitializingBean {

	public static final int SECOND = 3 * 5 + 1;
	final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private ClientOnlineRepository clientOnlineRepository;

	@Transactional(rollbackFor = Exception.class)
	public void heartbeat(final ClientRegDTO clientRegDTO) {
		this.online(clientRegDTO);
	}

	public List<ClientOnlineEntity> findAll() {
		return this.clientOnlineRepository.findAll();
	}

	@Transactional(rollbackFor = Exception.class)
	public void online(final ClientRegDTO clientRegDTO) {

		final List<String> jobNameList = clientRegDTO.getJobNameList();
		if (CollUtil.isEmpty(jobNameList)) {
			return;
		}

		final List<JobEntity> jeList = this.jobRepository.findByNameIn(jobNameList);

		if (CollUtil.isEmpty(jeList)) {
			return;
		}

		for (final JobEntity jobEntity : jeList) {

			final List<ClientOnlineEntity> colist = this.clientOnlineRepository
					.findByHostAndPortAndJobId(clientRegDTO.getHost(), clientRegDTO.getPort(), jobEntity.getId());
			if (CollUtil.isEmpty(colist)) {
				final ClientOnlineEntity entity = new ClientOnlineEntity();
				entity.setPort(clientRegDTO.getPort());
				entity.setJobId(jobEntity.getId());
				entity.setHost(clientRegDTO.getHost());
				entity.setHeartbeatTime(clientRegDTO.getHeartbeatTime());
				final ClientOnlineEntity save = this.clientOnlineRepository.save(entity);

				this.offline0(save);
			} else {
				final ClientOnlineEntity clientOnlineEntity = colist.get(0);
				clientOnlineEntity.setHeartbeatTime(clientRegDTO.getHeartbeatTime());
				final ClientOnlineEntity save = this.clientOnlineRepository.save(clientOnlineEntity);

				this.offline0(save);
			}

		}

	}

	private void offline0(final ClientOnlineEntity save) {
		this.scheduledExecutorService.schedule(new Runnable() {
			@Override
			public void run() {
				ClientOnlineService.this.clientOnlineRepository.deleteById(save.getId());
			}
		}, SECOND, TimeUnit.SECONDS);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		final List<ClientOnlineEntity> cel = this.clientOnlineRepository.findAll();
		for (final ClientOnlineEntity clientOnlineEntity : cel) {
			this.offline0(clientOnlineEntity);
		}
	}


}
