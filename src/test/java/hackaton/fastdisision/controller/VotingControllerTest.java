package hackaton.fastdisision.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.repo.UserRepo;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Sql(scripts = {"classpath:create-user-before.sql", "classpath:create-votings-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:create-votings-after.sql", "classpath:create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
// add voting (post mapping) is absent because always return HttpMessageNotReadableException
class VotingControllerTest extends BasicTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MockMvc mockMvc;

    @Value("${voting.public.key}")
    private String publicVotingKey;

    private String rightProtectedVotingKey = "notPublic";

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void getPublicVotingById() throws Exception {
        //user with id = 3 is owner of all votings (see SQL)
        User votingOwner = userRepo.findById("3").get();
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/votings/{id}", 1)) //default votingKey value = publicVotingKey
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(parameterWithName("id")
                                .description("ID of requested voting.")),
                        responseFields(
                                fieldWithPath("id")
                                        .description("ID of requested voting."),
                                fieldWithPath("totalVotes")
                                        .description("totalVotes of requested voting."),
                                fieldWithPath("votingTitle")
                                        .description("votingTitle of requested voting."),
                                fieldWithPath("totalVotes")
                                        .description("totalVotes of requested voting."),
                                fieldWithPath("isProtectedVoting")
                                        .description("Is requested voting protected by key."),
                                fieldWithPath("owner")
                                        .description("requested voting creator."),
                                fieldWithPath("owner.id")
                                        .description("ID of requested voting creator."),
                                fieldWithPath("owner.username")
                                        .description("username of requested voting creator."),
                                fieldWithPath("owner.roles")
                                        .description("roles of requested voting creator.")
                        )
                ))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        User user = mapper.readValue(responseObj.get("owner").toString(), User.class);
        assertTrue((int) responseObj.get("id") == 1, "voting id in request not compared to id in result!");
        assertEquals(user, votingOwner);
        assertFalse(responseObj.has("creationDate"));
        assertFalse(responseObj.has("isPrivateVoting"));
        assertFalse(responseObj.has("votedIps"));

    }

    @Test
    void getPublicVotingByIdWithWrongKeyByOwner() throws Exception {
        //user with id = 3 is owner of all votings (see SQL)
        User votingOwner = userRepo.findById("3").get();
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/votings/{id}", 6)
                .with(user(votingOwner)).param("votingKey", "wrongKey"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        User user = mapper.readValue(responseObj.get("owner").toString(), User.class);
        assertEquals(user, votingOwner);
        assertFalse(responseObj.has("creationDate"));
        assertFalse(responseObj.has("isPrivateVoting"));
        assertFalse(responseObj.has("votedIps"));
    }

    @Test
    void getProtectedVotingById() throws Exception {
        //voting with id = 6 is protected and it's key = 'notPublic' (see SQL)
        User votingOwner = userRepo.findById("3").get();
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/votings/{id}", 6).param("votingKey", rightProtectedVotingKey))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(
                                parameterWithName("id")
                                        .description("ID of requested voting.")),
                        requestParameters(
                                parameterWithName("votingKey")
                                        .description("protected voting key")),
                        responseFields(
                                fieldWithPath("id")
                                        .description("ID of requested voting."),
                                fieldWithPath("totalVotes")
                                        .description("totalVotes of requested voting."),
                                fieldWithPath("votingTitle")
                                        .description("votingTitle of requested voting."),
                                fieldWithPath("totalVotes")
                                        .description("totalVotes of requested voting."),
                                fieldWithPath("isProtectedVoting")
                                        .description("Is requested voting protected by key."),
                                fieldWithPath("owner")
                                        .description("requested voting creator."),
                                fieldWithPath("owner.id")
                                        .description("ID of requested voting creator."),
                                fieldWithPath("owner.username")
                                        .description("username of requested voting creator."),
                                fieldWithPath("owner.roles")
                                        .description("roles of requested voting creator.")
                        )
                ))
                .andReturn();
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        User user = mapper.readValue(responseObj.get("owner").toString(), User.class);
        assertTrue((int) responseObj.get("id") == 6, "voting id in request not compared to id in result!");
        assertEquals(user, votingOwner);
        assertFalse(responseObj.has("votingKey"));
        assertFalse(responseObj.has("creationDate"));
        assertFalse(responseObj.has("isPrivateVoting"));
        assertFalse(responseObj.has("votedIps"));
    }

    @Test
    void getProtectedVotingByIdWrongKey() throws Exception {
        //voting with id = 6 is protected and it's key = 'notPublic' (see SQL)
        mockMvc.perform(get("/voteApi/votings/{id}", 6).param("votingKey", "wrongKey"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getVotingByIdNotFound() throws Exception {
        mockMvc.perform(get("/voteApi/votings/{id}", 999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void validatePublicVotingKey() throws Exception {
        MvcResult votingKeyIsValidResponse = mockMvc.perform(get("/voteApi/votings/{id}/validation/key", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(
                                parameterWithName("id")
                                        .description("ID of validated voting."))
                ))
                .andReturn();
        assertTrue(Boolean.parseBoolean(votingKeyIsValidResponse.getResponse().getContentAsString()), "voting key is invalid!");
    }

    @Test
    void validateProtectedVotingKey() throws Exception {
        MvcResult votingKeyIsValidResponse = mockMvc.perform(get("/voteApi/votings/{id}/validation/key", 6)
                .param("votingKey", rightProtectedVotingKey))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(Boolean.parseBoolean(votingKeyIsValidResponse.getResponse().getContentAsString()), "voting key is invalid!");
    }

    @Test
    void validateVotingKeyFailed() throws Exception {
        MvcResult votingKeyIsValidResponse = mockMvc.perform(get("/voteApi/votings/{id}/validation/key", 6))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertFalse(Boolean.parseBoolean(votingKeyIsValidResponse.getResponse().getContentAsString()), "voting key is valid!");
    }

    @Test
    void validateVotingKeyNotFound() throws Exception {
        mockMvc.perform(get("/voteApi/votings/{id}/validation/key", 999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVoting() throws Exception {
        User votingOwner = userRepo.findById("3").get();
        mockMvc.perform(delete("/voteApi/votings/{id}", 1).with(user(votingOwner)))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        pathParameters(
                                parameterWithName("id")
                                        .description("ID of requested voting."))
                ))
                .andExpect(status().isOk());
        mockMvc.perform(get("/voteApi/votings/{id}", 1))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVotingByAdmin() throws Exception {
        User adminUser = userRepo.findById("1").get();
        mockMvc.perform(delete("/voteApi/votings/{id}", 2).with(user(adminUser)))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/voteApi/votings/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVotingFailed() throws Exception {
        User otherUser = userRepo.findById("4").get();
        mockMvc.perform(delete("/voteApi/votings/{id}", 3).with(user(otherUser)))
                .andDo(print())
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/voteApi/votings/{id}", 3))
                .andDo(print())
                .andExpect(status().isOk());
    }
}