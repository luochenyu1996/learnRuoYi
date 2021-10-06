package com.chenyu.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: learnRuoYi
 * @description: 项目配置类
 * @author: chen yu
 * @create: 2021-10-02 14:57
 * <p>
 * 获取导入上传路径
 * <p>
 * 获取头像上传路径
 * <p>
 * 获取下载路径
 * <p>
 * 获取上传路径
 */

@Component
@ConfigurationProperties(prefix = "ruoyi")  //这个注解表明和配置文件相关？
public class RuoYiConfig {


    //项目名称
    private String name;

    //版本
    private String version;

    //版权年份
    private String copyrightYear;

    //实例子演示开关
    private boolean demoEnabled;

    //上传路径
    private static String profile;


    //获取地址开关  自定义配置是否打开获取位置和关闭获取位置
    private static boolean addressEnabled;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public boolean isDemoEnabled() {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled) {
        this.demoEnabled = demoEnabled;
    }

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        RuoYiConfig.profile = profile;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        RuoYiConfig.addressEnabled = addressEnabled;
    }

    /**
     * 获取导入上传路径
     */

    public static String getImportPath() {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */

    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */

    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */


    public static String getUploadPath() {
        return getProfile() + "/upload";
    }
}
