package hackaton.fastdisision.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.data.VotingDTO;
import hackaton.fastdisision.repo.UserRepo;
import hackaton.fastdisision.repo.VotingRepo;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Daniil Dmitrochenkov
 * @version 1.3
 */
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:create-user-before.sql", "classpath:create-votings-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:create-votings-after.sql", "classpath:create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class VotingControllerTest extends BasicTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private VotingRepo votingRepo;

    @Autowired
    private MockMvc mockMvc;

    private String rightProtectedVotingKey = "notPublic";
    private String wrongProtectedVotingKey = "wrongKey";

    private String votingsOwnerUserId = "3";
    private String adminUserId = "1";
    private String otherCommonUserId = "4";

    private long commonVotingId = 1;
    private long protectedVotingId = 6;
    private long notExistingVotingId = 999;
    private long firsDeletedVotingId = 1;

    private long secDeletedVotingId = 2;
    private long thDeletedVotingId = 3;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void getPublicVotingById() throws Exception {
        User votingOwner = userRepo.findById(votingsOwnerUserId).get();

        MvcResult mvcResult = mockMvc.perform(get("/voteApi/votings/{id}", commonVotingId))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(parameterWithName("id").description("ID of requested voting.")),
                        responseFields(
                                fieldWithPath("id").description("ID of requested voting."),
                                fieldWithPath("totalVotes").description("totalVotes of requested voting."),
                                fieldWithPath("votingTitle").description("votingTitle of requested voting."),
                                fieldWithPath("totalVotes").description("totalVotes of requested voting."),
                                fieldWithPath("isProtectedVoting").description("Is requested voting protected by key."),
                                fieldWithPath("owner").description("requested voting owner."),
                                fieldWithPath("owner.id").description("ID of requested voting owner."),
                                fieldWithPath("owner.username").description("username of requested voting owner."),
                                fieldWithPath("owner.roles").description("roles of requested voting owner."),
                                fieldWithPath("votingOptions").description("Array of voting options."),
                                fieldWithPath("votingOptions[].id").description("voting option ID."),
                                fieldWithPath("votingOptions[].voteDiscription").description("voting option discription."),
                                fieldWithPath("votingOptions[].pluses").description("voting option pluses.")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commonVotingId))
                .andExpect(jsonPath("$.creationDate").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.isPrivateVoting").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.isCheckingIpVoting").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.votedIps").doesNotHaveJsonPath())
                .andReturn();

        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        User user = mapper.readValue(responseObj.get("owner").toString(), User.class);

        assertEquals(user, votingOwner);
    }

    @Test
    void getPublicVotingByIdWithWrongKeyByOwner() throws Exception {
        User votingOwner = userRepo.findById(votingsOwnerUserId).get();
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/votings/{id}", protectedVotingId)
                .with(user(votingOwner)).param("votingKey", wrongProtectedVotingKey))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(protectedVotingId))
                .andExpect(jsonPath("$.creationDate").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.isPrivateVoting").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.isCheckingIpVoting").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.votedIps").doesNotHaveJsonPath())
                .andReturn();

        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        User user = mapper.readValue(responseObj.get("owner").toString(), User.class);

        assertEquals(user, votingOwner);
    }

    @Test
    void getProtectedVotingById() throws Exception {
        User votingOwner = userRepo.findById(votingsOwnerUserId).get();

        MvcResult mvcResult = mockMvc.perform(get("/voteApi/votings/{id}", protectedVotingId).param("votingKey", rightProtectedVotingKey))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(
                                parameterWithName("id").description("ID of requested voting.")),
                        requestParameters(
                                parameterWithName("votingKey").description("protected voting key")),
                        responseFields(
                                fieldWithPath("id").description("ID of requested voting."),
                                fieldWithPath("totalVotes").description("totalVotes of requested voting."),
                                fieldWithPath("votingTitle").description("votingTitle of requested voting."),
                                fieldWithPath("totalVotes").description("totalVotes of requested voting."),
                                fieldWithPath("isProtectedVoting").description("Is requested voting protected by key."),
                                fieldWithPath("owner").description("requested voting owner."),
                                fieldWithPath("owner.id").description("ID of requested voting owner."),
                                fieldWithPath("owner.username").description("username of requested voting owner."),
                                fieldWithPath("owner.roles").description("roles of requested voting owner."),
                                fieldWithPath("votingOptions").description("Array of voting options."),
                                fieldWithPath("votingOptions[].id").description("voting option ID."),
                                fieldWithPath("votingOptions[].voteDiscription").description("voting option discription."),
                                fieldWithPath("votingOptions[].pluses").description("voting option pluses.")
                        )
                ))
                .andExpect(jsonPath("$.id").value(protectedVotingId))
                .andExpect(jsonPath("$.votingKey").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.creationDate").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.isPrivateVoting").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.isCheckingIpVoting").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.votedIps").doesNotHaveJsonPath())
                .andReturn();

        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        User user = mapper.readValue(responseObj.get("owner").toString(), User.class);

        assertEquals(protectedVotingId, (int) responseObj.get("id"), "voting id in request not compared to id in result!");
        assertEquals(user, votingOwner);
    }

    @Test
    void getProtectedVotingByIdWrongKey() throws Exception {
        mockMvc.perform(get("/voteApi/votings/{id}", protectedVotingId).param("votingKey", wrongProtectedVotingKey))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getVotingByIdNotFound() throws Exception {
        mockMvc.perform(get("/voteApi/votings/{id}", notExistingVotingId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void addVoting() throws Exception {

        Map<String, Object> requestVoting = getAddVotingRequestBody();
        List<HashMap<String, Object>> requestVotingOptions = (List<HashMap<String, Object>>) requestVoting.get("votingOptions");
        ObjectMapper om = new ObjectMapper();

        MvcResult mvcResult = mockMvc.perform(post("/voteApi/votings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(requestVoting)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{ClassName}/{methodName}",
                        responseFields(
                                fieldWithPath("id").description("New voting ID."),
                                fieldWithPath("totalVotes").description("Total voting votes."),
                                fieldWithPath("votingTitle").description("Voting title."),
                                fieldWithPath("votingKey").description("Voting protect key."),
                                fieldWithPath("isProtectedVoting").description("Is voting protected."),
                                fieldWithPath("owner").type(User.class).description("voting owner."),
                                fieldWithPath("votingOptions").description("Voting options."),
                                fieldWithPath("votingOptions[].id").description("Option ID."),
                                fieldWithPath("votingOptions[].voteDiscription").description("Option discription."),
                                fieldWithPath("votingOptions[].pluses").description("Option pluses(votes)."))
                ))
                .andExpect(jsonPath("$.votingTitle").value(requestVoting.get("votingTitle")))
                .andExpect(jsonPath("$.isProtectedVoting").value(requestVoting.get("isProtectedVoting")))
                .andExpect(jsonPath("$.votingOptions[0].voteDiscription").value(requestVotingOptions.get(0).get("voteDiscription")))
                .andExpect(jsonPath("$.votingOptions[1].voteDiscription").value(requestVotingOptions.get(1).get("voteDiscription")))
                .andExpect(jsonPath("$.votedIps").doesNotHaveJsonPath())
                .andReturn();

        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        VotingDTO voting = mapper.readValue(responseObj.toString(), VotingDTO.class);

        Optional<Voting> votingOnServer = votingRepo.findById(voting.getId());

        //check correct save in db
        assertTrue(votingOnServer.isPresent(), "Voting was not saved on server!");
        assertEquals(votingOnServer.get().getVotingTitle(), requestVoting.get("votingTitle"), "Wrong server voting title!");
        assertEquals(votingOnServer.get().isProtectedVoting(), requestVoting.get("isProtectedVoting"), "Wrong server 'is protected voting'!");
        assertEquals(votingOnServer.get().isPrivateVoting(), requestVoting.get("isPrivateVoting"), "Wrong server 'is private voting'!");
        assertEquals(votingOnServer.get().isCheckingIpVoting(), requestVoting.get("isCheckingIpVoting"), "Wrong server 'is checking IP voting'!");
        assertEquals(votingOnServer.get().getVotingOptions().get(0).getVoteDiscription(), requestVotingOptions.get(0).get("voteDiscription"), "Wrong server first 'voting option disc'!");
        assertEquals(votingOnServer.get().getVotingOptions().get(1).getVoteDiscription(), requestVotingOptions.get(1).get("voteDiscription"), "Wrong server sec 'voting option disc'!");
    }

    @Test
    void addVotingWithoutTitle() throws Exception {

        Map<String, Object> requestVoting = getAddVotingRequestBody();
        requestVoting.put("votingTitle", null);

        ObjectMapper om = new ObjectMapper();

        mockMvc.perform(post("/voteApi/votings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(requestVoting)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addVotingWithoutOptions() throws Exception {

        Map<String, Object> requestVoting = getAddVotingRequestBody();
        requestVoting.put("votingOptions", null);

        ObjectMapper om = new ObjectMapper();

        mockMvc.perform(post("/voteApi/votings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(requestVoting)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addVotingWithoutOptionDiscription() throws Exception {

        Map<String, Object> requestVoting = getAddVotingRequestBody();

        List<HashMap<String, Object>> options = (List<HashMap<String, Object>>) requestVoting.get("votingOptions");
        options.get(0).put("voteDiscription", null);
        options.get(1).put("voteDiscription", null);

        ObjectMapper om = new ObjectMapper();

        mockMvc.perform(post("/voteApi/votings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(requestVoting)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void validatePublicVotingKey() throws Exception {
        MvcResult votingKeyIsValidResponse = mockMvc.perform(get("/voteApi/votings/{id}/validation/key", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(
                                parameterWithName("id").description("ID of validated voting.")),
                        responseFields(
                                fieldWithPath("keyIsValid").description("Key is valid."))
                ))
                .andReturn();

        JSONObject responseObj = new JSONObject(votingKeyIsValidResponse.getResponse().getContentAsString());

        assertTrue((Boolean) responseObj.get("keyIsValid"), "voting key is invalid!");
    }

    @Test
    void validateProtectedVotingKey() throws Exception {
        MvcResult votingKeyIsValidResponse = mockMvc.perform(get("/voteApi/votings/{id}/validation/key", protectedVotingId)
                .param("votingKey", rightProtectedVotingKey))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject responseObj = new JSONObject(votingKeyIsValidResponse.getResponse().getContentAsString());
        assertTrue((Boolean) responseObj.get("keyIsValid"), "voting key is invalid!");
    }

    @Test
    void validateVotingKeyFailed() throws Exception {
        MvcResult votingKeyIsValidResponse = mockMvc.perform(get("/voteApi/votings/{id}/validation/key", protectedVotingId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertFalse(Boolean.parseBoolean(votingKeyIsValidResponse.getResponse().getContentAsString()), "voting key is valid!");
    }

    @Test
    void validateVotingKeyNotFound() throws Exception {
        mockMvc.perform(get("/voteApi/votings/{id}/validation/key", notExistingVotingId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVoting() throws Exception {
        User votingOwner = userRepo.findById(votingsOwnerUserId).get();
        mockMvc.perform(delete("/voteApi/votings/{id}", firsDeletedVotingId).with(user(votingOwner)))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(
                                parameterWithName("id").description("ID of requested voting."))
                ))
                .andExpect(status().isOk());

        mockMvc.perform(get("/voteApi/votings/{id}", firsDeletedVotingId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVotingByAdmin() throws Exception {
        User adminUser = userRepo.findById(adminUserId).get();

        mockMvc.perform(delete("/voteApi/votings/{id}", secDeletedVotingId).with(user(adminUser)))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/voteApi/votings/{id}", secDeletedVotingId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVotingFailed() throws Exception {
        User otherUser = userRepo.findById(otherCommonUserId).get();

        mockMvc.perform(delete("/voteApi/votings/{id}", thDeletedVotingId).with(user(otherUser)))
                .andDo(print())
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/voteApi/votings/{id}", thDeletedVotingId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Map<String, Object> getAddVotingRequestBody() {

        Map<String, Object> request = new HashMap<>();
        request.put("id", null);
        request.put("votingTitle", "title");
        request.put("isProtectedVoting", true);
        request.put("isPrivateVoting", false);
        request.put("isCheckingIpVoting", false);

        Map<String, Object> requestVotingOption1 = new HashMap<>();
        requestVotingOption1.put("id", null);
        requestVotingOption1.put("voteDiscription", "disc1");

        Map<String, Object> requestVotingOption2 = new HashMap<>();
        requestVotingOption2.put("id", null);
        requestVotingOption2.put("voteDiscription", "disc2");

        request.put("votingOptions", (Arrays.asList(requestVotingOption1, requestVotingOption2)));

        return request;
    }
}