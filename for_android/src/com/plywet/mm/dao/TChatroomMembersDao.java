package com.plywet.mm.dao;

public class TChatroomMembersDao {
	public static final String[] sqls;

	static {
		sqls = new String[] {
				"CREATE TABLE IF NOT EXISTS chatroom_members ( ChatRoomName VARCHAR(40),username TEXT,AddTime LONG,reserved1 INT DEFAULT 0,reserved2 TEXT )",
				"CREATE UNIQUE INDEX IF NOT EXISTS ChatRoomMembersChatRoomNameIndex   ON chatroom_members ( ChatRoomName )" };
	}
}
