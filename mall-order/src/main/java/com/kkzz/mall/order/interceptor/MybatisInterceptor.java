//package com.kkzz.mall.order.interceptor;
//
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.plugin.Intercepts;
//import org.apache.ibatis.plugin.Invocation;
//import org.apache.ibatis.plugin.Signature;
//import org.springframework.stereotype.Component;
//
//
//@Intercepts({
//        @Signature(
//                type = Executor.class,
//                method = "select",
//                args = {MappedStatement.class,Object.class}
//        )
//})
//@Component
//public class MybatisInterceptor implements Interceptor {
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object proceed = invocation.proceed();
//        System.out.println("这是插件测试");
//        return proceed;
//    }
//}
