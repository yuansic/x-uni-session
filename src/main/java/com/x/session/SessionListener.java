package com.x.session;


import com.x.session.impl.CacheHttpSession;

public interface SessionListener {
    void onAttributeChanged(CacheHttpSession paramRedisHttpSession);

    void onInvalidated(CacheHttpSession paramRedisHttpSession);
}
