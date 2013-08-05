package com.plywet.imeta.utils;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

import com.plywet.imeta.core.log.Log;
import com.plywet.imeta.i18n.GlobalMessageUtil;

/**
 * UUID工具类
 * 
 * @author 潘巍（Peter Pan）
 * @since 2011-4-20 下午08:12:20
 */
public class UUIDUtil {

	static boolean nativeInitialized = false;

	static UUIDGenerator ug;

	static org.safehaus.uuid.EthernetAddress eAddr;

	private static final Log log = Log.getLog(UUIDUtil.class.getName());

	static {
		ug = UUIDGenerator.getInstance();
		if (nativeInitialized) {
			try {
				com.ccg.net.ethernet.EthernetAddress ea = com.ccg.net.ethernet.EthernetAddress
						.getPrimaryAdapter();
				eAddr = new org.safehaus.uuid.EthernetAddress(ea.getBytes());
			} catch (Exception ex) {
				log
						.error(GlobalMessageUtil
								.formatErrorMessage("UUIDUtil.ERROR_0002_GET_MAC_ADDR"));
			} catch (UnsatisfiedLinkError ule) {
				log
						.error(GlobalMessageUtil
								.formatErrorMessage("UUIDUtil.ERROR_0002_GET_MAC_ADDR"));
				nativeInitialized = false;
			}
		}

		// 为运行在集群环境提供支持。用这种方法，运行服务器的MAC地址将被加入环境变量中，
		// 如-DMAC_ADDRESS=00:50:56:C0:00:01
		if (eAddr == null) {
			String macAddr = System.getProperty("MAC_ADDRESS");
			if (macAddr != null) {
				// 在Windows环境下，人们可以通过ipconfig /all得到包含MAC地址的信息。
				// 格式像00-50-56-C0-00-08这样。所以，在创建地址之前替换'-'为':'。
				macAddr = macAddr.replace('-', ':');
				eAddr = new org.safehaus.uuid.EthernetAddress(macAddr);
			}
		}

		if (eAddr == null) {
			// 如果仍不能获得以太网卡地址——创建一个傀儡。
			eAddr = ug.getDummyAddress();
		}

		// 创建一个UUID，确保所以事情运作OK
		UUID olduuId = ug.generateTimeBasedUUID(eAddr);
		if (olduuId == null) {
			log.error(GlobalMessageUtil
					.formatErrorMessage("UUIDUtil.ERROR_0003_GENERATEFAILED"));
		}

	}

	public static String getUUIDAsString() {
		return getUUID().toString();
	}

	public static UUID getUUID() {
		return ug.generateTimeBasedUUID(eAddr);
	}

}
