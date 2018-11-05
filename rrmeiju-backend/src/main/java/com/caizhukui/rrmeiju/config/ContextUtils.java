package com.caizhukui.rrmeiju.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 通过该类的getBean()静态方法可获取ApplicationContext中的实例
 * 
 * @author caizhukui
 * @date 2017年1月16日
 */
public class ContextUtils {

	private static Logger logger = LoggerFactory.getLogger(ContextUtils.class);

	private static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		synchronized (ContextUtils.class) {
			logger.debug("setApplicationContext, notifyAll");
			ContextUtils.applicationContext = applicationContext;
			ContextUtils.class.notifyAll();
		}
	}

	public static ApplicationContext getApplicationContext() {
		synchronized (ContextUtils.class) {
			while (applicationContext == null) {
				try {
					logger.debug("getApplicationContext, wait...");
					ContextUtils.class.wait(60000);
					if (applicationContext == null) {
						logger.warn("Have been waiting for ApplicationContext to be set for 1 minute", new Exception());
					}
				} catch (InterruptedException ex) {
					logger.debug("getApplicationContext, wait interrupted");
				}
			}
			return applicationContext;
		}
	}
	
	/**
	 * 
	 * 
	 * @param name
	 * @return Object
	 * @author caizhukui
	 * @date 2017年4月11日下午7:05:07
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

}
