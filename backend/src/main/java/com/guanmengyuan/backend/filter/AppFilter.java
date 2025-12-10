package com.guanmengyuan.backend.filter;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.exception.StatusException;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.ValidatorException;

@Component(index = 0) // index 为顺序位（不加，则默认为0）
public class AppFilter implements Filter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        try {
            chain.doFilter(ctx);
        } catch (ValidatorException e) {
            ctx.render(Result.failure(e.getCode(), e.getMessage())); // e.getResult().getDescription()
        } catch (StatusException e) {
            if (e.getCode() == 404) {
                ctx.status(e.getCode());
            } else {
                ctx.render(Result.failure(e.getCode(), e.getMessage()));
            }
        } catch (Throwable e) {
            ctx.status(500);
            ctx.render(Result.failure(500, e.getMessage()));
        }
    }
}