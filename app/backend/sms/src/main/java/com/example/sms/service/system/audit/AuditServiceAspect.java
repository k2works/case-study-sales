package com.example.sms.service.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AuditServiceAspect {
    private final AuditService auditService;

    @Autowired
    public AuditServiceAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Around("@annotation(auditAnnotation)")
    public Object handleAuditAspect(ProceedingJoinPoint joinPoint, AuditAnnotation auditAnnotation) throws Throwable {
        ApplicationExecutionProcessType process = ApplicationExecutionProcessType.fromNameAndCode(auditAnnotation.process().getName(), auditAnnotation.process().getCode());
        ApplicationExecutionHistoryType type = ApplicationExecutionHistoryType.fromName(auditAnnotation.type().getName());
        ApplicationExecutionHistory audit = auditService.start(process, type);
        log.info("{}:{}を開始しました", audit.getProcessStart(), process.getName());
        try {
            Object result = joinPoint.proceed();
            audit = auditService.end(audit);
            log.info("{}:{}を終了しました", audit.getProcessEnd(), process.getName());
            return result;
        } catch (Throwable e) {
            auditService.error(audit, e.getMessage());
            log.error("{}:{}でエラーが発生しました", audit.getProcessEnd(), process.getName());
            throw e;
        }
    }
}
