package com.logical.router.context;


import com.logical.router.LogicalConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaojiandong
 * @date 17/10/1
 */
public class LogicalRouterContext {


    private static LogicalRouterContextHolder contextHolder = new DefaultRouterContextHolder();

    private static Map<String, String> localLogicalRouterMap = new HashMap();

    static {
        if (null != System.getenv(LogicalConstants.LOGICAL_ROUTER_ENV)) {
            localLogicalRouterMap.put(LogicalConstants.LOGICAL_ROUTER_NAME, System.getenv().get(LogicalConstants.LOGICAL_ROUTER_ENV));
        }
    }

    public static void init() {

    }

    /**
     * 1. 首先从上下文中获取
     * 2. 其次从localmap获取
     *
     * @return
     */
    public static Map getInvocationLogicalRouterContext() {
        Map<String, String> context = contextHolder.getHolder().get().getContext();
        if (context != null & context.size() > 0) {
            return context;
        }
        if (localLogicalRouterMap.size() > 0) {
            return new HashMap<>(localLogicalRouterMap);
        }
        return null;
    }

    public static String getLocalLogicalRouterName() {
        if (localLogicalRouterMap.containsKey(LogicalConstants.LOGICAL_ROUTER_NAME)) {
            return localLogicalRouterMap.get(LogicalConstants.LOGICAL_ROUTER_NAME);
        }
        return null;
    }

    public static String getInvocationLogicalRouterName() {
        Map<String, String> map = getInvocationLogicalRouterContext();
        if (map != null && map.containsKey(LogicalConstants.LOGICAL_ROUTER_NAME)) {
            return map.get(LogicalConstants.LOGICAL_ROUTER_NAME);
        }
        return null;
    }

    public static String getLogicanRouterName(Map<String, String> otherMap) {
        if (otherMap == null || otherMap.size() == 0) {
            return null;
        }
        return otherMap.get(LogicalConstants.LOGICAL_ROUTER_NAME);
    }

    public static void removeLogicanRouterContext() {
        contextHolder.getHolder().remove();
    }

    public static void setLogicanRouterContext(Map<String, String> map) {
        contextHolder.getHolder().get().setContext(map);
    }


    static class DefaultRouterContextHolder implements LogicalRouterContextHolder {

        private final Map<String, String> attachments = new HashMap();

        private static final ThreadLocal<DefaultRouterContextHolder> LOCAL = new ThreadLocal<DefaultRouterContextHolder>() {
            @Override
            protected DefaultRouterContextHolder initialValue() {
                return new DefaultRouterContextHolder();
            }
        };

        @Override
        public ThreadLocal<DefaultRouterContextHolder> getHolder() {
            return LOCAL;
        }

        @Override
        public Map getContext() {
            return attachments;
        }

        @Override
        public void setContext(Map<String, String> contextMap) {
            if (contextMap == null || contextMap.size() == 0) {
                return;
            }
            attachments.putAll(contextMap);
        }
    }


    public interface LogicalRouterContextHolder {
        ThreadLocal<DefaultRouterContextHolder> getHolder();

        Map getContext();

        void setContext(Map<String, String> contextMap);
    }

}
