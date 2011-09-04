/*
 * Copyright 2011 NTUU "KPI".
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.kpi.griddl.ws;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebMethod;
import ua.kpi.griddl.core.infrastructure.ServerStartupManager;
import ua.kpi.griddl.core.infrastructure.reasoner.QueryTask;
import ua.kpi.griddl.core.infrastructure.reasoner.ReasonerManager;
import ua.kpi.griddl.core.infrastructure.tbox.TBoxManager;

/**
 *
 * @author Pospishniy Olexandr <pospishniy@kpi.in.ua>
 */
@WebService(serviceName = "ServiceFacade")
public class ServiceFacade {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getTbox")
    public String getTbox() {
        return TBoxManager.getInstance().getTBoxStr();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getTboxVersion")
    public String getTboxVersion() {
        return TBoxManager.getInstance().getTBoxVersionStr();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "submitQuery")
    public Long submitQuery(@WebParam(name = "userQuery") WSUserQuery userQuery) throws WSEception {
        if (ServerStartupManager.getStatus().equals(ServerStartupManager.ServerStatus.READY)) {
            return ReasonerManager.getInstance().submitQuery(userQuery);
        } else {
            throw new WSEception("Server is not ready");
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getQueryTask")
    public WSQueryTask getQueryTask(@WebParam(name = "id") final Long id) throws WSEception {
        if (ServerStartupManager.getStatus().equals(ServerStartupManager.ServerStatus.READY)) {
            QueryTask queryTask = ReasonerManager.getInstance().getQueryTask(id);
            if (queryTask == null) {
                throw new WSEception("No such query task registered with id: " + id);
            } else {
                return queryTask.toWSQueryTask();
            }
        } else {
            throw new WSEception("Server is not ready");
        }
    }
}