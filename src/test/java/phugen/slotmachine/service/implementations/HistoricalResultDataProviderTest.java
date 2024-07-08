package phugen.slotmachine.service.implementations;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.model.RoundResult;
import phugen.slotmachine.repository.interfaces.RoundResultRepository;
import phugen.slotmachine.repository.interfaces.HistoricalResultDataProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@Testcontainers
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {HistoricalResultDataProviderTest.Initializer.class})
@SpringBootTest
public class HistoricalResultDataProviderTest {
	private final RoundResultRepository repository;
	private final HistoricalResultDataProvider dataProvider;

	public HistoricalResultDataProviderTest(
			RoundResultRepository repository,
			HistoricalResultDataProvider dataProvider

	) {
		this.repository = repository;
		this.dataProvider = dataProvider;
	}

	@BeforeEach
	public void resetDatabase() {
		this.repository.deleteAll();
	}

	@Container
	private final static PostgreSQLContainer database = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3"))
			.withDatabaseName("slotmachine")
			.withUsername("postgres")
			.withPassword("secret");

	@Transactional
	@MethodSource("getRetrievingDataTests")
	@ParameterizedTest
	public void testRetrievingDataFromResultDb(TestData testData) {
		assertThat(database.isRunning()).isTrue();

		testData.roundResults.forEach(repository::save);

		List<RoundResult> actual = dataProvider.getResultDataAfter(testData.deadline);
		assertEquals("database result size", testData.expectedSize, actual.size());
	}

	@Transactional
	@Test
	public void testSaveDataToResultDb() {
		assertThat(database.isRunning()).isTrue();

		List<RoundResult> dataToSave = List.of(
				new RoundResult(
						List.of(
								new Row("a", "b", "c"),
								new Row("d", "e", "f"),
								new Row("g", "h", "i")
						),
						false
				),
				new RoundResult(
						List.of(
								new Row("x", "b", "c"),
								new Row("x", "e", "f"),
								new Row("x", "h", "i")
						),
						true
				)
		);

		dataToSave.forEach(dataProvider::saveResultData);

		List<RoundResult> actual = new ArrayList<>();
		repository.findAll().forEach(actual::add);
		assertEquals("database result size", dataToSave.size(), actual.size());
	}

	private static Stream<TestData> getRetrievingDataTests() {
		return Stream.of(
				new TestData(
						List.of(
								new RoundResult(
										List.of(
												new Row("x", "x", "x"),
												new Row("o", "o", "o"),
												new Row("o", "o", "o")
										),
										true
								),
								new RoundResult(
										List.of(
												new Row("x", "o", "x"),
												new Row("x", "o", "o"),
												new Row("o", "x", "o")
										),
										false
								)
						),
						2,
						LocalDateTime.now().minus(60, ChronoUnit.MINUTES),
						"should retrieve two round results correctly when deadline is way in the past"
				),
				new TestData(
						List.of(
								new RoundResult(
										List.of(
												new Row("x", "x", "x"),
												new Row("o", "o", "o"),
												new Row("o", "o", "o")
										),
										true
								),
								new RoundResult(
										List.of(
												new Row("x", "o", "x"),
												new Row("x", "o", "o"),
												new Row("o", "x", "o")
										),
										false
								)
						),
						0,
						LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
						"should retrieve no results when deadline is in the future"
				)
		);
	}

	private record TestData(
			List<RoundResult> roundResults,
			Integer expectedSize,
			LocalDateTime deadline,
			String description
	) {
		@Override
		public String toString() {
			return this.description;
		}
	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url=" + database.getJdbcUrl(),
					"spring.datasource.username=" + database.getUsername(),
					"spring.datasource.password=" + database.getPassword()
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}
}
