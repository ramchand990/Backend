package com.healspan.claimservice.mstupload.config;

import com.healspan.claimservice.mstupload.filters.JwtRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/authentication/login"
                        , "/authentication/welcome"
                        , "/healspan/claim/retrieveallclaimsofloggedinuser/*"
                        , "/healspan/claim/retrieveclaim/*"
                        , "/healspan/claim/createorupdateclaimandpatientinfo"
                        , "/healspan/claim/createorupdatemedicalinfo"
                        , "/healspan/claim/createorupdateinsuranceinfo"
                        , "/healspan/claim/savequestionnairesanddocument"
                        , "/healspan/claim/updateclaimstatus"
                        , "/healspan/claim/download/*"
                        , "/healspan/claim/downloadZip/*"
                        , "/healspan/claim/upload"
                        , "/healspan/claim/admin/masters"
                        , "/healspan/claim/admin/masters/name/*"
                        , "/healspan/claim/reviewer-claims/*"
                        , "/healspan/claim/pushclaimdatatorpa"
                        , "/healspan/claim/updatestage"
                        , "/healspan/claim/getdocument/*/*"
                        , "/healspan/claim/comment"
                        , "/healspan/claim/getreviewerdashboardclaims/*"
                        , "/healspan/claim/hospitaluserdashboarddata/*"
                        , "/healspan/claim/healspanuserdashboarddata/*"
                        , "/healspan/claim/admin/masters/name/*"
                        , "/healspan/claim/admin/masters/name/*/*"
                        , "/healspan/claim/admin/masters/insert"
                        , "/healspan/claim/admin/masters/update"
                        , "/tpa/claim/tparesponse"
                        , "/configuration/ui"
                        , "/swagger-resources/**"
                        , "/configuration/security"
                        , "/swagger-ui.html"
                        , "/webjars/**"
                        , "/v2/api-docs/**"

                ).permitAll().
                anyRequest().authenticated().and().
                exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

}