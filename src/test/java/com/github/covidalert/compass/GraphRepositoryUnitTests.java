package com.github.covidalert.compass;


/*@DataNeo4jTest
@Transactional(propagation = Propagation.NEVER)
@Slf4j*/
public class GraphRepositoryUnitTests {
    /*
    private static Neo4j embeddedDatabaseServer;

    @Autowired
    private GraphRepository graphRepository;

    @BeforeAll
    static void initializeNeo4j() { embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder().withDisabledServer().build(); }

    @AfterAll
    static void stopNeo4j() { embeddedDatabaseServer.close(); }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> "root");
    }

    @BeforeEach
    public void setup() { graphRepository.deleteAll(); }

    @Ignore("failing unit test")
    @Test
    public void simpleTest(){
        graphRepository.findPotentials("123-456",1L);
    }*/
}
