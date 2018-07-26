package com.wangke.core.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/10
 *    desc   : 工具类: OS 系统相关
 *             简介  : 由于国内定制系统的泛滥, 不同定制系统的一些功能或实现方法会有所不同, 如果需要做到足够好的适配工作, 需要
 *                     对不同的定制系统做一些专门的适配.
 *             API  : 获取 ROM 类型等
 * </pre>
 */
public class OSUtils {

    private OSUtils() {
    }

    /**
     * ROM 类型
     */
//    private static final ROM ROM_TYPE = initRomType();

    private static final String KEY_DISPLAY_ID = "ro.build.display.id";
    private static final String KEY_BASE_OS_VERSION = "ro.build.version.base_os";
    private static final String KEY_CLIENT_ID_BASE = "ro.com.google.clientidbase";

    // 小米 : MIUI
    private static final String KEY_MIUI_VERSION = "ro.build.version.incremental"; // "7.6.15"
    private static final String KEY_MIUI_VERSION_NANE = "ro.miui.ui.version.name"; // "V8"
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"; // "6"

    private static final String VALUE_MIUI_CLIENT_ID_BASE = "android-xiaomi";
    // 华为 : EMUI
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui"; // "EmotionUI_3.0"
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level"; //
    private static final String KEY_EMUI_SYSTEM_VERSION = "ro.confg.hw_systemversion"; // "T1-A21wV100R001C233B008_SYSIMG"
    // 魅族 : Flyme
    private static final String KEY_FLYME_PUBLISHED = "ro.flyme.published"; // "true"
    private static final String KEY_FLYME_SETUP = "ro.meizu.setupwizard.flyme"; // "true"

    private static final String VALUE_FLYME_DISPLAY_ID_CONTAIN = "Flyme"; // "Flyme OS 4.5.4.2U"
    // OPPO : ColorOS
    private static final String KEY_COLOROS_VERSION = "ro.oppo.theme.version"; // "703"
    private static final String KEY_COLOROS_THEME_VERSION = "ro.oppo.version"; // ""
    private static final String KEY_COLOROS_ROM_VERSION = "ro.rom.different.version"; // "ColorOS2.1"

    private static final String VALUE_COLOROS_BASE_OS_VERSION_CONTAIN = "OPPO"; // "OPPO/R7sm/R7sm:5.1.1/LMY47V/1440928800:user/release-keys"
    private static final String VALUE_COLOROS_CLIENT_ID_BASE = "android-oppo";
    // vivo : FuntouchOS
    private static final String KEY_FUNTOUCHOS_BOARD_VERSION = "ro.vivo.board.version"; // "MD"
    private static final String KEY_FUNTOUCHOS_OS_NAME = "ro.vivo.os.name"; // "Funtouch"
    private static final String KEY_FUNTOUCHOS_OS_VERSION = "ro.vivo.os.version"; // "3.0"
    private static final String KEY_FUNTOUCHOS_DISPLAY_ID = "ro.vivo.os.build.display.id"; // "FuntouchOS_3.0"
    private static final String KEY_FUNTOUCHOS_ROM_VERSION = "ro.vivo.rom.version"; // "rom_3.1"

    private static final String VALUE_FUNTOUCHOS_CLIENT_ID_BASE = "android-vivo";
    // Samsung
    private static final String VALUE_SAMSUNG_BASE_OS_VERSION_CONTAIN = "samsung"; // "samsung/zeroltezc/zeroltechn:6.0.1/MMB29K/G9250ZCU2DQD1:user/release-keys"
    private static final String VALUE_SAMSUNG_CLIENT_ID_BASE = "android-samsung";
    // Sony
    private static final String KEY_SONY_PROTOCOL_TYPE = "ro.sony.irremote.protocol_type"; // "2"
    private static final String KEY_SONY_ENCRYPTED_DATA = "ro.sony.fota.encrypteddata"; // "supported"

    private static final String VALUE_SONY_CLIENT_ID_BASE = "android-sonyericsson";
    private static final String VALUE_SONY_CLIENT_ID_BASE2 = "android-sonymobile";
    // 乐视 : eui
    private static final String KEY_EUI_VERSION = "ro.letv.release.version"; // "5.9.023S"
    private static final String KEY_EUI_VERSION_DATE = "ro.letv.release.version_date"; // "5.9.023S_03111"
    private static final String KEY_EUI_NAME = "ro.product.letv_name"; // "乐1s"
    private static final String KEY_EUI_MODEL = "ro.product.letv_model"; // "Letv X500"
    // 金立 : amigo
    private static final String KEY_AMIGO_ROM_VERSION = "ro.gn.gnromvernumber"; // "GIONEE ROM5.0.16"
    private static final String KEY_AMIGO_SYSTEM_UI_SUPPORT = "ro.gn.amigo.systemui.support"; // "yes"

    private static final String VALUE_AMIGO_DISPLAY_ID_CONTAIN = "amigo"; // "amigo3.5.1"
    private static final String VALUE_AMIGO_CLIENT_ID_BASE = "android-gionee";
    // 酷派 : yulong
    private static final String KEY_YULONG_VERSION_RELEASE = "ro.yulong.version.release"; // "5.1.046.P1.150921.8676_M01"
    private static final String KEY_YULONG_VERSION_TAG = "ro.yulong.version.tag"; // "LC"

    private static final String VALUE_YULONG_CLIENT_ID_BASE = "android-coolpad";
    // HTC : Sense
    private static final String KEY_SENSE_BUILD_STAGE = "htc.build.stage"; // "2"
    private static final String KEY_SENSE_BLUETOOTH_SAP = "ro.htc.bluetooth.sap"; // "true"

    private static final String VALUE_SENSE_CLIENT_ID_BASE = "android-htc-rev";
    // LG : LG
    private static final String KEY_LG_SW_VERSION = "ro.lge.swversion"; // "D85720b"
    private static final String KEY_LG_SW_VERSION_SHORT = "ro.lge.swversion_short"; // "V20b"
    private static final String KEY_LG_FACTORY_VERSION = "ro.lge.factoryversion"; // "LGD857AT-00-V20b-CUO-CN-FEB-17-2015+0"
    // 联想
    private static final String KEY_LENOVO_DEVICE = "ro.lenovo.device"; // "phone"
    private static final String KEY_LENOVO_PLATFORM = "ro.lenovo.platform"; // "qualcomm"
    private static final String KEY_LENOVO_ADB = "ro.lenovo.adb"; // "apkctl,speedup"

    private static final String VALUE_LENOVO_CLIENT_ID_BASE = "android-lenovo";

    //SmartisanOS
    private static final String KEY_SMARTISAN_IMEISV = "ro.smartisan.imeisv";
    private static final String KEY_SMARTISAN_SA = "ro.smartisan.sa";
    private static final String KEY_SMARTISAN_TAG = "ro.smartisan.tag";
    private static final String KEY_SMARTISAN_VER = "ro.smartisan.version";

    /**
     * 获取 ROM 类型
     *
     * @return ROM
     */
//    public static ROM getRomType() {
//        return ROM_TYPE;
//    }
    public static String getProperty(String property) {
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd("getprop " + property, false);
        if (commandResult != null && !TextUtils.isEmpty(commandResult.successMsg)) {
            return commandResult.successMsg;
        }
        return "";
    }

    public static boolean hasBuildPropertie(String property) {
        return !TextUtils.isEmpty(getProperty((property)));
    }



    /**
     * 初始化 ROM 类型
     */
    public static ROM getRomType() {
        ROM rom = ROM.Other;

        String  rom_type = (String) LSSpUtil.get("rom_type", "");

        if (!TextUtils.isEmpty(rom_type)) {
            return ROM.valueOf(rom_type);
        }

        if (hasBuildPropertie(KEY_MIUI_VERSION_NANE) || hasBuildPropertie(KEY_MIUI_VERSION_CODE)) {
            // MIUI
            rom = ROM.MIUI;
            if (hasBuildPropertie(KEY_MIUI_VERSION_NANE)) {
                String versionName = getProperty(KEY_MIUI_VERSION_NANE);
                if (!TextUtils.isEmpty(versionName) && versionName.matches("[Vv]\\d+")) { // V8
                    try {
                        rom.setBaseVersion(Integer.parseInt(versionName.split("[Vv]")[1]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (hasBuildPropertie(KEY_MIUI_VERSION)) {
                String versionStr = getProperty(KEY_MIUI_VERSION);
                if (!TextUtils.isEmpty(versionStr) && versionStr.matches("[\\d.]+")) {
                    rom.setVersion(versionStr);
                }
            }
        } else if (hasBuildPropertie(KEY_EMUI_VERSION) || hasBuildPropertie(KEY_EMUI_API_LEVEL)
                || hasBuildPropertie(KEY_EMUI_SYSTEM_VERSION)) {
            // EMUI
            rom = ROM.EMUI;
            if (hasBuildPropertie(KEY_EMUI_VERSION)) {
                String versionStr = getProperty(KEY_EMUI_VERSION);
                Matcher matcher = Pattern.compile("EmotionUI_([\\d.]+)").matcher(versionStr); // EmotionUI_3.0
                if (!TextUtils.isEmpty(versionStr) && matcher.find()) {
                    try {
                        String version = matcher.group(1);
                        rom.setVersion(version);
                        rom.setBaseVersion(Integer.parseInt(version.split("\\.")[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (hasBuildPropertie(KEY_FLYME_SETUP) || hasBuildPropertie(KEY_FLYME_PUBLISHED)) {
            // Flyme
            rom = ROM.Flyme;
            if (hasBuildPropertie(KEY_DISPLAY_ID)) {
                String versionStr = getProperty(KEY_DISPLAY_ID);
                Matcher matcher = Pattern.compile("Flyme[^\\d]*([\\d.]+)[^\\d]*").matcher(versionStr); // Flyme OS 4.5.4.2U
                if (!TextUtils.isEmpty(versionStr) && matcher.find()) {
                    try {
                        String version = matcher.group(1);
                        rom.setVersion(version);
                        rom.setBaseVersion(Integer.parseInt(version.split("\\.")[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (hasBuildPropertie(KEY_COLOROS_VERSION) || hasBuildPropertie(KEY_COLOROS_THEME_VERSION)
                || hasBuildPropertie(KEY_COLOROS_ROM_VERSION)) {
            // ColorOS
            rom = ROM.ColorOS;
            if (hasBuildPropertie(KEY_COLOROS_ROM_VERSION)) {
                String versionStr = getProperty(KEY_COLOROS_ROM_VERSION);
                Matcher matcher = Pattern.compile("ColorOS([\\d.]+)").matcher(versionStr); // ColorOS2.1
                if (!TextUtils.isEmpty(versionStr) && matcher.find()) {
                    try {
                        String version = matcher.group(1);
                        rom.setVersion(version);
                        rom.setBaseVersion(Integer.parseInt(version.split("\\.")[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (hasBuildPropertie(KEY_FUNTOUCHOS_OS_NAME) || hasBuildPropertie(KEY_FUNTOUCHOS_OS_VERSION)
                || hasBuildPropertie(KEY_FUNTOUCHOS_DISPLAY_ID)) {
            // FuntouchOS
            rom = ROM.FuntouchOS;
            if (hasBuildPropertie(KEY_FUNTOUCHOS_OS_VERSION)) {
                String versionStr = getProperty(KEY_FUNTOUCHOS_OS_VERSION);
                if (!TextUtils.isEmpty(versionStr) && versionStr.matches("[\\d.]+")) { // 3.0
                    try {
                        rom.setVersion(versionStr);
                        rom.setBaseVersion(Integer.parseInt(versionStr.split("\\.")[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (hasBuildPropertie(KEY_EUI_VERSION) || hasBuildPropertie(KEY_EUI_NAME)
                || hasBuildPropertie(KEY_EUI_MODEL)) {
            // EUI
            rom = ROM.EUI;
            if (hasBuildPropertie(KEY_EUI_VERSION)) {
                String versionStr = getProperty(KEY_EUI_VERSION);
                Matcher matcher = Pattern.compile("([\\d.]+)[^\\d]*").matcher(versionStr); // 5.9.023S
                if (!TextUtils.isEmpty(versionStr) && matcher.find()) {
                    try {
                        String version = matcher.group(1);
                        rom.setVersion(version);
                        rom.setBaseVersion(Integer.parseInt(version.split("\\.")[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (hasBuildPropertie(KEY_AMIGO_ROM_VERSION) || hasBuildPropertie(KEY_AMIGO_SYSTEM_UI_SUPPORT)) {
            // amigo
            rom = ROM.AmigoOS;
            if (hasBuildPropertie(KEY_DISPLAY_ID)) {
                String versionStr = getProperty(KEY_DISPLAY_ID);
                Matcher matcher = Pattern.compile("amigo([\\d.]+)[a-zA-Z]*").matcher(versionStr); // "amigo3.5.1"
                if (!TextUtils.isEmpty(versionStr) && matcher.find()) {
                    try {
                        String version = matcher.group(1);
                        rom.setVersion(version);
                        rom.setBaseVersion(Integer.parseInt(version.split("\\.")[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (hasBuildPropertie(KEY_SONY_PROTOCOL_TYPE) || hasBuildPropertie(KEY_SONY_ENCRYPTED_DATA)) {
            // Sony
            rom = ROM.Sony;
        } else if (hasBuildPropertie(KEY_YULONG_VERSION_RELEASE) || hasBuildPropertie(KEY_YULONG_VERSION_TAG)) {
            // YuLong
            rom = ROM.YuLong;
        } else if (hasBuildPropertie(KEY_SENSE_BUILD_STAGE) || hasBuildPropertie(KEY_SENSE_BLUETOOTH_SAP)) {
            // Sense
            rom = ROM.Sense;
        } else if (hasBuildPropertie(KEY_LG_SW_VERSION) || hasBuildPropertie(KEY_LG_SW_VERSION_SHORT)
                || hasBuildPropertie(KEY_LG_FACTORY_VERSION)) {
            // LG
            rom = ROM.LG;
        } else if (hasBuildPropertie(KEY_LENOVO_DEVICE) || hasBuildPropertie(KEY_LENOVO_PLATFORM)
                || hasBuildPropertie(KEY_LENOVO_ADB)) {
            // Lenovo
            rom = ROM.Lenovo;
        }else if (hasBuildPropertie(KEY_SMARTISAN_IMEISV) || hasBuildPropertie(KEY_SMARTISAN_SA)
                || hasBuildPropertie(KEY_SMARTISAN_TAG)  || hasBuildPropertie(KEY_SMARTISAN_VER)) {
            // Lenovo
            rom = ROM.SmartisanOS;
        }/* else if (hasBuildPropertie(KEY_DISPLAY_ID)) {
            String displayId = getProperty(KEY_DISPLAY_ID);
            if (!TextUtils.isEmpty(displayId)) {
                if (displayId.contains(VALUE_FLYME_DISPLAY_ID_CONTAIN)) {
                    return ROM.Flyme;
                } else if (displayId.contains(VALUE_AMIGO_DISPLAY_ID_CONTAIN)) {
                    return ROM.AmigoOS;
                }
            }
        }*//* else if (hasBuildPropertie(KEY_BASE_OS_VERSION)) {
            String baseOsVersion = getProperty(KEY_BASE_OS_VERSION);
            if (!TextUtils.isEmpty(baseOsVersion)) {
                if (baseOsVersion.contains(VALUE_COLOROS_BASE_OS_VERSION_CONTAIN)) {
                    return ROM.ColorOS;
                } else if (baseOsVersion.contains(VALUE_SAMSUNG_BASE_OS_VERSION_CONTAIN)) {
                    return ROM.SamSung;
                }
            }
        }*/ else if (hasBuildPropertie(KEY_CLIENT_ID_BASE)) {
            String clientIdBase = getProperty(KEY_CLIENT_ID_BASE);
            switch (clientIdBase) {
                case VALUE_MIUI_CLIENT_ID_BASE:
                    return ROM.MIUI;
                case VALUE_COLOROS_CLIENT_ID_BASE:
                    return ROM.ColorOS;
                case VALUE_FUNTOUCHOS_CLIENT_ID_BASE:
                    return ROM.FuntouchOS;
                case VALUE_SAMSUNG_CLIENT_ID_BASE:
                    return ROM.SamSung;
                case VALUE_SONY_CLIENT_ID_BASE:
                case VALUE_SONY_CLIENT_ID_BASE2:
                    return ROM.Sony;
                case VALUE_YULONG_CLIENT_ID_BASE:
                    return ROM.YuLong;
                case VALUE_SENSE_CLIENT_ID_BASE:
                    return ROM.Sense;
                case VALUE_LENOVO_CLIENT_ID_BASE:
                    return ROM.Lenovo;
                case VALUE_AMIGO_CLIENT_ID_BASE:
                    return ROM.AmigoOS;
                default:
                    break;
            }
        }

         LSSpUtil.put("rom_type", rom.toString());

        return rom;
    }

    public enum ROM {
        MIUI, // 小米
        Flyme, // 魅族
        EMUI, // 华为
        ColorOS, // OPPO
        FuntouchOS, // vivo
        SmartisanOS, // 锤子
        EUI, // 乐视
        Sense, // HTC
        AmigoOS, // 金立
        _360OS, // 奇酷360
        NubiaUI, // 努比亚
        H2OS, // 一加
        YunOS, // 阿里巴巴
        YuLong, // 酷派

        SamSung, // 三星
        Sony, // 索尼
        Lenovo, // 联想
        LG, // LG

        Google, // 原生

        Other; // CyanogenMod, Lewa OS, 百度云OS, Tencent OS, 深度OS, IUNI OS, Tapas OS, Mokee

        private int baseVersion = -1;
        private String version;

        void setVersion(String version) {
            this.version = version;
        }

        void setBaseVersion(int baseVersion) {
            this.baseVersion = baseVersion;
        }

        public int getBaseVersion() {
            return baseVersion;
        }

        public String getVersion() {
            return version;
        }
    }
}
