package com.nj.nedis.config;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.omg.PortableInterceptor.Interceptor;

public class ServerInfo {

	private static  String version = "2.5.9";

	private static  String gitSha1 = "473f3090";

	private static  String gitDirty = "473f3090";

	private static  String os = "Linux 3.3.7-ARCH i686";

	private static  String arch_bits = "64";

	private static  String multiplexingApi = "epoll";

	private static  String gccVersion = "4.7.0";

	private static  String processId = "8104";//todo

	private static  String tcpPort = "6378";

	private static String lru_clock = "1680564";

	public static String getInfo() {

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo( ServerInfo.class );
			StringBuffer result = new StringBuffer( 128 );

			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

			result.append( "*" + pds.length + "\r\n" );
			for( PropertyDescriptor pd : pds ) {
//				return
			}


		} catch( IntrospectionException e ) {
			return "-ERR get info error\r\n";
		}

	}

	public static String getVersion() {
		return version;
	}

	public static void setVersion( String version ) {
		ServerInfo.version = version;
	}

	public static String getGitSha1() {
		return gitSha1;
	}

	public static void setGitSha1( String gitSha1 ) {
		ServerInfo.gitSha1 = gitSha1;
	}

	public static String getGitDirty() {
		return gitDirty;
	}

	public static void setGitDirty( String gitDirty ) {
		ServerInfo.gitDirty = gitDirty;
	}

	public static String getOs() {
		return os;
	}

	public static void setOs( String os ) {
		ServerInfo.os = os;
	}

	public static String getArch_bits() {
		return arch_bits;
	}

	public static void setArch_bits( String arch_bits ) {
		ServerInfo.arch_bits = arch_bits;
	}

	public static String getMultiplexingApi() {
		return multiplexingApi;
	}

	public static void setMultiplexingApi( String multiplexingApi ) {
		ServerInfo.multiplexingApi = multiplexingApi;
	}

	public static String getGccVersion() {
		return gccVersion;
	}

	public static void setGccVersion( String gccVersion ) {
		ServerInfo.gccVersion = gccVersion;
	}

	public static String getProcessId() {
		return processId;
	}

	public static void setProcessId( String processId ) {
		ServerInfo.processId = processId;
	}

	public static String getTcpPort() {
		return tcpPort;
	}

	public static void setTcpPort( String tcpPort ) {
		ServerInfo.tcpPort = tcpPort;
	}

	public static String getLru_clock() {
		return lru_clock;
	}

	public static void setLru_clock( String lru_clock ) {
		ServerInfo.lru_clock = lru_clock;
	}
}
