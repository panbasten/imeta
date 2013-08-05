/**
 * @(#)InputMeta.java 1.00 2009/06/22
 *
 * Copyright (c) 2009 中科软科技股份有限公司 版权所有
 * Sinosoft Co.,LTD. All rights reserved.
 * 
 * This software was developed by Sinosoft Corporation. 
 * You shall not disclose and decompile such software 
 * information or code and shall use it only in accordance 
 * with the terms of the contract agreement you entered 
 * into with Sinosoft.
 */
package com.panet.iform;

import java.util.MissingResourceException;

import com.panet.imeta.i18n.BaseMessages;

/**
* 消息功能
* 
*@author panwei
*@version 1.0 
*@since 2009/06/22
*/ 
public class Messages {
	public static final String packageName = Messages.class.getPackage().getName();
    
    
	public static String getString(String key) {
		try {
			return BaseMessages.getString(packageName, key);
		} catch (MissingResourceException e) {
            return Messages.getString(key);
		}
	}
    
    public static String getString(String key, String param1) {
        try {
            return BaseMessages.getString(packageName, key, param1);
        } catch (MissingResourceException e) {
            return Messages.getString(key, param1);
        }
    }

    public static String getString(String key, String param1, String param2) {
        try {
            return BaseMessages.getString(packageName, key, param1, param2);
        } catch (MissingResourceException e) {
            return Messages.getString(key, param1, param2);
        }
    }

    public static String getString(String key, String param1, String param2, String param3) {
        try {
            return BaseMessages.getString(packageName, key, param1, param2, param3);
        } catch (MissingResourceException e) {
            return Messages.getString(key, param1, param2);
        }
    }
    
    public static String getString(String key, String param1, String param2, String param3,String param4) {
        try {
            return BaseMessages.getString(packageName, key, param1, param2, param3,param4);
        } catch (MissingResourceException e) {
            return Messages.getString(key, param1, param2);
        }
    }
    
    public static String getString(String key, String param1, String param2, String param3,String param4,String param5) {
        try {
            return BaseMessages.getString(packageName, key, param1, param2, param3,param4,param5);
        } catch (MissingResourceException e) {
        
            return Messages.getString(key, param1, param2);
        }
    }

}
