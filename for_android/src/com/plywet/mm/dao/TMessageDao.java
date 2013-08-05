package com.plywet.mm.dao;

public class TMessageDao {
	public static final String[] sqls;

	static {
		sqls = new String[] {
				"CREATE TABLE IF NOT EXISTS message ( msgId INTEGER PRIMARY KEY, msgSvrId INTEGER , type INT, status INT, isSend INT, isShowTimer INTEGER, createTime INTEGER, talker TEXT, content TEXT, imgPath TEXT, reserved TEXT )",
				"CREATE INDEX IF NOT EXISTS  serverContactIndex ON message ( msgSvrId )",
				"CREATE INDEX IF NOT EXISTS  messageTalkerIndex ON message ( talker )",
				"CREATE INDEX IF NOT EXISTS  messageStatusIndex ON message ( status )",
				"CREATE INDEX IF NOT EXISTS  messageCreateTimeIndex ON message ( createTime )",
				"CREATE TABLE IF NOT EXISTS qmessage ( msgId INTEGER PRIMARY KEY, msgSvrId INTEGER , type INT, status INT, isSend INT, isShowTimer INTEGER, createTime INTEGER, talker TEXT, content TEXT, imgPath TEXT, reserved TEXT )",
				"CREATE INDEX IF NOT EXISTS  serverContactIndex ON qmessage ( msgSvrId )",
				"CREATE INDEX IF NOT EXISTS  messageTalkerIndex ON qmessage ( talker )",
				"CREATE INDEX IF NOT EXISTS  messageStatusIndex ON qmessage ( status )",
				"CREATE INDEX IF NOT EXISTS  messageCreateTimeIndex ON qmessage ( createTime )",
				"CREATE TABLE IF NOT EXISTS tmessage ( msgId INTEGER PRIMARY KEY, msgSvrId INTEGER , type INT, status INT, isSend INT, isShowTimer INTEGER, createTime INTEGER, talker TEXT, content TEXT, imgPath TEXT, reserved TEXT )",
				"CREATE INDEX IF NOT EXISTS  serverContactIndex ON tmessage ( msgSvrId )",
				"CREATE INDEX IF NOT EXISTS  messageTalkerIndex ON tmessage ( talker )",
				"CREATE INDEX IF NOT EXISTS  messageStatusIndex ON tmessage ( status )",
				"CREATE INDEX IF NOT EXISTS  messageCreateTimeIndex ON tmessage ( createTime )",
				"CREATE TABLE IF NOT EXISTS bottlemessage ( msgId INTEGER PRIMARY KEY, msgSvrId INTEGER , type INT, status INT, isSend INT, isShowTimer INTEGER, createTime INTEGER, talker TEXT, content TEXT, imgPath TEXT, reserved TEXT )",
				"CREATE INDEX IF NOT EXISTS  serverContactIndex ON bottlemessage ( msgSvrId )",
				"CREATE INDEX IF NOT EXISTS  messageTalkerIndex ON bottlemessage ( talker )",
				"CREATE INDEX IF NOT EXISTS  messageStatusIndex ON bottlemessage ( status )",
				"CREATE INDEX IF NOT EXISTS  messageCreateTimeIndex ON bottlemessage ( createTime )" };
	}
}
