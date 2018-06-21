package com.x.session.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.sdk.mcs.MCSClientFactory;
import com.x.sdk.mcs.interfaces.ICacheClient;



/**
 * redis的客户端实现
 */
public class SessionClient {
    private static final Logger LOG = LoggerFactory.getLogger(SessionClient.class);

    public static ICacheClient getCacheClient() {
    	return MCSClientFactory.getCacheClient("com.bonc.uni.session");
    }

    public void addItem(String key, Object object, int seconds) {
    	long t1=System.currentTimeMillis();   
    	LOG.debug("SessionClient。addItem ：key="+key+"begin");
    	getCacheClient().setex(key.getBytes(), seconds, serialize(object));
    	long t2=System.currentTimeMillis()-t1;   
    	LOG.debug("SessionClient。addItem ：key="+key+"end .{ getCacheClient().setex cost time: " +t2);
    }

    private static byte[] serialize(Object object) {
        if (object == null)
            return null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream objectOutput = null;
        try {
            baos = new ByteArrayOutputStream();
            objectOutput = new ObjectOutputStream(baos);
            objectOutput.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos)
                    baos.close() ;
                if (null != objectOutput) {
                    objectOutput = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public long delItem(String key) {
    	LOG.debug("SessionClient。delItem ：key="+key+"begin");
    	return	getCacheClient().del(key.getBytes());
    }

    /**
     * 统一session使用
     *
     * @param key
     * @return
     */
    public Object getSession(String key) {
        byte[] data = null;
        long t1=System.currentTimeMillis();   
    	LOG.debug("SessionClient。getSession ：key="+key+"begin");
        data = getCacheClient().get(key.getBytes());
        long t2=System.currentTimeMillis()-t1;   
        LOG.debug("SessionClient。getSession ：key="+key+"end .{ getCacheClient().hget cost time: " +t2);
        return deserialize(data);
    }

    private static Object deserialize(byte[] bytes) {
        if (bytes == null)
            return null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bais)
                    bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
