package com.plywet.mm.dao;

public class TConversationDao {

	public static final String[] sqls;

	static {
		sqls = new String[] {
				"CREATE TABLE IF NOT EXISTS conversation ( unReadCount INTEGER, status INT, isSend INT, createTime LONG, username VARCHAR(40), content TEXT, reserved TEXT )",
				"CREATE UNIQUE INDEX IF NOT EXISTS  conversationUsername ON conversation ( username )",
				"CREATE TABLE IF NOT EXISTS bottleconversation ( unReadCount INTEGER, status INT, isSend INT, createTime LONG, username VARCHAR(40), content TEXT, reserved TEXT )",
				"CREATE UNIQUE INDEX IF NOT EXISTS  conversationUsername ON conversation ( username )" };
	}
}
