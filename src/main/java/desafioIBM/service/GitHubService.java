package desafioIBM.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;

@Service
public class GitHubService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public String findReposotoryByUser (String userGit) throws Exception {
        return response (userGit);
    }

    private String response (String userGit) {

        final String strURL = String.format("https://api.github.com/users/%s/repos", userGit);

        try {
            ClientResponse response = connectGitAPI(strURL);
            assert response != null;
            int statusCode = response.getStatus();

            log.info("\n StatusCode = " + statusCode);

            if (statusCode == HttpStatus.OK.value())
                return response.getEntity(String.class);
            else
                return "";
        } catch (Exception exception) {
            return "";
        }
    }

    private ClientResponse connectGitAPI (String url)  {

        ClientResponse response = null;

        try {
            Client client = Client.create();
            WebResource webResource = client.resource(url);
            response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE)
                    .get(ClientResponse.class);

            return response;
        } catch (Exception exception) {
            log.error("Error in connect URL: " + url + " . Cause: " + exception.getMessage());
            return null;
        }
    }
}
