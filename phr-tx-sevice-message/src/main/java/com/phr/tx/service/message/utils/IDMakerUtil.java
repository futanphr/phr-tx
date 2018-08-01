package com.phr.tx.service.message.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhi
 * @time 2018年3月30日上午11:24:53
 * @email zhi19861117@126.com
 * @version 1.0
 * @类介绍 id 生成工具
 */
public class IDMakerUtil {
	private static Logger logger = LoggerFactory.getLogger(IDMakerUtil.class);
	//
	//
	//
//	private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();

//	private static KeyGenerator defaultGenerator;
//	private static String sharingJdbcKey = "SHARING_JDBC_KEY";

	private static IdWorker idWorker;
	static {
		try {
			idWorker = new IdWorker();
		} catch (Exception e) {
			logger.error("=================================================id生成组建报错=============================");
		}
//		init();
//		put();
//		Long workerId = RedisUtils.incr(sharingJdbcKey);
//		DefaultKeyGenerator.setWorkerId(workerId);
//		defaultGenerator = KeyGeneratorFactory.createKeyGenerator(DefaultKeyGenerator.class);
	}

	//
//	private static  void init() {
//
//		try {
//			while (queue.size() < 200) {
//				int l = getRedisInt();
//				queue.offer(l);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	//
//	private static final String dateStyle = "yyMMdd";
//	private static final String dateStyle2 = "yyMMddHHmmss";

	public static Long getIdNum(Long namespace) {
//		return poll(namespace);

		return idWorker.nextId();

//		return defaultGenerator.generateKey().longValue();
	}

//	public static Long poll(long namespace) {
//		Date date = new Date();
//		String dateStr = DateUtil.DateToString(date, dateStyle2);
//		try {
//			Integer l = queue.poll();
//			return Long.parseLong(new StringBuffer(String.valueOf(namespace)).append(dateStr).append(l).toString());
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return poll(namespace);
//		}
//
//	}
//
//
//	private static void put() {
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				while (true) {
//					if (queue.size() < 100) {
//						init();
//					}
//				}
//
//			}
//		}).start();
//	}
//
//	private static final String orderNoCounterKey = "id_make_count_key:";
//
//	private static Integer getRedisInt() {
//		StringBuffer key = new StringBuffer(orderNoCounterKey).append(DateUtil.DateToString(new Date(), dateStyle));
//		if (RedisClusterUtils.exists(key.toString())) {
//			// 若存在，则进行累加
//			Long l = RedisClusterUtils.incr(key.toString());
//			if (l > 9999) {
//				l = 1000L;
//				RedisClusterUtils.setex(key.toString(), String.valueOf(1000), 86400);
//			}
//			return l.intValue();
//		} else {
//			// 若该键不存在，则重新开始计算。并将其存储到redis中，设置过期时间为1天
//			RedisClusterUtils.incr(key.toString(), 1000);
//			return 1000;
//		}
//	}

}
