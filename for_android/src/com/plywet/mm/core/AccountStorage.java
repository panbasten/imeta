package com.plywet.mm.core;

import com.plywet.mm.database.SqliteDB;
import com.plywet.mm.tablecreator.*;

public class AccountStorage {
	
	static {
		SqliteDB.put(new TConfigCreator(), "CONFIG_TABLE".hashCode());
		SqliteDB.put(new TContactCreator(), "CONTACT_TABLE".hashCode());
		SqliteDB.put(new TChatroomMembersCreator(), "CHATROOM_MEMBERS_TABLE".hashCode());
		SqliteDB.put(new TConversationCreator(), "CONVERSATION_TABLE".hashCode());
		SqliteDB.put(new TMessageCreator(), "MESSAGE_TABLE".hashCode());
		SqliteDB.put(new TRoleInfoCreator(), "ROLEINFO_TABLE".hashCode());
		SqliteDB.put(new TImgInfoCreator(), "IMGINFO_TABLE".hashCode());
		SqliteDB.put(new TVoiceCreator(), "VOICE_TABLE".hashCode());
		SqliteDB.put(new TAddrUploadCreator(), "ADDR_UPLOAD_TABLE".hashCode());
		SqliteDB.put(new TImgFlagCreator(), "IMGFLAG_TABLE".hashCode());
		SqliteDB.put(new TVerifyContactCreator(), "VERIFY_CONTACT_TABLE".hashCode());
		SqliteDB.put(new TQqListCreator(), "QQLIST_TABLE".hashCode());
		SqliteDB.put(new TQqGroupCreator(), "QQGROUP_TABLE".hashCode());
		SqliteDB.put(new TFriendExtCreator(), "FRIENDEXT_TABLE".hashCode());
		SqliteDB.put(new TVideoInfoCreator(), "VIDEOINFO_TABLE".hashCode());
		SqliteDB.put(new TNetstatCreator(), "NETSTAT_TABLE".hashCode());
		SqliteDB.put(new TQContactCreator(), "QCONTACT_TABLE".hashCode());
		SqliteDB.put(new TTContactCreator(), "TCONTACT_TABLE".hashCode());
		SqliteDB.put(new THDHeadImgInfoCreator(), "HDHEADIMGINFO_TABLE".hashCode());
		SqliteDB.put(new TThRowBottleInfoCreator(), "THROWBOTTLEINFO_TABLE".hashCode());
		SqliteDB.put(new TThemeInfoCreator(), "THEMEINFO_TABLE".hashCode());
		SqliteDB.put(new TShakeItemCreator(), "SHAKEITEM_TABLE".hashCode());
		SqliteDB.put(new TInviteFriendOpenCreator(), "INVITEFRIENDOPEN_TABLE".hashCode());
		SqliteDB.put(new TVUserInfoCreator(), "VUSERINFO_TABLE".hashCode());
	}

}
