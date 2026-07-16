// 
// Decompiled by Procyon v0.6.0
// 

package org.kxml2.wap.wv;

import java.io.IOException;
import org.kxml2.wap.WbxmlParser;

public abstract class WV
{
    public static final String[] tagTablePage0;
    public static final String[] tagTablePage1;
    public static final String[] tagTablePage2;
    public static final String[] tagTablePage3;
    public static final String[] tagTablePage4;
    public static final String[] tagTablePage5;
    public static final String[] tagTablePage6;
    public static final String[] tagTablePage7;
    public static final String[] tagTablePage8;
    public static final String[] tagTablePage9;
    public static final String[] tagTablePageA;
    public static final String[] attrStartTable;
    public static final String[] attrValueTable;
    
    public static WbxmlParser createParser() throws IOException {
        final WbxmlParser parser = new WbxmlParser();
        parser.setTagTable(0, WV.tagTablePage0);
        parser.setTagTable(1, WV.tagTablePage1);
        parser.setTagTable(2, WV.tagTablePage2);
        parser.setTagTable(3, WV.tagTablePage3);
        parser.setTagTable(4, WV.tagTablePage4);
        parser.setTagTable(5, WV.tagTablePage5);
        parser.setTagTable(6, WV.tagTablePage6);
        parser.setTagTable(7, WV.tagTablePage7);
        parser.setTagTable(8, WV.tagTablePage8);
        parser.setTagTable(9, WV.tagTablePage9);
        parser.setTagTable(10, WV.tagTablePageA);
        parser.setAttrStartTable(0, WV.attrStartTable);
        parser.setAttrValueTable(0, WV.attrValueTable);
        return parser;
    }
    
    static {
        tagTablePage0 = new String[] { "Acceptance", "AddList", "AddNickList", "SName", "WV-CSP-Message", "ClientID", "Code", "ContactList", "ContentData", "ContentEncoding", "ContentSize", "ContentType", "DateTime", "Description", "DetailedResult", "EntityList", "Group", "GroupID", "GroupList", "InUse", "Logo", "MessageCount", "MessageID", "MessageURI", "MSISDN", "Name", "NickList", "NickName", "Poll", "Presence", "PresenceSubList", "PresenceValue", "Property", "Qualifier", "Recipient", "RemoveList", "RemoveNickList", "Result", "ScreenName", "Sender", "Session", "SessionDescriptor", "SessionID", "SessionType", "Status", "Transaction", "TransactionContent", "TransactionDescriptor", "TransactionID", "TransactionMode", "URL", "URLList", "User", "UserID", "UserList", "Validity", "Value" };
        tagTablePage1 = new String[] { "AllFunctions", "AllFunctionsRequest", "CancelInvite-Request", "CancelInviteUser-Request", "Capability", "CapabilityList", "CapabilityRequest", "ClientCapability-Request", "ClientCapability-Response", "DigestBytes", "DigestSchema", "Disconnect", "Functions", "GetSPInfo-Request", "GetSPInfo-Response", "InviteID", "InviteNote", "Invite-Request", "Invite-Response", "InviteType", "InviteUser-Request", "InviteUser-Response", "KeepAlive-Request", "KeepAliveTime", "Login-Request", "Login-Response", "Logout-Request", "Nonce", "Password", "Polling-Request", "ResponseNote", "SearchElement", "SearchFindings", "SearchID", "SearchIndex", "SearchLimit", "KeepAlive-Response", "SearchPairList", "Search-Request", "Search-Response", "SearchResult", "Service-Request", "Service-Response", "SessionCookie", "StopSearch-Request", "TimeToLive", "SearchString", "CompletionFlag", null, "ReceiveList", "VerifyID-Request", "Extended-Request", "Extended-Response", "AgreedCapabilityList", "Extended-Data", "OtherServer", "PresenceAttributeNSName", "SessionNSName", "TransactionNSName" };
        tagTablePage2 = new String[] { "ADDGM", "AttListFunc", "BLENT", "CAAUT", "CAINV", "CALI", "CCLI", "ContListFunc", "CREAG", "DALI", "DCLI", "DELGR", "FundamentalFeat", "FWMSG", "GALS", "GCLI", "GETGM", "GETGP", "GETLM", "GETM", "GETPR", "GETSPI", "GETWL", "GLBLU", "GRCHN", "GroupAuthFunc", "GroupFeat", "GroupMgmtFunc", "GroupUseFunc", "IMAuthFunc", "IMFeat", "IMReceiveFunc", "IMSendFunc", "INVIT", "InviteFunc", "MBRAC", "MCLS", "MDELIV", "NEWM", "NOTIF", "PresenceAuthFunc", "PresenceDeliverFunc", "PresenceFeat", "REACT", "REJCM", "REJEC", "RMVGM", "SearchFunc", "ServiceFunc", "SETD", "SETGP", "SRCH", "STSRC", "SUBGCN", "UPDPR", "WVCSPFeat", "MF", "MG", "MM" };
        tagTablePage3 = new String[] { "AcceptedCharset", "AcceptedContentLength", "AcceptedContentType", "AcceptedTransferEncoding", "AnyContent", "DefaultLanguage", "InitialDeliveryMethod", "MultiTrans", "ParserSize", "ServerPollMin", "SupportedBearer", "SupportedCIRMethod", "TCPAddress", "TCPPort", "UDPPort" };
        tagTablePage4 = new String[] { "CancelAuth-Request", "ContactListProperties", "CreateAttributeList-Request", "CreateList-Request", "DefaultAttributeList", "DefaultContactList", "DefaultList", "DeleteAttributeList-Request", "DeleteList-Request", "GetAttributeList-Request", "GetAttributeList-Response", "GetList-Request", "GetList-Response", "GetPresence-Request", "GetPresence-Response", "GetWatcherList-Request", "GetWatcherList-Response", "ListManage-Request", "ListManage-Response", "UnsubscribePresence-Request", "PresenceAuth-Request", "PresenceAuth-User", "PresenceNotification-Request", "UpdatePresence-Request", "SubscribePresence-Request", "Auto-Subscribe", "GetReactiveAuthStatus-Request", "GetReactiveAuthStatus-Response" };
        tagTablePage5 = new String[] { "Accuracy", "Address", "AddrPref", "Alias", "Altitude", "Building", "Caddr", "City", "ClientInfo", "ClientProducer", "ClientType", "ClientVersion", "CommC", "CommCap", "ContactInfo", "ContainedvCard", "Country", "Crossing1", "Crossing2", "DevManufacturer", "DirectContent", "FreeTextLocation", "GeoLocation", "Language", "Latitude", "Longitude", "Model", "NamedArea", "OnlineStatus", "PLMN", "PrefC", "PreferredContacts", "PreferredLanguage", "PreferredContent", "PreferredvCard", "Registration", "StatusContent", "StatusMood", "StatusText", "Street", "TimeZone", "UserAvailability", "Cap", "Cname", "Contact", "Cpriority", "Cstatus", "Note", "Zone", null, "Inf_link", "InfoLink", "Link", "Text" };
        tagTablePage6 = new String[] { "BlockList", "BlockEntity-Request", "DeliveryMethod", "DeliveryReport", "DeliveryReport-Request", "ForwardMessage-Request", "GetBlockedList-Request", "GetBlockedList-Response", "GetMessageList-Request", "GetMessageList-Response", "GetMessage-Request", "GetMessage-Response", "GrantList", "MessageDelivered", "MessageInfo", "MessageNotification", "NewMessage", "RejectMessage-Request", "SendMessage-Request", "SendMessage-Response", "SetDeliveryMethod-Request", "DeliveryTime" };
        tagTablePage7 = new String[] { "AddGroupMembers-Request", "Admin", "CreateGroup-Request", "DeleteGroup-Request", "GetGroupMembers-Request", "GetGroupMembers-Response", "GetGroupProps-Request", "GetGroupProps-Response", "GroupChangeNotice", "GroupProperties", "Joined", "JoinedRequest", "JoinGroup-Request", "JoinGroup-Response", "LeaveGroup-Request", "LeaveGroup-Response", "Left", "MemberAccess-Request", "Mod", "OwnProperties", "RejectList-Request", "RejectList-Response", "RemoveGroupMembers-Request", "SetGroupProps-Request", "SubscribeGroupNotice-Request", "SubscribeGroupNotice-Response", "Users", "WelcomeNote", "JoinGroup", "SubscribeNotification", "SubscribeType", "GetJoinedUsers-Request", "GetJoinedUsers-Response", "AdminMapList", "AdminMapping", "Mapping", "ModMapping", "UserMapList", "UserMapping" };
        tagTablePage8 = new String[] { "MP", "GETAUT", "GETJU", "VRID", "VerifyIDFunc" };
        tagTablePage9 = new String[] { "CIR", "Domain", "ExtBlock", "HistoryPeriod", "IDList", "MaxWatcherList", "ReactiveAuthState", "ReactiveAuthStatus", "ReactiveAuthStatusList", "Watcher", "WatcherStatus" };
        tagTablePageA = new String[] { "WV-CSP-NSDiscovery-Request", "WV-CSP-NSDiscovery-Response", "VersionList" };
        attrStartTable = new String[] { "xmlns=http://www.wireless-village.org/CSP", "xmlns=http://www.wireless-village.org/PA", "xmlns=http://www.wireless-village.org/TRC", "xmlns=http://www.openmobilealliance.org/DTD/WV-CSP", "xmlns=http://www.openmobilealliance.org/DTD/WV-PA", "xmlns=http://www.openmobilealliance.org/DTD/WV-TRC" };
        attrValueTable = new String[] { "AccessType", "ActiveUsers", "Admin", "application/", "application/vnd.wap.mms-message", "application/x-sms", "AutoJoin", "BASE64", "Closed", "Default", "DisplayName", "F", "G", "GR", "http://", "https://", "image/", "Inband", "IM", "MaxActiveUsers", "Mod", "Name", "None", "N", "Open", "Outband", "PR", "Private", "PrivateMessaging", "PrivilegeLevel", "Public", "P", "Request", "Response", "Restricted", "ScreenName", "Searchable", "S", "SC", "text/", "text/plain", "text/x-vCalendar", "text/x-vCard", "Topic", "T", "Type", "U", "US", "www.wireless-village.org", "AutoDelete", "GM", "Validity", "ShowID", "GRANTED", "PENDING", null, null, null, null, null, null, "GROUP_ID", "GROUP_NAME", "GROUP_TOPIC", "GROUP_USER_ID_JOINED", "GROUP_USER_ID_OWNER", "HTTP", "SMS", "STCP", "SUDP", "USER_ALIAS", "USER_EMAIL_ADDRESS", "USER_FIRST_NAME", "USER_ID", "USER_LAST_NAME", "USER_MOBILE_NUMBER", "USER_ONLINE_STATUS", "WAPSMS", "WAPUDP", "WSP", "GROUP_USER_ID_AUTOJOIN", null, null, null, null, null, null, null, null, null, null, "ANGRY", "ANXIOUS", "ASHAMED", "AUDIO_CALL", "AVAILABLE", "BORED", "CALL", "CLI", "COMPUTER", "DISCREET", "EMAIL", "EXCITED", "HAPPY", "IM", "IM_OFFLINE", "IM_ONLINE", "IN_LOVE", "INVINCIBLE", "JEALOUS", "MMS", "MOBILE_PHONE", "NOT_AVAILABLE", "OTHER", "PDA", "SAD", "SLEEPY", "SMS", "VIDEO_CALL", "VIDEO_STREAM" };
    }
}
