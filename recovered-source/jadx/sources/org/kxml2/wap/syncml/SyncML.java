package org.kxml2.wap.syncml;

import io.legado.app.constant.RSSKeywords;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.kxml2.wap.WbxmlParser;
import org.kxml2.wap.WbxmlSerializer;

/* JADX INFO: loaded from: app-classes.jar:org/kxml2/wap/syncml/SyncML.class */
public abstract class SyncML {
    public static final String[] TAG_TABLE_0 = {"Add", "Alert", "Archive", "Atomic", "Chal", "Cmd", "CmdID", "CmdRef", "Copy", "Cred", "Data", "Delete", "Exec", "Final", "Get", "Item", "Lang", "LocName", "LocURI", "Map", "MapItem", "Meta", "MsgID", "MsgRef", "NoResp", "NoResults", "Put", "Replace", "RespURI", "Results", "Search", "Sequence", "SessionID", "SftDel", "Source", "SourceRef", "Status", "Sync", "SyncBody", "SyncHdr", "SyncML", "Target", "TargetRef", "Reserved for future use", "VerDTD", "VerProto", "NumberOfChanged", "MoreData", "Field", "Filter", "Record", "FilterType", "SourceParent", "TargetParent", "Move", "Correlator"};
    public static final String[] TAG_TABLE_1 = {"Anchor", "EMI", "Format", "FreeID", "FreeMem", "Last", "Mark", "MaxMsgSize", "Mem", "MetInf", "Next", "NextNonce", "SharedMem", "Size", "Type", "Version", "MaxObjSize", "FieldLevel"};
    public static final String[] TAG_TABLE_2_DM = {"AccessType", "ACL", "Add", "b64", "bin", "bool", "chr", "CaseSense", "CIS", "Copy", "CS", PackageDocumentBase.DCTags.date, "DDFName", "DefaultValue", "Delete", "Description", "DDFFormat", "DFProperties", "DFTitle", "DFType", "Dynamic", "Exec", "float", "Format", "Get", "int", "Man", "MgmtTree", "MIME", "Mod", "Name", "Node", "node", "NodeName", "null", "Occurence", "One", "OneOrMore", "OneOrN", "Path", "Permanent", "Replace", "RTProperties", "Scope", "Size", RSSKeywords.RSS_ITEM_TIME, "Title", "TStamp", "Type", "Value", "VerDTD", "VerNo", "xml", "ZeroOrMore", "ZeroOrN", "ZeroOrOne"};

    public static WbxmlParser createParser() {
        WbxmlParser p = new WbxmlParser();
        p.setTagTable(0, TAG_TABLE_0);
        p.setTagTable(1, TAG_TABLE_1);
        return p;
    }

    public static WbxmlSerializer createSerializer() {
        WbxmlSerializer s = new WbxmlSerializer();
        s.setTagTable(0, TAG_TABLE_0);
        s.setTagTable(1, TAG_TABLE_1);
        return s;
    }

    public static WbxmlParser createDMParser() {
        WbxmlParser p = createParser();
        p.setTagTable(2, TAG_TABLE_2_DM);
        return p;
    }

    public static WbxmlSerializer createDMSerializer() {
        WbxmlSerializer s = createSerializer();
        s.setTagTable(2, TAG_TABLE_2_DM);
        return s;
    }
}
