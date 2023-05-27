package com.vo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vo.core.ZLog2;
import com.votool.cacheredis.EnableZRedisCache;

/**
 * zjob-server端，提供web界面，可以查看/编辑/配置/推送job、查看job日志等
 *
 * @author zhangzhen
 * @date 2023年5月24日
 *
 */
@EnableZRedisCache
@SpringBootApplication
public class SbZjob20230524Application {

	private final static ZLog2 LOG = ZLog2.getInstance();

	public static void main(final String[] args) {
		SpringApplication.run(SbZjob20230524Application.class, args);

		LOG.info("sb_zjob_20230524启动成功");

	}

}
