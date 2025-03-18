package org.bigmouth.gpt.ai;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bigmouth.gpt.ai.entity.ChatServiceArgument;
import org.bigmouth.gpt.entity.AiModel;
import org.bigmouth.gpt.exceptions.AiException;
import org.springframework.context.annotation.Configuration;

/**
 * @author huxiao
 * @date 2023/11/20
 * @since 1.0.0
 */
@Aspect
@Configuration
public class ChatServiceChatInterceptor {


    @Around("execution(* org.bigmouth.gpt.ai.ChatService.chat(org.bigmouth.gpt.ai.entity.ChatServiceArgument))")
    public Object interceptChatMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object arg = args[0];
        if (!(arg instanceof ChatServiceArgument)) {
            return joinPoint.proceed();
        }
        ChatServiceArgument argument = (ChatServiceArgument) arg;
        AiModel aiModel = argument.getAiModel();

        long start = System.nanoTime();
        try {
            Object proceed = joinPoint.proceed();
            return proceed;
        } catch (Throwable ex) {
            this.handleThrowable(aiModel, ex);
            throw ex;
        } finally {
            // 统计模型请求次数
        }
    }

    private void handleThrowable(AiModel aiModel, Throwable ex) {
        if (ex instanceof AiException) {
        }
    }
}
