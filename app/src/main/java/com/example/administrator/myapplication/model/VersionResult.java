package com.example.administrator.myapplication.model;

public class VersionResult {

    /**
     * id : 1
     * name : V2
     * apkurl : http://111.230.222.252:8982/file/apk2.apk
     * versionname : apk2
     * versioncode : 2
     */

    private int id;
    private String name;
    private String apkurl;
    private String versionname;
    private String versioncode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }
}
