package com.nj.nedis.config;

import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

public class ServerInfo {

    private  String version = "2.5.9";

    private  String gitSha1 = "473f3090";

    private  String gitDirty = "473f3090";

    private  String os = "Linux 3.3.7-ARCH i686";

    private  String arch_bits = "64";

    private  String multiplexingApi = "epoll";

    private  String gccVersion = "4.7.0";

    private  String processId = "8104";//todo

    private  String tcpPort = "6378";

    private  String lru_clock = "1680564";

    public static String getInfo(ServerInfo info) {

        try {

            Map<String, String> result = new HashMap<String, String>();
            result = BeanUtils.describe(info);

            StringBuffer sb = new StringBuffer(1024);
            sb.append("*");
            sb.append(result.size());
            sb.append("\r\n");

            for (Map.Entry<String, String> entry : result.entrySet()) {
                StringBuffer tmp = new StringBuffer(16);
                tmp.append(entry.getKey());
                tmp.append(":");
                tmp.append(entry.getValue());

                sb.append("$");
                sb.append(tmp.length());
                sb.append("\r\n");

                sb.append(tmp.toString());
                sb.append("\r\n");
            }
            System.out.print(sb.toString().trim());

            return sb.toString().trim();

        } catch (Exception e) {
            return "-ERR get info error\r\n";
        }

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGitSha1() {
        return gitSha1;
    }

    public void setGitSha1(String gitSha1) {
        this.gitSha1 = gitSha1;
    }

    public String getGitDirty() {
        return gitDirty;
    }

    public void setGitDirty(String gitDirty) {
        this.gitDirty = gitDirty;
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

    public String getMultiplexingApi() {
        return multiplexingApi;
    }

    public void setMultiplexingApi(String multiplexingApi) {
        this.multiplexingApi = multiplexingApi;
    }

    public String getGccVersion() {
        return gccVersion;
    }

    public void setGccVersion(String gccVersion) {
        this.gccVersion = gccVersion;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(String tcpPort) {
        this.tcpPort = tcpPort;
    }

    public String getLru_clock() {
        return lru_clock;
    }

    public void setLru_clock(String lru_clock) {
        this.lru_clock = lru_clock;
    }

    public static void main(String[] args) {
        ServerInfo.getInfo(new ServerInfo());
    }
}
