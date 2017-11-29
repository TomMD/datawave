package nsa.datawave.query.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import nsa.datawave.query.language.parser.ParseException;
import nsa.datawave.query.rewrite.tables.RefactoredShardQueryLogic;
import nsa.datawave.security.authorization.DatawavePrincipal;
import nsa.datawave.security.authorization.DatawavePrincipalLookup;
import nsa.datawave.webservice.query.Query;
import nsa.datawave.webservice.query.configuration.GenericQueryConfiguration;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.security.Authorizations;

/**
 * Extension to the query logic that enforces the current user is equal to the USER field in the QueryMetrics <br />
 * <p>
 * QLF.xml entry: <br />
 * 
 * <pre>
 *  &lt;bean id="QueryMetricsQuery" scope="prototype"  parent="BaseEventQuery" class="nsa.datawave.query.metrics.QueryMetricsQueryLogic"&gt;
 *      &lt;property name="logicDescription" value="Query Metrics query for users" /&gt;
 *      &lt;property name="includeHierarchyFields" value="false" /&gt;
 *      &lt;property name="modelTableName" value="QueryMetrics_m" /&gt;
 *      &lt;property name="modelName" value="NONE" /&gt;
 *      &lt;property name="tableName" value="QueryMetrics_e" /&gt;
 *      &lt;property name="metadataTableName" value="QueryMetrics_m" /&gt;
 *      &lt;property name="indexTableName" value="QueryMetrics_i" /&gt;
 *      &lt;property name="reverseIndexTableName" value="QueryMetrics_r" /&gt;
 *      &lt;property name="maxValueExpansionThreshold" value="1500" /&gt;
 *      &lt;property name="auditType" value="NONE" /&gt;
 *      &lt;property name="collectQueryMetrics" value="false" /&gt;
 *  &lt;/bean&gt;
 * </pre>
 * 
 * <br />
 */
public class QueryMetricQueryLogic extends RefactoredShardQueryLogic {
    
    @Inject
    private DatawavePrincipalLookup datawavePrincipalLookup;
    
    private List<String> roles = null;
    
    public void setRolesSets(List<String> roleSets) {
        this.roles = roleSets;
    }
    
    public QueryMetricQueryLogic() {
        super();
    }
    
    public QueryMetricQueryLogic(QueryMetricQueryLogic other) {
        super(other);
        datawavePrincipalLookup = other.datawavePrincipalLookup;
        if (other.roles != null) {
            roles = new ArrayList<>();
            roles.addAll(other.roles);
        }
    }
    
    @Override
    public QueryMetricQueryLogic clone() {
        return new QueryMetricQueryLogic(this);
    }
    
    @Override
    public final GenericQueryConfiguration initialize(Connector connection, Query settings, Set<Authorizations> auths) throws Exception {
        return super.initialize(connection, settings, auths);
    }
    
    @Override
    public final String getJexlQueryString(Query settings) throws ParseException {
        
        if (null == this.roles) {
            DatawavePrincipal userPrincipal = datawavePrincipalLookup.getCurrentPrincipal();
            this.roles = userPrincipal.getRoleSets();
        }
        
        String query = super.getJexlQueryString(settings);
        if (this.roles.contains("MetricsAdministrator")) {
            return query;
        }
        
        StringBuilder jexl = new StringBuilder();
        if (query.length() > 0) {
            jexl.append("(").append(query).append(")");
            jexl.append(" AND (USER == '").append(settings.getOwner()).append("')");
        } else {
            jexl.append("USER == '").append(settings.getOwner()).append("'");
        }
        return jexl.toString();
    }
}