package com.plywet.imeta.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射的工具类
 * 
 * @since 1.0 2010-4-1
 * @author 潘巍（Peter Pan）
 * 
 */
public class ReflectionUtils {

	/**
	 * 得到某个对象的公共属性
	 * 
	 * @param owner
	 * @param fieldName
	 * @return 该属性对象
	 * @throws Exception
	 * 
	 */
	public static Object getProperty(Object owner, String fieldName)
			throws Exception {
		Class<?> ownerClass = owner.getClass();
		Field field = ownerClass.getField(fieldName);
		Object property = field.get(owner);
		return property;
	}

	/**
	 * 得到某类的静态公共属性
	 * 
	 * @param className
	 *            类名
	 * @param fieldName
	 *            属性名
	 * @return 该属性对象
	 * @throws Exception
	 */
	public static Object getStaticProperty(String className, String fieldName)
			throws Exception {
		Class<?> ownerClass = Class.forName(className);
		Field field = ownerClass.getField(fieldName);
		Object property = field.get(ownerClass);
		return property;
	}

	/**
	 * 执行相应参数的Get方法
	 * 
	 * @param owner
	 *            对象
	 * @param methodName
	 *            参数名
	 * @return 方法返回值
	 * @throws Exception
	 */
	public static Object invokeGetMethod(Object owner, String privateField)
			throws Exception {
		if (Const.isEmpty(privateField)) {
			return null;
		}
		String methodName = "get" + privateField.substring(0, 1).toUpperCase();
		if (privateField.length() > 1) {
			methodName += privateField.substring(1);
		}

		return invokeMethod(owner, methodName, new Object[] {});
	}

	/**
	 * 执行某对象方法
	 * 
	 * @param owner
	 *            对象
	 * @param methodName
	 *            方法名
	 * @return 方法返回值
	 * @throws Exception
	 */
	public static Object invokeMethod(Object owner, String methodName)
			throws Exception {
		return invokeMethod(owner, methodName, new Object[] {});
	}

	/**
	 * 执行某对象方法
	 * 
	 * @param owner
	 *            对象
	 * @param methodName
	 *            方法名
	 * @param args
	 *            参数
	 * @return 方法返回值
	 * @throws Exception
	 */
	public static Object invokeMethod(Object owner, String methodName,
			Object... args) throws Exception {

		Class<?> ownerClass = owner.getClass();
		int argsNum = 0;
		Class<?>[] argsClass;
		if (args == null) {
			argsClass = new Class[0];
		} else {
			argsNum = args.length;
			argsClass = new Class[argsNum];
		}
		for (int i = 0, j = argsNum; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(owner, args);
	}

	/**
	 * 执行某类的静态方法
	 * 
	 * @param className
	 *            类名
	 * @param methodName
	 *            方法名
	 * @param args
	 *            参数数组
	 * @return 执行方法返回的结果
	 * @throws Exception
	 */
	public static Object invokeStaticMethod(String className,
			String methodName, Object... args) throws Exception {
		Class<?> ownerClass = Class.forName(className);
		Class<?>[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(null, args);
	}

	/**
	 * 新建实例
	 * 
	 * @param className
	 *            类名
	 * @param args
	 *            构造函数的参数
	 * @return 新建的实例
	 * @throws Exception
	 */
	public static Object newInstance(String className, Object... args)
			throws Exception {
		Class<?> newoneClass = Class.forName(className);
		return newInstance(newoneClass, args);
	}

	/**
	 * 新建实例
	 * 
	 * @param clazz
	 *            类
	 * @param args
	 *            构造函数的参数
	 * @return 新建的实例
	 * @throws Exception
	 */
	public static Object newInstance(Class<?> clazz, Object... args)
			throws Exception {
		Class<?>[] argsClass;
		if (args == null) {
			argsClass = new Class[0];
		} else {
			argsClass = new Class[args.length];
			for (int i = 0, j = args.length; i < j; i++) {
				argsClass[i] = args[i].getClass();
			}
		}
		Constructor<?> cons = clazz.getConstructor(argsClass);
		return cons.newInstance(args);
	}

	/**
	 * 是不是某个类的实例
	 * 
	 * @param obj
	 *            实例
	 * @param cls
	 *            类
	 * @return 如果 obj 是此类的实例，则返回 true
	 */
	public static boolean isInstance(Object obj, Class<?> cls) {
		return cls.isInstance(obj);
	}

}
