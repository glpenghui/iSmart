package com.why.ismart.framework.mvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Request {

    private final String method;
    private final String path;
    
    public Request(String method, String path) {
        this.method = method;
        this.path = path;
    }
    
    public Request(HttpServletRequest req) {
        this(req.getMethod().toLowerCase(), req.getPathInfo());
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return "Request [method=" + method + ", path=" + path + "]";
    }
    
}
