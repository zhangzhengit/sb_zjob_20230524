package com.vo.api;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vo.core.ZLog2;
import com.vo.dto.AddJobDTO;
import com.vo.dto.AddJobLogDTO;
import com.vo.dto.ExecuteImmediatelJobDTO;
import com.vo.dto.UpdateJobDTO;
import com.vo.entity.ClientOnlineEntity;
import com.vo.entity.JobEntity;
import com.vo.entity.JobLogEntity;
import com.vo.entity.JobResultEntity;
import com.vo.entity.JobUpdateLogEntity;
import com.vo.repository.JobLogRepository;
import com.vo.repository.JobRepository;
import com.vo.repository.JobUpdateLogRepository;
import com.vo.service.ClientOnlineService;
import com.vo.service.JobLogService;
import com.vo.service.JobService;
import com.votool.common.CR;
import com.votool.page.ZMR;
import com.votool.page.ZPR;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

/**
 *
 *	api
 * @author zhangzhen
 * @date 2023年5月24日
 *
 */
@Controller
public class API {


	private static final long DATE = 123456L;

	// FIXME 2023年5月27日 上午6:32:55 zhanghen: TODO 支持 @ZJobTask的方法传参执行


	private final static ZLog2 LOG = ZLog2.getInstance();

	@Autowired
	private ClientOnlineService clientOnlineService;
	@Autowired
	private JobLogRepository jobLogRepository;
	@Autowired
	private JobLogService jobLogService;
	@Autowired
	private JobUpdateLogRepository logRepository;
	@Autowired
	private JobService jobService;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private ClientOnlineService onlineService;

	@GetMapping
	public String index(final Model model) {
		final List<JobEntity> jobList = this.jobRepository.findAll();

		final List<JobResultEntity> jreList = jobList.stream().map(e -> {
			final JobResultEntity r = new JobResultEntity();
			BeanUtil.copyProperties(e, r);
			return r;
		}).collect(Collectors.toList());

		model.addAttribute("jobList", jreList);


		// 2 onlineService
		final List<ClientOnlineEntity> clientOnlineList = this.onlineService.findAll();
		for (final JobResultEntity jobResultEntity : jreList) {
			final Optional<ClientOnlineEntity> oo = clientOnlineList.stream()
					.filter(co -> co.getJobId().equals(jobResultEntity.getId()))
					.max(Comparator.comparing(ClientOnlineEntity::getHeartbeatTime));
			if (oo.isPresent()) {
				jobResultEntity.setHeartbeatTime(oo.get().getHeartbeatTime());
			}

			 final List<ClientOnlineEntity> on = clientOnlineList.stream()
				.filter(co -> co.getJobId().equals(jobResultEntity.getId()))
				.collect(Collectors.toList());

			 final List<String> h = on.stream().map(co -> co.getHost() + ":" + co.getPort()).collect(Collectors.toList());
			 jobResultEntity.setOnlineHostList(h);


			 final Date updateTime = Objects.isNull(jobResultEntity.getUpdateTime()) ? new Date(DATE) : jobResultEntity.getUpdateTime();
			 final Date pushTime = Objects.isNull(jobResultEntity.getPushTime()) ? new Date(DATE) : jobResultEntity.getPushTime();
			 final String upr = updateTime.getTime() > pushTime.getTime() ? "有最新的修改还未推送" : "";
			 jobResultEntity.setUpdatePushRemark(upr);

		}


		// 1
//		final Collection<Entry<String, ClientRegDTO>> cs = this.clientInfo.all();
//		if (CollUtil.isEmpty(cs)) {
//			return "index";
//		}
//
//		final List<ClientRegDTO> crList = cs.stream().map(e -> e.getValue()).collect(Collectors.toList());
//		for (final JobResultEntity jobResultEntity : jreList) {
//			final Optional<ClientRegDTO> oo = crList.stream()
//					.filter(cr -> Objects.nonNull(cr.getHeartbeatTime()))
//					.filter(cr -> cr.getJobNameList().contains(jobResultEntity.getName()))
//					.max(Comparator.comparing(ClientRegDTO::getHeartbeatTime));
//			if (oo.isPresent()) {
//				jobResultEntity.setHeartbeatTime(oo.get().getHeartbeatTime());
//				final ClientRegDTO clientRegDTO = oo.get();
//				jobResultEntity.getOnlineHostList().add(clientRegDTO.getHost() + ":" + clientRegDTO.getPort());
//			}
//		}

		return "index";
	}

	/**
	 * 获取job配置信息
	 *
	 * @param name
	 * @return
	 */
	@GetMapping(value = "/job")
	@ResponseBody
	public CR job(@RequestParam final String name) {
		final List<JobEntity> list = this.jobRepository.findByName(name);
		if (CollUtil.isEmpty(list)) {
			return CR.ok();
		}

		final JobEntity j = list.get(0);

		return CR.ok(j);
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public CR update(@RequestBody @Validated final UpdateJobDTO updateJobDTO) {

		LOG.info("开始update job信息,id={},updateJobDTO={}", updateJobDTO.getId(), updateJobDTO);

		final CR cr = this.jobService.update(updateJobDTO);

		return cr;
	}

	@GetMapping(value = "/detail")
	public String detail(
			final Model model,
			@RequestParam @NotNull @Min(value = 1, message = "id最小为1") final Integer id) {

		final Optional<JobEntity> jeo = this.jobRepository.findById(id);
		if (!jeo.isPresent()) {
			return "update";
		}

		model.addAttribute("job", jeo.get());


		final List<JobUpdateLogEntity> logList = this.logRepository.findByJobId(jeo.get().getId());
		logList.sort(Comparator.comparing(JobUpdateLogEntity::getId).reversed());
		model.addAttribute("logList", logList);

		return "update";
	}


	@GetMapping(value = "/add")
	public String add(
			final Model model) {

		return "add";
	}

	@PostMapping(value = "/add")
	@ResponseBody
	public CR addJob(
			final Model model, @RequestBody @Validated final AddJobDTO addJobDTO) {

		final CR<Object> cr = this.jobService.add(addJobDTO);

		return cr;
	}


	@GetMapping(value = "/logDetail")
	public String logDetail(
			final Model model,
			@RequestParam @NotNull @Min(value = 1, message = "id最小为1") final Integer id) {
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "API.logDetail()");

		final Optional<JobLogEntity> jeo = this.jobLogRepository.findById(id);
		if (!jeo.isPresent()) {
			return "logDetail";
		}

		model.addAttribute("jobLog", jeo.get());

		return "logDetail";
	}

	@GetMapping(value = "/log")
	public String log(final Model model,
			@RequestParam @NotNull @Min(value = 1, message = "id最小为1") final Integer id,
			@RequestParam(defaultValue = "")  final String keyword,
			@RequestParam(required = false, defaultValue = "10") final Integer ps,
			@RequestParam(required = false, defaultValue = "1") final Integer pn) {

		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "API.indexPage()");

		model.addAttribute("keyword", keyword);

		final Optional<JobEntity> jo = this.jobRepository.findById(id);
		if (!jo.isPresent()) {
			return "log";
		}
		model.addAttribute("job", jo.get());


		final Specification<JobLogEntity> specification = new Specification<JobLogEntity>() {

			@Override
			public Predicate toPredicate(final Root<JobLogEntity> root, final CriteriaQuery<?> query,
					final CriteriaBuilder cb) {
				final Predicate jobId = cb.equal(root.get("jobId"), id);
				final Predicate jobLog = cb.like(root.get("jobLog"), "%" + keyword + "%");
				final Predicate jobName = cb.like(root.get("jobName"), "%" + keyword + "%");
				final Predicate jobDescription = cb.like(root.get("jobDescription"), "%" + keyword + "%");
				final Predicate jobHost = cb.like(root.get("jobHost"), "%" + keyword + "%");
				final Predicate jobPort = cb.like(root.get("jobPort"), "%" + keyword + "%");
				final Predicate jobtaskId = cb.like(root.get("jobtaskId"), "%" + keyword + "%");

				// XXX 匹配时间？

				final Predicate like = cb.or(cb.or(jobLog), cb.or(jobName), cb.or(jobDescription), cb.or(jobHost),
						cb.or(jobPort), cb.or(jobtaskId));

				final CriteriaQuery<?> where = query.where(jobId, like);
				return where.getRestriction();
			}
		};;


		final int p1 = pn <= 0 ? 1 : pn - 1;
		final PageRequest pageRequest = PageRequest.of(p1, ps, Sort.by("id").descending());

		final ExampleMatcher matcher = ExampleMatcher.matching();

		final JobLogEntity entity = new JobLogEntity();
		entity.setJobId(id);

		final Page<JobLogEntity> page = this.jobLogRepository.findAll(specification, pageRequest);
		model.addAttribute("list", page.toList());
		model.addAttribute("page", page);

		ZMR.init(model, new ZPR<>(page));

		return "log";
	}

	/**
	 * 接收client的job执行的日志
	 *
	 * @param addJobDTO
	 * @return
	 *
	 */
	@PostMapping(value = "/receivelog")
	@ResponseBody
	public CR receivlog(
			 @RequestBody @Validated final AddJobLogDTO addJobDTO) {

		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "r");

		final CR<Object> cr = this.jobLogService.add(addJobDTO);

		return cr;
	}


	/**
	 * 立即执行job/立即更新job最新信息
	 *
	 * @param executeImmediatelDTO
	 * @return
	 *
	 */
	@PostMapping(value = "/executeImmediatel")
	@ResponseBody
	public CR<Object> executeImmediatel(@RequestBody @Validated final ExecuteImmediatelJobDTO executeImmediatelDTO) {

		LOG.info("executeImmediatel执行开始，executeImmediatelDTO={}", executeImmediatelDTO);

		final CR<Object> cr = this.jobService.executeImmediatel(executeImmediatelDTO);
		LOG.info("executeImmediatel执行结束，cr={}", cr);
		return cr;
	}

	/**
	 * 接收client的注册信息
	 *
	 * @param executeImmediatelDTO
	 * @return
	 *
	 */
	@PostMapping(value = "/register")
	@ResponseBody
	public CR register(
			@RequestBody @Validated final ClientRegDTO clientRegDTO) {

		LOG.info("收到client注册信息,dto={}", clientRegDTO);

		this.clientOnlineService.online(clientRegDTO);

		return CR.ok();
	}

	@PostMapping(value = "/heartbeat")
	@ResponseBody
	public CR heartbeat(
			@RequestBody @Validated final ClientRegDTO clientRegDTO) {

//		LOG.info("收到client-heartbeat,dto={}", clientRegDTO);

		this.clientOnlineService.online(clientRegDTO);

		return CR.ok();
	}

}
