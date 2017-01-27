package bbm.actions.impl;

import bbm.actions.ActionResult;
import bbm.actions.SandboxSyncAction;
import bbm.actions.context.OrgActionContext;
import bbm.database.orgs.Org;
import bbm.database.orgs.Orgs;
import bbm.database.sandboxes.Sandbox;
import bbm.database.sandboxes.Sandboxes;
import bbm.salesforce.Api;
import com.google.inject.Inject;
import com.sforce.soap.tooling.GetUserInfoResult;
import com.sforce.soap.tooling.sobject.SandboxInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Blocking;

import java.util.*;

/**
 * Created by mario on 1/24/17.
 */
public class SandboxSyncSyncActionImpl implements SandboxSyncAction {

    private final Orgs orgs;
    private final Sandboxes sandboxes;
    private final Api sfApi;
    private final static Logger logger = LoggerFactory.getLogger(SandboxSyncSyncActionImpl.class);

    @Inject
    public SandboxSyncSyncActionImpl(Orgs orgs, Sandboxes sandboxes, Api sfApi){
        this.orgs = orgs;
        this.sandboxes = sandboxes;
        this.sfApi = sfApi;
    }

    @Override
    public void apply(OrgActionContext orgActionContext) {
        // Async stuff here
        Blocking.get(() -> sfApi.blockingPartnerLogin(orgActionContext.getUsername(),
                orgActionContext.getPassword(), orgActionContext.getToken()))
        .onError(throwable -> logger.error("SF PartnerConnection failed!", throwable))
        .map(loginResult -> sfApi.blockingToolingApiLogin(loginResult))
        .left(toolingConnection -> sfApi.blockingGetSandboxes(toolingConnection))
        .onError(throwable -> logger.error("SF ToolingConnection failed!", throwable))
        .map(pair -> {
            GetUserInfoResult userInfo = pair.getRight().getUserInfo();
            Org resultOrg = getOrCreateOrg(userInfo);

            List<Sandbox> resultSandboxes = new ArrayList();
            for(SandboxInfo info : pair.left()){
                logger.debug(info.getSandboxName());
                if(!info.getSandboxName().startsWith(orgActionContext.getSandboxPrefix())) {
                    continue;
                }
                logger.debug(info.getSandboxName() + " matches!");
                Optional<Sandbox> currentSandboxOptional = sandboxes.getSandbox(info.getId());
                if(currentSandboxOptional.isPresent()){
                    resultSandboxes.add(currentSandboxOptional.get());
                } else {
                    sandboxes.createSandbox(info.getSandboxName(), info.getId(), resultOrg);
                    resultSandboxes.add(sandboxes.getSandbox(info.getId()).get());
                }
            }
            return Pair.of(resultOrg, resultSandboxes);
        }).onError(throwable -> logger.error("Org / Sandbox creation failed after sync: ", throwable))
        .then(pair -> {
            Map<String, String> payload = new HashMap();
            payload.put("success", "true");
            payload.put("org", pair.getLeft().getSalesforceId());
            payload.put("sandboxes", String.valueOf(pair.getRight().size()));
            orgActionContext.getContext().render(getActionResult(payload, true));
        });
    }

    private Org getOrCreateOrg(GetUserInfoResult userInfo) {
        Org resultOrg;Optional<Org> orgOptional = orgs.getOrg(userInfo.getOrganizationId());
        if(orgOptional.isPresent()) {
            resultOrg = orgOptional.get();
        } else {
            resultOrg = orgs.createOrg(userInfo.getOrganizationId());
        }
        return resultOrg;
    }

    private ActionResult getActionResult(final Map<String, String> payLoad, final Boolean success) {
        return new ActionResult() {

            @Override
            public Boolean getSuccess() {
                return success;
            }

            @Override
            public Map<String, String> getPayload() {
                return payLoad;
            }
        };
    }
}
