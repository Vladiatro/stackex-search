package net.falsetrue.stackexchangequerier

import net.falsetrue.stackexchangequerier.services.StackExchangeSearchService
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class StackexchangequerierApplicationTests {

	@Autowired
	private lateinit var mvc: MockMvc

	@SpyBean
	private lateinit var stackExchangeSearchService: StackExchangeSearchService

	@Before
	fun setup() {
		// return a stub JSON
		doReturn("""{"items":[{"owner":{"user_id":3296367,"profile_image":"https://i.stack.imgur.com/1WwiD.jpg?s=128&g=1","display_name":"Praneeth Vadlakonda","link":"https://stackoverflow.com/users/3296367/praneeth-vadlakonda"},"is_answered":false,"view_count":5,"answer_count":0,"score":0,"last_activity_date":1543569551,"creation_date":1543569551,"question_id":53554542,"link":"https://stackoverflow.com/questions/53554542/a-fatal-error-has-been-detected-by-the-java-runtime-environment-sigbus-0x7","title":"A fatal error has been detected by the Java Runtime Environment (SIGBUS (0x7))","body":"<p>We have written JMH custom bench marking to our test cases,  expected inputs we are reading from properties file. For each iteration we read input from properties file then we execute our actual logic, we are capturing time in milliseconds before and after execution  of our logic. While starting the test case we pass number of iterations as argument, until 2000 iterations it is working fine. If we give iteration number more than 2000 then we are getting below error.</p>\n\n<blockquote>\n  <p>A fatal error has been detected by the Java Runtime Environment:\n  SIGBUS (0x7) at pc=0xx, pid=xx, tid=0xx\n  Problematic frame:v StubRoutines:jlong_disjoint_arraycopy</p>\n</blockquote>\n\n<p>can you please provide your valuable suggestion on this, thank you.</p>\n"},{"owner":{"user_id":5234729,"profile_image":"https://i.stack.imgur.com/bjpcw.jpg?s=128&g=1","display_name":"Satya G","link":"https://stackoverflow.com/users/5234729/satya-g"},"is_answered":false,"view_count":9,"answer_count":0,"score":0,"last_activity_date":1543569540,"creation_date":1543494680,"last_edit_date":1543569540,"question_id":53539146,"link":"https://stackoverflow.com/questions/53539146/what-are-the-firefoxprofile-preferences-to-allow-downloading-jnlp-content-using","title":"What are the FirefoxProfile preferences to allow downloading jnlp content using Java Web Launcher","body":"<p>I want to set Firefox preference to use \"Java Web Launcher\" as the default application to download jnlp content.  Same can be done through Firefox-Options-Application preferences as show in the screenshot here. </p>\n\n<p>But, how can I do the using Selenium FirefoxProfile ? which preference should be modified. Any help is very much appreciated.</p>\n\n<p>I have tried creating a firefox profile and looking for proper preferences to be updated</p>\n\n<p>FirefoxProfile prf = new FirefoxProfile();<br>\nprf.setPreference(\"browser.helperApps.neverAsk.openFile\",\"application/jnlp\");</p>\n\n<p>....\n??? what other properties should i set here to open jnlp using Java Web Launcher ????\n....</p>\n\n<p>System.setProperty(\"webdriver.gecko.driver\",\"....\\geckodriver.exe\");\nWebDriver ff = new FirefoxDriver(prf)</p>\n"}],"has_more":true,"quota_max":300,"quota_remaining":296,"total":415810}""")
				.`when`(stackExchangeSearchService)
				.loadText(ArgumentMatchers.anyString())
	}

	@Test
	fun contextLoads() {
	}

	@Test
	fun `search with an empty query`() {
		mvc.perform(get("/")
				.accept(MediaType.parseMediaType("application/html;charset=UTF-8")))
				.andExpect(status().isOk)
				.andExpect(forwardedUrl("/WEB-INF/jsp/search.jsp"))
				.andExpect(model().attribute("response",
						hasProperty<Nothing>("total", equalTo(0))))
	}

	@Test
	fun `search with a query with default params`() {
		mvc.perform(get("/")
				.param("query", "java")
				.accept(MediaType.parseMediaType("application/html;charset=UTF-8")))
				.andExpect(status().isOk)
				.andExpect(forwardedUrl("/WEB-INF/jsp/search.jsp"))
				.andExpect(model().attribute("response",
						hasProperty<Nothing>("total", equalTo(415810))))
				.andExpect(model().attribute("response",
						hasProperty<Nothing>("pageCount", equalTo(415810 / 10 + 1))))
				.andExpect(model().attribute("response",
						hasProperty<Nothing>("items", hasSize<Nothing>(2))))
				.andExpect(model().attribute("response",
						hasProperty<Nothing>("items",
								hasItem(hasProperty("id", equalTo(53539146))))))
				.andExpect(model().attribute("params",
						hasProperty<Nothing>("pageSize", equalTo(10))))
				.andExpect(model().attribute("pages",
						hasSize<Nothing>(7)))
	}

	@Test
	fun `search with a query with reversed order, different page size and and 6th page`() {
		mvc.perform(get("/")
				.param("query", "java")
				.param("page", "7")
				.param("orderAsc", "true")
				.param("pageSize", "50")
				.accept(MediaType.parseMediaType("application/html;charset=UTF-8")))
				.andExpect(status().isOk)
				.andExpect(forwardedUrl("/WEB-INF/jsp/search.jsp"))
				.andExpect(model().attribute("response",
						hasProperty<Nothing>("pageCount", equalTo(415810 / 50 + 1))))
				.andExpect(model().attribute("params",
						hasProperty<Nothing>("orderAsc", equalTo(true))))
				.andExpect(model().attribute("pages",
						hasSize<Nothing>(11)))
	}

}
