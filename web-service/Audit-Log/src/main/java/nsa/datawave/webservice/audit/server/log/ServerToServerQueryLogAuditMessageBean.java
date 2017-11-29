package nsa.datawave.webservice.audit.server.log;

import javax.annotation.security.RunAs;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.MessageListener;

import nsa.datawave.webservice.common.audit.AuditReceiverBase;
import nsa.datawave.webservice.common.audit.Auditor;

@RunAs("InternalUser")
@MessageDriven(name = "ServerToServerQueryLogAuditMessageBean", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/Audit.Log"),
        @ActivationConfigProperty(propertyName = "useLocalTx", propertyValue = "true"),
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "ServerToServerQueryLogAuditMessageBean"),
        @ActivationConfigProperty(propertyName = "user", propertyValue = "${dw.hornetq.system.userName}"),
        @ActivationConfigProperty(propertyName = "password", propertyValue = "${dw.hornetq.system.password}"),
        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "ID:ServerToServerQueryLogAuditMessageBean"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "${dw.audit.log.mdb.pool.size}")})
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
public class ServerToServerQueryLogAuditMessageBean extends AuditReceiverBase implements MessageListener {
    @Inject
    private ServerToServerQueryLogAuditBean auditBean;
    
    @Override
    protected Auditor getAuditor() {
        return auditBean;
    }
}