package com.vo.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.vo.core.ZLog2;
import com.vo.dto.AddJobDTO;
import com.vo.dto.ExecuteImmediatelJobDTO;
import com.vo.dto.JobDTO;
import com.vo.dto.UpdateJobDTO;
import com.vo.entity.ClientOnlineEntity;
import com.vo.entity.JobEntity;
import com.vo.entity.JobUpdateLogEntity;
import com.vo.enums.ClientAPIEnum;
import com.vo.enums.PushJobTypeEnum;
import com.vo.enums.StatusEnum;
import com.vo.repository.ClientOnlineRepository;
import com.vo.repository.JobRepository;
import com.vo.repository.JobUpdateLogRepository;
import com.votool.common.CR;
import com.votool.ze.AbstractZETask;
import com.votool.ze.ZE;
import com.votool.ze.ZES;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年5月24日
 *
 */
@Service
public class JobService {

	private final static ZLog2 LOG = ZLog2.getInstance();

	@Autowired
	private ClientOnlineRepository clientOnlineRepository;
	@Autowired
	private ClientOnlineService clientOnlineService;
	@Autowired
	private JobUpdateLogService jobUpdateLogService;

	@Autowired
	private JobUpdateLogRepository jobUpdateLogRepository;
	@Autowired
	private JobRepository jobRepository;


	@Transactional(rollbackFor = Exception.class)
	public CR<Object> add(final AddJobDTO addJobDTO) {

		final String name = addJobDTO.getName();

		final List<JobEntity> l = this.jobRepository.findByName(name);
		if (CollUtil.isNotEmpty(l)) {
			return CR.error("name已存在");
		}

		final boolean validExpression = CronExpression.isValidExpression(addJobDTO.getCron());
		if (!validExpression) {
			return CR.error("cron错误");
		}

		final JobEntity jobEntity = new JobEntity();
		jobEntity.setName(addJobDTO.getName());
		jobEntity.setStatus(StatusEnum.TING_YONG.getStatus());
		jobEntity.setCron(addJobDTO.getCron());
		jobEntity.setCreateTime(new Date());
		jobEntity.setRemark(addJobDTO.getRemark());

		this.jobRepository.save(jobEntity);

		return CR.ok();
	}


	@Transactional(rollbackFor = Exception.class)
	public CR update(final UpdateJobDTO updateJobDTO) {

		final Optional<JobEntity> o = this.jobRepository.findById(updateJobDTO.getId());
		if (!o.isPresent()) {
			return CR.error("job不存在");
		}

		final JobEntity jobEntity = o.get();
		final String old = "原值=" + String.valueOf(jobEntity);

		final JobEntity oldValue = new JobEntity();
		BeanUtil.copyProperties(jobEntity, oldValue);

		if (StrUtil.isNotBlank(updateJobDTO.getName())) {
			jobEntity.setName(updateJobDTO.getName());
		}

		if (StrUtil.isNotBlank(updateJobDTO.getCron())) {
			final boolean validExpression = CronExpression.isValidExpression(updateJobDTO.getCron());
			if (!validExpression) {
				return CR.error("cron错误");
			}

			jobEntity.setCron(updateJobDTO.getCron());
		}

		if (Objects.nonNull(updateJobDTO.getStatus())) {
			final StatusEnum en = StatusEnum.valueByStatus(updateJobDTO.getStatus());
			if (Objects.isNull(en)) {
				return CR.error("status错误，此值不存在,status=" + updateJobDTO.getStatus());
			}
			jobEntity.setStatus(updateJobDTO.getStatus());
		}

		if (StrUtil.isNotBlank(updateJobDTO.getRemark())) {
			jobEntity.setRemark(updateJobDTO.getRemark());
		}
		jobEntity.setUpdateTime(new Date());

		this.jobRepository.save(jobEntity);

		final String log = old + " 修改为新值=" + updateJobDTO;
		final JobUpdateLogEntity logEntity  = new JobUpdateLogEntity();
		logEntity.setJobId(jobEntity.getId());
		logEntity.setJobName(jobEntity.getName());
		logEntity.setJobLog(log);
		logEntity.setOldValue(String.valueOf(oldValue));
		logEntity.setNewValue(String.valueOf(jobEntity));
		logEntity.setCreateTime(new Date());
		this.jobUpdateLogRepository.save(logEntity);

		return CR.ok();
	}


	@Transactional(rollbackFor = Exception.class)
	public CR<Object> executeImmediatel(final ExecuteImmediatelJobDTO executeImmediatelDTO) {

		final Integer id = executeImmediatelDTO.getId();
		final Optional<JobEntity> o = this.jobRepository.findById(id);
		if(!o.isPresent()) {
			return CR.error("job不存在");
		}

		final List<ClientOnlineEntity> coeList = this.clientOnlineRepository.findByJobId(id);
		if (CollUtil.isEmpty(coeList)) {
			return CR.error("job未注册（未上线）");
		}

		final Set<AbstractZETask<String>> zetSet = coeList.stream().map(coe -> {
			return new AbstractZETask<String>() {
				@Override
				public String call() {
					final String host = coe.getHost() + ":" + coe.getPort();
					return JobService.this.push(executeImmediatelDTO, o.get(), host);
				}
			};
		}).collect(Collectors.toSet());


		final ArrayList<AbstractZETask<String>> azeTList = Lists.newArrayList(zetSet);
		final ZE ze = ZES.newZE(azeTList.size());
		final List<String> pushJobCRList = ze.submitInQueueAndGet(azeTList);

		@SuppressWarnings("restriction")
		final String lineSeparator = java.security.AccessController.doPrivileged(
	            new sun.security.action.GetPropertyAction("line.separator"));

		final String r = pushJobCRList.stream().collect(Collectors.joining(lineSeparator));

		return CR.okMessage(r);

	}

	String push(final ExecuteImmediatelJobDTO executeImmediatelJobDTO, final JobEntity jobEntity,
			final String host) {

		final String name = jobEntity.getName();

		final PushJobTypeEnum pushJobTypeEnum = PushJobTypeEnum.valueByType(executeImmediatelJobDTO.getType());
		switch (pushJobTypeEnum) {
		case EXECUTE_IMMEDIATEL:
			final JobDTO jobDTOEXECUTE_IMMEDIATEL = new JobDTO();
			jobDTOEXECUTE_IMMEDIATEL.setName(jobEntity.getName());
			executeImmediatelJobDTO.setContent(JSON.toJSONString(jobDTOEXECUTE_IMMEDIATEL));
			break;

		case UPDATE_CONFIGURATION:
			// XXX 改为记录点击按钮时的值，与此一致才推送，否则提醒
			final JobDTO jobDTOUPDATE_CONFIGURATION = new JobDTO();
			BeanUtil.copyProperties(jobEntity, jobDTOUPDATE_CONFIGURATION);
			executeImmediatelJobDTO.setContent(JSON.toJSONString(jobDTOUPDATE_CONFIGURATION));
			jobEntity.setPushTime(new Date());
			this.jobRepository.save(jobEntity);

			break;

		default:
			break;
		}

		final String json = JSON.toJSONString(executeImmediatelJobDTO);
		final String url = ClientAPIEnum.EXECUTE_IMMEDIATEL.getUrl();

		try {

			LOG.info("开始推送[{}]消息,host={},name={}", pushJobTypeEnum.getDescription(), host, name);
			final String body = HttpRequest.post(host + url).body(json).execute().body();

			LOG.info("推送[{}行]消息结果,host={},name={},cr={}", pushJobTypeEnum.getDescription(), host, name, body);

			return "推送[" + pushJobTypeEnum.getDescription() + "]消息成功,host={" + host + "},name={" + name + "}";

		} catch (final Exception e) {
			final StringWriter stringWriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stringWriter, true));
			final String message = stringWriter.toString();
			LOG.error("推送[{}]消息异常,host={},name={},cr={}", pushJobTypeEnum.getDescription(), host, name, message);
			return "推送[" + pushJobTypeEnum.getDescription() + "]消息异常,host={" + host + "},name={" + name + "},cr={"
					+ message + "}";
		}

	}
}
