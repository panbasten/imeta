package com.panet.imeta.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据对象注释类
 * 
 * @author Peter Pan
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityNode {
	// 数据对象的名称（ID，类型）
	String[] name();

	// 数据对象描述
	String description() default "";

	// 数据对象提示说明
	String tooltip() default "";

	// 数据对象图片
	String image();

	// 数据对象类型
	int category();

	// 数据对象类型描述
	String categoryDescription() default "";

	// 国际化包路径
	String i18nPackageName() default "";
}
