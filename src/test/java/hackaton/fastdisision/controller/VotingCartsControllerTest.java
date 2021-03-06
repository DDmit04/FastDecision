package hackaton.fastdisision.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackaton.fastdisision.BasicTest;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.VotingDTO;
import hackaton.fastdisision.repo.UserRepo;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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
public class VotingCartsControllerTest extends BasicTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private String commonUserID = "3";
    private String adminUserID = "1";

    private long votingId = 3;
    private String nonOwnerUserId = "4";

    private User commonUser;
    private User adminUser;
    private User otherCommonUser;

    @Test
    void searchVotings() throws Exception {
        String searchString = "search";

        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/search").param("search", searchString))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        requestParameters(
                                parameterWithName("search").description("string to search voting title")),
                        responseFields(
                                fieldWithPath("size").description("Page size."),
                                fieldWithPath("number").description("Current page number."),
                                fieldWithPath("totalElements").description("Total count of elements."),
                                fieldWithPath("last").description("Is last page."),
                                fieldWithPath("totalPages").description("Total page number."),
                                fieldWithPath("sort.sorted").description("Is votings sorted."),
                                fieldWithPath("sort.unsorted").description("Is votings unsorted."),
                                fieldWithPath("sort.empty").description("Is votings empty."),
                                fieldWithPath("first").description("Is last page."),
                                fieldWithPath("numberOfElements").description("Total number of elements."),
                                fieldWithPath("content").description("Page content."),
                                fieldWithPath("content[].id").description("Voting ID"),
                                fieldWithPath("content[].totalVotes").description("Total voting votes"),
                                fieldWithPath("content[].votingTitle").description("Voting title"),
                                fieldWithPath("content[].isProtectedVoting").description("Is voting protected"),
                                fieldWithPath("content[].owner").description("Voting owner"),
                                fieldWithPath("content[].owner.id").description("Voting owner ID"),
                                fieldWithPath("content[].owner.username").description("Voting owner username"),
                                fieldWithPath("content[].owner.roles").description("Voting owner roles"),
                                fieldWithPath("content[].votingOptions[].id").description("voting option ID."),
                                fieldWithPath("content[].votingOptions[].voteDiscription").description("voting option discription."),
                                fieldWithPath("content[].votingOptions[].pluses").description("voting option pluses.")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.numberOfElements").value(3))
                .andReturn();
        
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<VotingDTO> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), VotingDTO[].class));

        content.forEach(voting -> assertTrue(voting.getVotingTitle().contains(searchString), "voting title isn't contain searched string!"));
    }

    @Test
    void getNewest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/newest"))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        responseFields(
                                fieldWithPath("size").description("Page size."),
                                fieldWithPath("number").description("Current page number."),
                                fieldWithPath("totalElements").description("Total count of elements."),
                                fieldWithPath("last").description("Is last page."),
                                fieldWithPath("totalPages").description("Total page number."),
                                fieldWithPath("sort").description("Page content sorting options."),
                                fieldWithPath("sort.sorted").description("Is votings sorted."),
                                fieldWithPath("sort.unsorted").description("Is votings unsorted."),
                                fieldWithPath("sort.empty").description("Is votings empty."),
                                fieldWithPath("first").description("Is last page."),
                                fieldWithPath("numberOfElements").description("Total number of elements."),
                                fieldWithPath("content").description("Page content."),
                                fieldWithPath("content[].id").description("Voting ID"),
                                fieldWithPath("content[].totalVotes").description("Total voting votes"),
                                fieldWithPath("content[].votingTitle").description("Voting title"),
                                fieldWithPath("content[].isProtectedVoting").description("Is voting protected"),
                                fieldWithPath("content[].owner").description("Voting owner"),
                                fieldWithPath("content[].owner.id").description("Voting owner ID"),
                                fieldWithPath("content[].owner.username").description("Voting owner username")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.numberOfElements").value(5))
                .andReturn();

        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<VotingDTO> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), VotingDTO[].class));

        for(int i = 0; i < content.size() - 1; i++) {
            // check ID's because API doesn't return CreationDate in response (change SQL file carefully!!!)
            assertTrue(content.get(i).getId() < content.get(i + 1).getId(), "wrong votings sorting! (pay extra attention to SQL and this test)");
        }

    }

    @Test
    void getPopular() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/popular"))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        responseFields(
                                fieldWithPath("size").description("Page size."),
                                fieldWithPath("number").description("Current page number."),
                                fieldWithPath("totalElements").description("Total count of elements."),
                                fieldWithPath("last").description("Is last page."),
                                fieldWithPath("totalPages").description("Total page number."),
                                fieldWithPath("sort.sorted").description("Is votings sorted."),
                                fieldWithPath("sort").description("Page content sorting options."),
                                fieldWithPath("sort.unsorted").description("Is votings unsorted."),
                                fieldWithPath("sort.empty").description("Is votings empty."),
                                fieldWithPath("first").description("Is last page."),
                                fieldWithPath("numberOfElements").description("Total number of elements."),
                                fieldWithPath("content").description("Page content."),
                                fieldWithPath("content[].id").description("Voting ID"),
                                fieldWithPath("content[].totalVotes").description("Total voting votes"),
                                fieldWithPath("content[].votingTitle").description("Voting title"),
                                fieldWithPath("content[].isProtectedVoting").description("Is voting protected"),
                                fieldWithPath("content[].owner").description("Voting owner"),
                                fieldWithPath("content[].owner.id").description("Voting owner ID"),
                                fieldWithPath("content[].owner.username").description("Voting owner username")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.numberOfElements").value(5))
                .andReturn();

        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<VotingDTO> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), VotingDTO[].class));

        for(int i = 0; i < content.size() - 1; i++) {
            // change SQL file carefully!!!
            assertTrue(content.get(i).getTotalVotes() >= content.get(i + 1).getTotalVotes(), "wrong votings soring! (pay extra attantion to SQL and this test)");
        }
    }

    @Test
    void getUserPublic() throws Exception {
        mockMvc.perform(get("/voteApi/charts/userPublic/" + votingId))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        responseFields(
                                fieldWithPath("size").description("Page size."),
                                fieldWithPath("number").description("Current page number."),
                                fieldWithPath("totalElements").description("Total count of elements."),
                                fieldWithPath("last").description("Is last page."),
                                fieldWithPath("totalPages").description("Total page number."),
                                fieldWithPath("sort").description("Page content sorting options."),
                                fieldWithPath("sort.sorted").description("Is votings sorted."),
                                fieldWithPath("sort.unsorted").description("Is votings unsorted."),
                                fieldWithPath("sort.empty").description("Is votings empty."),
                                fieldWithPath("first").description("Is last page."),
                                fieldWithPath("numberOfElements").description("Total number of elements."),
                                fieldWithPath("content").description("Page content."),
                                fieldWithPath("content[].id").description("Voting ID"),
                                fieldWithPath("content[].totalVotes").description("Total voting votes"),
                                fieldWithPath("content[].votingTitle").description("Voting title"),
                                fieldWithPath("content[].isProtectedVoting").description("Is voting protected"),
                                fieldWithPath("content[].owner").description("Voting owner"),
                                fieldWithPath("content[].owner.id").description("Voting owner ID"),
                                fieldWithPath("content[].owner.username").description("Voting owner username"),
                                fieldWithPath("content[].owner.roles").description("Voting owner roles"),
                                fieldWithPath("content[].votingOptions[].id").description("voting option ID."),
                                fieldWithPath("content[].votingOptions[].voteDiscription").description("voting option discription."),
                                fieldWithPath("content[].votingOptions[].pluses").description("voting option pluses.")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.numberOfElements").value(5));
    }

    @Test
    void getUserPrivate() throws Exception {
        commonUser = userRepo.findById(commonUserID).get();

        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/userPrivate/" + votingId).with(user(commonUser)))
                .andDo(print())
                .andDo(document("{ClassName}/{methodName}",
                        responseFields(
                                fieldWithPath("size").description("Page size."),
                                fieldWithPath("number").description("Current page number."),
                                fieldWithPath("totalElements").description("Total count of elements."),
                                fieldWithPath("last").description("Is last page."),
                                fieldWithPath("totalPages").description("Total page number."),
                                fieldWithPath("sort").description("Page content sorting options."),
                                fieldWithPath("sort.sorted").description("Is votings sorted."),
                                fieldWithPath("sort.unsorted").description("Is votings unsorted."),
                                fieldWithPath("sort.empty").description("Is votings empty."),
                                fieldWithPath("first").description("Is last page."),
                                fieldWithPath("numberOfElements").description("Total number of elements."),
                                fieldWithPath("content").description("Page content."),
                                fieldWithPath("content[].id").description("Voting ID"),
                                fieldWithPath("content[].totalVotes").description("Total voting votes"),
                                fieldWithPath("content[].votingTitle").description("Voting title"),
                                fieldWithPath("content[].isProtectedVoting").description("Is voting protected"),
                                fieldWithPath("content[].owner").description("Voting owner"),
                                fieldWithPath("content[].owner.id").description("Voting owner ID"),
                                fieldWithPath("content[].owner.username").description("Voting owner username"),
                                fieldWithPath("content[].owner.roles").description("Voting owner roles"),
                                fieldWithPath("content[].votingOptions[].id").description("voting option ID."),
                                fieldWithPath("content[].votingOptions[].voteDiscription").description("voting option discription."),
                                fieldWithPath("content[].votingOptions[].pluses").description("voting option pluses.")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andReturn();

        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<VotingDTO> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), VotingDTO[].class));

        for(int i = 0; i < content.size() - 1; i++) {
            // change SQL file carefully!!!
            assertEquals(content.get(i).getOwner(), commonUser, "user is not owner of it's public voitngs! (pay extra attantion to SQL and this test)");
        }
    }

    @Test
    void getUserPrivateByAdmin() throws Exception {
        commonUser = userRepo.findById(commonUserID).get();
        adminUser = userRepo.findById(adminUserID).get();

        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/userPrivate/" + votingId).with(user(adminUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andReturn();

        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<VotingDTO> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), VotingDTO[].class));

        for(int i = 0; i < content.size() - 1; i++) {
            // change SQL file carefully!!!
            assertEquals(content.get(i).getOwner(), commonUser, "user is not owner of it's public voitngs! (pay extra attantion to SQL and this test)");
        }
    }

    @Test
    void getUserPrivateByOtherUser() throws Exception {
        otherCommonUser = userRepo.findById(nonOwnerUserId).get();

        mockMvc.perform(get("/voteApi/charts/userPrivate/" + votingId).with(user(otherCommonUser)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}