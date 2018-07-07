package com.nj.nedis.config;

import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2018/7/7.
 */
public class Info {

    private String redis_version = "redis_version:2.5.9";

    private String redis_git_sha1 = "redis_git_sha1:473f3090";

    private String redis_git_dirty = "redis_git_dirty:0";

    private String os = "os:Linux 3.3.7-1-ARCH i686";

    private String arch_bits = "arch_bits:32";

    private String multiplexing_api = "multiplexing_api:epoll";

    private String gcc_version = "gcc_version:4.7.0";

    private String process_id = "process_id:8104";

    private String run_id = "run_id:bc9e20c6f0aac67d0d396ab950940ae4d1479ad1";

    private String tcp_port = "tcp_port:6379";

    private String uptime_in_seconds = "uptime_in_seconds:7";

    private String uptime_in_days = "uptime_in_days:0";

    private String lru_clock = "lru_clock:1680564";


    public static String getInfo(Info info) {

        Map<String, String> map = new HashMap<String, String>(32);

        try {
            map = BeanUtils.describe(info);
        } catch (Exception e) {
            e.printStackTrace();

            return "*1\r\n" +
                    "$20\r\n" +
                    "redis_version:2.9.11\r\n";
        }

        StringBuffer sb = new StringBuffer(1024);

        sb.append("*" + map.size() + "\r\n");

        for (String value : map.values()) {
            sb.append("$" + value.length() + "\r\n" + value + "\r\n");
        }
        return sb.toString().trim();
    }




    public String getRedis_version() {
        return redis_version;
    }

    public void setRedis_version(String redis_version) {
        this.redis_version = redis_version;
    }

    public String getRedis_git_sha1() {
        return redis_git_sha1;
    }

    public void setRedis_git_sha1(String redis_git_sha1) {
        this.redis_git_sha1 = redis_git_sha1;
    }

    public String getRedis_git_dirty() {
        return redis_git_dirty;
    }

    public void setRedis_git_dirty(String redis_git_dirty) {
        this.redis_git_dirty = redis_git_dirty;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getArch_bits() {
        return arch_bits;
    }

    public void setArch_bits(String arch_bits) {
        this.arch_bits = arch_bits;
    }

    public String getMultiplexing_api() {
        return multiplexing_api;
    }

    public void setMultiplexing_api(String multiplexing_api) {
        this.multiplexing_api = multiplexing_api;
    }

    public String getGcc_version() {
        return gcc_version;
    }

    public void setGcc_version(String gcc_version) {
        this.gcc_version = gcc_version;
    }

    public String getProcess_id() {
        return process_id;
    }

    public void setProcess_id(String process_id) {
        this.process_id = process_id;
    }

    public String getRun_id() {
        return run_id;
    }

    public void setRun_id(String run_id) {
        this.run_id = run_id;
    }

    public String getTcp_port() {
        return tcp_port;
    }

    public void setTcp_port(String tcp_port) {
        this.tcp_port = tcp_port;
    }

    public String getUptime_in_seconds() {
        return uptime_in_seconds;
    }

    public void setUptime_in_seconds(String uptime_in_seconds) {
        this.uptime_in_seconds = uptime_in_seconds;
    }

    public String getUptime_in_days() {
        return uptime_in_days;
    }

    public void setUptime_in_days(String uptime_in_days) {
        this.uptime_in_days = uptime_in_days;
    }

    public String getLru_clock() {
        return lru_clock;
    }

    public void setLru_clock(String lru_clock) {
        this.lru_clock = lru_clock;
    }
}
