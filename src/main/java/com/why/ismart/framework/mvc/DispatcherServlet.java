package com.why.ismart.framework.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.why.ismart.framework.Loader;
import com.why.ismart.framework.config.Config;
import com.why.ismart.framework.ioc.BeanContext;
import com.why.ismart.framework.util.ArrayUtil;
import com.why.ismart.framework.util.JsonUtil;
import com.why.ismart.framework.util.ReflectUtil;
import com.why.ismart.framework.util.StringUtil;
import com.why.ismart.framework.util.UrlCoderUtil;

@SuppressWarnings("serial")
@WebServlet(urlPatterns="/*", loadOnStartup=0)
public class DispatcherServlet extends HttpServlet{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        Loader.init();
        ServletContext servletContext = servletConfig.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(Config.getAppJspPath()+"*");
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(Config.getAppAssetPath()+"*");
    }
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Request request = new Request(req);
        Handler handler = ControllerContext.getHandler(request);
        LOGGER.info("service "+request+" : "+handler);
        if(handler == null){
            String errMsg = "Can not found Handler for "+request;
            LOGGER.error(errMsg);
            throw new ServletException(errMsg);
        }
        Object controller = BeanContext.getBean(handler.getController());
        Method method = handler.getMethod();
        Map<String, Object> paramMap = parse(req);
        Object result = ReflectUtil.invokeMethod(controller, method, new Param(paramMap));
        process(result, req, resp);
    }
    
    private Map<String, Object> parse(HttpServletRequest req){
        Map<String, Object> map = new HashMap<String, Object>();
        parseParam(req, map);
        parseBody(req, map);
        return map;
    }
    
    private void parseParam(HttpServletRequest req, Map<String, Object> map){
        Enumeration<String> paramNames = req.getParameterNames();
        while(paramNames.hasMoreElements()){
            String paramName = paramNames.nextElement();
            String paramValue = req.getParameter(paramName);
            map.put(paramName, paramValue);
        }
    }
    
    private void parseBody(HttpServletRequest req, Map<String, Object> map){
        String body = UrlCoderUtil.decodeURL(readBody(req));
        if(StringUtil.isNotEmpty(body)){
            String[] params = StringUtil.splitString(body, "&");
            if(ArrayUtil.isNotEmpty(params)){
                for(String param:params){
                    String[] array = StringUtil.splitString(param, "=");
                    if(ArrayUtil.isNotEmpty(array)){
                        String paramName = array[0];
                        String paramValue = array[1];
                        map.put(paramName, paramValue);
                    }
                }
            }
        }
    }
    
    private String readBody(HttpServletRequest req){
        List<String> lines = Collections.emptyList();
        try {
            lines = IOUtils.readLines(req.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for(String line:lines){
            sb.append(line);
        }
        return sb.toString();
    }
    
    private void process(Object result, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        if(result instanceof View){
            processJsp((View)result, req, resp);
        }else if(result instanceof Data){
            processJson((Data)result, req, resp);
        }else{
            System.out.println("Unknown result!");
        }
    }
    
    private void processJsp(View view, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String path = view.getPath();
        if(path.startsWith("/")){
            resp.sendRedirect(req.getContextPath()+path);
        }else{
            Map<String, Object> model = view.getModel();
            for(Map.Entry<String, Object> entry:model.entrySet()){
                req.setAttribute(entry.getKey(), entry.getValue());
            }
            req.getRequestDispatcher(Config.getAppJspPath()+path).forward(req, resp);
        }
    }
    
    private void processJson(Data data, HttpServletRequest req, HttpServletResponse resp) throws IOException{
        Object model = data.getModel();
        if(model != null){
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter writer = resp.getWriter();
            String json = JsonUtil.toJson(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
    
}
