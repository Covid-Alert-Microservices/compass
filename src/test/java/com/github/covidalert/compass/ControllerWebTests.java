package com.github.covidalert.compass;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.KeycloakAccess;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.KeycloakAccessToken;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.keycloak.ServletKeycloakAuthUnitTestingSupport;
import com.github.covidalert.compass.controllers.GeoController;
import com.github.covidalert.compass.dtos.NewGeolocationDto;
import com.github.covidalert.compass.repositories.GeolocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GeoController.class)
@Import({
        ServletKeycloakAuthUnitTestingSupport.UnitTestConfig.class,
        KeycloakSecurityConfig.class,
        TestConfig.class
})
public class ControllerWebTests {

    @Autowired
    private GeoController geoController;

    @MockBean
    private GeolocationRepository geolocationRepository;

    @Autowired
    MockMvcSupport api;

    @Test
    public void givenUnauthenticatedUser_whenPostGeolocation_shouldBeUnauthorized() throws Exception
    {
        api.post(new NewGeolocationDto(1,1,1L),"/api")
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockKeycloakAuth
    public void givenAuthenticatedUserWithoutRole_whenPostGeolocation_shouldBeForbidden() throws Exception
    {
        api.post(new NewGeolocationDto(1,1,1L),"/api")
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockKeycloakAuth(
            claims = @OpenIdClaims(
                    sub = "user-id",
                    preferredUsername = "username"
            ),
            accessToken = @KeycloakAccessToken(
                    realmAccess = @KeycloakAccess(
                            roles = "user"
                    )
            )
    )
    public void givenAuthenticatedUserWithRole_whenPostGeolocation_shouldBeAuthorized() throws Exception
    {
        api.post(new NewGeolocationDto(1,1,1L),"/api")
                .andExpect(status().isOk());
    }

}
