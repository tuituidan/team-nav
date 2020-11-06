package com.tuituidan.teamnav.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * IpKit.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/11/6
 */
@UtilityClass
@Slf4j
public class IpKit {

    /**
     * 获取本机IP.
     *
     * @return IP
     */
    public static String getIpAddress() {
        try {
            InetAddress candidateAddress = null;
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = ifaces.nextElement();
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    if (inetAddr.isLoopbackAddress()) {
                        continue;
                    }
                    if (inetAddr.isSiteLocalAddress()) {
                        return inetAddr.getHostAddress();
                    }
                    if (candidateAddress == null) {
                        candidateAddress = inetAddr;
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("InetAddress.getLocalHost获取失败.");
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception ex) {
            log.error("获取本机IP失败", ex);
        }
        return "";
    }
}
