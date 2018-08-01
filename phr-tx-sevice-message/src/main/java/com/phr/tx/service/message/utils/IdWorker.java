package com.phr.tx.service.message.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdWorker {

	private static Logger logger = LoggerFactory.getLogger(IdWorker.class);

	// ==============================Fields===========================================
	/** 开始时间截 (2018-01-01) */
	private final long twepoch = 1514736000000L;

	/** 工作机器ID(0~31) */
	private long workerId;

	/** 毫秒内序列(0~4095) */
	private long sequence = 0L;

	/** 上次生成ID的时间截 */
	private long lastTimestamp = -1L;

	// ==============================Constructors=====================================
	/**
	 * 构造函数
	 * 
	 * @param workerId 工作ID (0~31)
	 * @throws Exception
	 */
	public IdWorker() throws Exception {

		// 获取当前服务器的机器id start
		int index = 0;
		String ip = getLocalHostLANAddress().getHostAddress();
		try {
			String cluster = System.getProperty("cluster");
			String[] strings = cluster.split(",");

			for (int j = 0; j < strings.length; j++) {
				if (ip.equals(strings[j])) {
					index = j + 1;
				}
			}

			logger.warn("method[IdWorker] 获取当前服务器IP:{},服务器ip列表{},机器码：{}", ip, cluster, index);
			// 获取当前服务器的机器id end
		} catch (Exception e) {
			index = 0;
		}
		this.workerId = index;
	}

	// ==============================Methods==========================================
	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * 
	 * @return SnowflakeId
	 */
	public synchronized long nextId() {

		long timestamp = timeGen();

		// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		StringBuilder builder = new StringBuilder("");

		// 如果是同一时间生成的，则进行毫秒内序列
		if (lastTimestamp == timestamp) {
			sequence = sequence + 1;
			if (sequence > 999) { // 毫秒内序列溢出
				sequence = 0;
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else { // 时间戳改变，毫秒内序列重置
			sequence = 0L;
		}

		// 上次生成ID的时间截
		lastTimestamp = timestamp;

		builder.append(workerId).append(timestamp - twepoch);
		if (sequence < 10) {
			builder.append("00").append(sequence);
		} else if (sequence < 100) {
			builder.append("0").append(sequence);
		} else if (sequence < 1000) {
			builder.append(sequence);
		}
		return Long.parseLong(builder.toString());
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 * 
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 返回以毫秒为单位的当前时间
	 * 
	 * @return 当前时间(毫秒)
	 */
	protected long timeGen() {
		return System.currentTimeMillis();
	}

	public static InetAddress getLocalHostLANAddress() throws Exception {
		try {
			InetAddress candidateAddress = null;
			// 遍历所有的网络接口
			for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// 在所有的接口下再遍历IP
				for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
						if (inetAddr.isSiteLocalAddress()) {
							// 如果是site-local地址，就是它了
							return inetAddr;
						} else if (candidateAddress == null) {
							// site-local类型的地址未被发现，先记录候选地址
							candidateAddress = inetAddr;
						}
					}
				}
			}
			if (candidateAddress != null) {
				return candidateAddress;
			}
			// 如果没有发现 non-loopback地址.只能用最次选的方案
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			return jdkSuppliedAddress;
		} catch (Exception e) {
			logger.error("method[getLocalHostLANAddress] id生成器异常", e);
		}
		return null;
	}
}