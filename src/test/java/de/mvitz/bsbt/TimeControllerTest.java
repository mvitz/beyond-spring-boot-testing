package de.mvitz.bsbt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WithLocalDateTime(date = "2024-05-31", time = "17:42:53")
class TimeControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void now_shouldRenderCurrentTime() throws Exception {
        mvc.perform(get("/time"))
            .andExpect(status().isOk())
            .andExpect(content().string(
                is(equalTo("2024-05-31 17:42:53"))));
    }
}
