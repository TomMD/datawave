package datawave.microservice.audit.common;

import datawave.microservice.audit.common.Auditor.AuditType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class AuditMessageHandler {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    public static String LISTENER_METHOD = "onMessage";
    
    private AuditParameters auditParameters;
    
    private Auditor auditor;
    
    public AuditMessageHandler(AuditParameters auditParameters, Auditor auditor) {
        this.auditParameters = auditParameters;
        this.auditor = auditor;
    }
    
    public void onMessage(Map<String,Object> msg) {
        try {
            auditParameters.clear();
            AuditParameters ap = auditParameters.fromMap(msg);
            if (!ap.getAuditType().equals(AuditType.NONE)) {
                auditor.audit(ap);
            }
        } catch (Exception e) {
            log.error("Error processing audit message: " + e.getMessage());
        }
    }
}
