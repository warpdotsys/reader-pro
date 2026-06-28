package com.htmake.reader.lib.tts.constant;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/lib/tts/constant/VoiceEnum.class */
public enum VoiceEnum {
    zh_HK_HiuGaaiNeural("zh-HK-HiuGaaiNeural", "女", "zh-HK"),
    zh_HK_HiuMaanNeural("zh-HK-HiuMaanNeural", "女", "zh-HK"),
    zh_HK_WanLungNeural("zh-HK-WanLungNeural", "男", "zh-HK"),
    zh_CN_XiaoxiaoNeural("zh-CN-XiaoxiaoNeural", "女", "zh-CN"),
    zh_CN_XiaoyiNeural("zh-CN-XiaoyiNeural", "女", "zh-CN"),
    zh_CN_YunjianNeural("zh-CN-YunjianNeural", "男", "zh-CN"),
    zh_CN_YunxiNeural("zh-CN-YunxiNeural", "男", "zh-CN"),
    zh_CN_YunxiaNeural("zh-CN-YunxiaNeural", "男", "zh-CN"),
    zh_CN_YunyangNeural("zh-CN-YunyangNeural", "男", "zh-CN"),
    zh_CN_liaoning_XiaobeiNeural("zh-CN-liaoning-XiaobeiNeural", "女", "zh-CN-liaoning"),
    zh_TW_HsiaoChenNeural("zh-TW-HsiaoChenNeural", "女", "zh-TW"),
    zh_TW_YunJheNeural("zh-TW-YunJheNeural", "男", "zh-TW"),
    zh_TW_HsiaoYuNeural("zh-TW-HsiaoYuNeural", "女", "zh-TW"),
    zh_CN_shaanxi_XiaoniNeural("zh-CN-shaanxi-XiaoniNeural", "女", "zh-CN-shaanxi"),
    en_US_AriaNeural("en-US-AriaNeural", "女", "en-US"),
    en_US_AnaNeural("en-US-AnaNeural", "女", "en-US"),
    en_US_ChristopherNeural("en-US-ChristopherNeural", "男", "en-US"),
    en_US_EricNeural("en-US-EricNeural", "男", "en-US"),
    en_US_GuyNeural("en-US-GuyNeural", "男", "en-US"),
    en_US_JennyNeural("en-US-JennyNeural", "女", "en-US"),
    en_US_MichelleNeural("en-US-MichelleNeural", "女", "en-US"),
    en_US_RogerNeural("en-US-RogerNeural", "男", "en-US"),
    en_US_SteffanNeural("en-US-SteffanNeural", "男", "en-US"),
    zh_CN_XiaochenNeural("zh-CN-XiaochenNeural", "女", "zh-CN"),
    zh_CN_XiaohanNeural("zh-CN-XiaohanNeural", "女", "zh-CN"),
    zh_CN_XiaomengNeural("zh-CN-XiaomengNeural", "女", "zh-CN"),
    zh_CN_XiaomoNeural("zh-CN-XiaomoNeural", "女", "zh-CN"),
    zh_CN_XiaoqiuNeural("zh-CN-XiaoqiuNeural", "女", "zh-CN"),
    zh_CN_XiaoruiNeural("zh-CN-XiaoruiNeural", "女", "zh-CN"),
    zh_CN_XiaoshuangNeural("zh-CN-XiaoshuangNeural", "女", "zh-CN"),
    zh_CN_XiaoxuanNeural("zh-CN-XiaoxuanNeural", "女", "zh-CN"),
    zh_CN_XiaoyanNeural("zh-CN-XiaoyanNeural", "女", "zh-CN"),
    zh_CN_XiaoyouNeural("zh-CN-XiaoyouNeural", "女", "zh-CN"),
    zh_CN_XiaozhenNeural("zh-CN-XiaozhenNeural", "女", "zh-CN"),
    zh_CN_YunfengNeural("zh-CN-YunfengNeural", "男", "zh-CN"),
    zh_CN_YunhaoNeural("zh-CN-YunhaoNeural", "男", "zh-CN"),
    zh_CN_YunyeNeural("zh-CN-YunyeNeural", "男", "zh-CN"),
    zh_CN_YunzeNeural("zh-CN-YunzeNeural", "男", "zh-CN");

    private final String shortName;
    private final String gender;
    private final String locale;

    VoiceEnum(String shortName, String gender, String locale) {
        this.shortName = shortName;
        this.gender = gender;
        this.locale = locale;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getGender() {
        return this.gender;
    }

    public String getLocale() {
        return this.locale;
    }

    public static VoiceEnum fromSortName(String shortName) {
        for (VoiceEnum eVoiceEnum : values()) {
            if (eVoiceEnum.getShortName().equals(shortName)) {
                return eVoiceEnum;
            }
        }
        return null;
    }
}
