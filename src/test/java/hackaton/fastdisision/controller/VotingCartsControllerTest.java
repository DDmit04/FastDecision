package hackaton.fastdisision.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hackaton.fastdisision.data.User;
import hackaton.fastdisision.data.Voting;
import hackaton.fastdisision.repo.UserRepo;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:create-user-before.sql", "classpath:create-votings-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:create-votings-after.sql", "classpath:create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class VotingCartsControllerTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    void searchVotings() throws Exception {
        String searchString = "search";
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/search").param("search", searchString))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<Voting> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), Voting[].class));

        assertTrue((int)responseObj.get("totalElements") == 3, "total elements count is wrong!");
        assertTrue((int)responseObj.get("numberOfElements") == 3, "number of elements is wrong!");
        content.forEach(voting -> {
            assertTrue(voting.getVotingTitle().contains(searchString), "voting title isn't contain searched string!");
        });
    }

    @Test
    void getNewest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/newest"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<Voting> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), Voting[].class));

        assertTrue((int)responseObj.get("totalElements") == 5, "total elements count is wrong!");
        assertTrue((int)responseObj.get("numberOfElements") == 5, "number of elements is wrong!");

        for(int i = 0; i < content.size() - 1; i++) {
            // check ID's because API doesn't return CreationDate in response (change SQL file carefully!!!)
            assertTrue(content.get(i).getId() < content.get(i + 1).getId(), "wrong votings sorting! (pay extra attention to SQL and this test)");
        }

    }

    @Test
    void getPopular() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/popular"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<Voting> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), Voting[].class));

        assertTrue((int)responseObj.get("totalElements") == 5, "total elements count is wrong!");
        assertTrue((int)responseObj.get("numberOfElements") == 5, "number of elements is wrong!");

        for(int i = 0; i < content.size() - 1; i++) {
            // change SQL file carefully!!!
            assertTrue(content.get(i).getTotalVotes() < content.get(i + 1).getTotalVotes(), "wrong votings soring! (pay extra attantion to SQL and this test)");
        }
    }

    @Test
    void getUserPublic() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/userPublic/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<Voting> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), Voting[].class));
        assertTrue((int)responseObj.get("totalElements") == 5, "total elements count is wrong!");
        assertTrue((int)responseObj.get("numberOfElements") == 5, "number of elements is wrong!");
    }

    @Test
    void getUserPrivate() throws Exception {
        // id = 3 user with id = 3 is not admin
        User commonUser = userRepo.findById("3").get();
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/userPrivate/3").with(user(commonUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<Voting> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), Voting[].class));
        assertTrue((int)responseObj.get("totalElements") == 2, "total elements count is wrong!");
        assertTrue((int)responseObj.get("numberOfElements") == 2, "number of elements is wrong!");
        for(int i = 0; i < content.size() - 1; i++) {
            // change SQL file carefully!!!
            assertTrue(content.get(i).getOwner().equals(commonUser), "user is not owner of it's public voitngs! (pay extra attantion to SQL and this test)");
        }
    }

    @Test
    void getUserPrivateByAdmin() throws Exception {
        // id = 3 user with id = 3 is not admin (see SQL)
        User commonUser = userRepo.findById("3").get();
        User adminUser = userRepo.findById("1").get();
        MvcResult mvcResult = mockMvc.perform(get("/voteApi/charts/userPrivate/3").with(user(adminUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject responseObj = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<Voting> content = Arrays.asList(mapper.readValue(responseObj.get("content").toString(), Voting[].class));
        assertTrue((int)responseObj.get("totalElements") == 2, "total elements count is wrong!");
        assertTrue((int)responseObj.get("numberOfElements") == 2, "number of elements is wrong!");
        for(int i = 0; i < content.size() - 1; i++) {
            // change SQL file carefully!!!
            assertTrue(content.get(i).getOwner().equals(commonUser), "user is not owner of it's public voitngs! (pay extra attantion to SQL and this test)");
        }
    }

    @Test
    void getUserPrivateByOtherUser() throws Exception {
        // id = 4 user with id = 4 is not admin and not owner of votings(see SQL)
        User otherCommonUser = userRepo.findById("4").get();
        mockMvc.perform(get("/voteApi/charts/userPrivate/3").with(user(otherCommonUser)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}