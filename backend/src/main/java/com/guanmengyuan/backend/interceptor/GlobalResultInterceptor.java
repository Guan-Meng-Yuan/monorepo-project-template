package com.guanmengyuan.backend.interceptor;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.core.handle.Result;
import org.noear.solon.core.route.RouterInterceptor;
import org.noear.solon.core.route.RouterInterceptorChain;

@Component
public class GlobalResultInterceptor implements RouterInterceptor {
   
    @Override
    public void doIntercept(Context ctx, Handler mainHandler, RouterInterceptorChain chain) throws Throwable {
        //提示：这里和 doFilter 差不多...
        chain.doIntercept(ctx, mainHandler);
    }

     /**
     * 提交结果（ render 执行前调用）//不要做太复杂的事情
     */
    @Override
    public Object postResult(Context ctx, Object result) throws Throwable {
        if(result instanceof Throwable){
            return result;
        }else{
            return Result.succeed(result);
        }
    }
}