
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.azure.management.resources.ResourceManagementClient;
import com.microsoft.azure.management.resources.ResourceManagementService;
import com.microsoft.azure.management.resources.models.ResourceGroupExtended;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.management.configuration.ManagementConfiguration;

import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {

    public static void main(String[] args) throws Exception {
        ResourceManagementClient client = Main.createResourceManagementClient();
        ArrayList<ResourceGroupExtended> groups = client.getResourceGroupsOperations().list(null).getResourceGroups();
        for (ResourceGroupExtended group : groups) {
            System.out.println(group.getName());
        }
    }

    protected static ResourceManagementClient createResourceManagementClient() throws Exception {
        Configuration config = createConfiguration();
        return ResourceManagementService.create(config);
    }

    public static Configuration createConfiguration() throws Exception {
        String baseUri = "https://management.azure.com";
        return ManagementConfiguration.configure(
                null,
                new URI(baseUri),
                System.getenv(ManagementConfiguration.SUBSCRIPTION_ID),
                getAccessTokenFromUserCredentials(System.getenv("arm.username"), System.getenv("arm.password")).getAccessToken());
    }

    private static AuthenticationResult getAccessTokenFromUserCredentials(
            String username, String password) throws Exception {
        AuthenticationContext context = null;
        AuthenticationResult result = null;
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(1);
            context = new AuthenticationContext("https://login.windows.net" + "arm.tenant", false, service);
            Future<AuthenticationResult> future = context.acquireToken(
                    System.getenv(ManagementConfiguration.URI), "arm.clientid", username, password, null);
            result = future.get();
        } finally {
            service.shutdown();
        }

        if (result == null) {
            throw new ServiceUnavailableException(
                    "authentication result was null");
        }
        return result;
    }
}
