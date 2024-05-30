package com.example.shopbe.config;

import com.example.shopbe.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(apiPrefix + "/users/register",
                                    apiPrefix + "/users/login")
                            .permitAll()

                            .requestMatchers(HttpMethod.GET, apiPrefix + "/roles").permitAll()

                            .requestMatchers(HttpMethod.GET, apiPrefix + "/products/images/**").permitAll()

                            .requestMatchers(HttpMethod.POST, apiPrefix + "/products/uploads/**").hasAnyRole("ADMIN", "USER")

                            .requestMatchers(HttpMethod.GET, apiPrefix + "/categories**").permitAll() // everybody can get categories

                            .requestMatchers(HttpMethod.POST, apiPrefix + "/categories").hasRole("ADMIN") // only admin can add categories

                            .requestMatchers(HttpMethod.DELETE, apiPrefix + "/categories/**").hasRole("ADMIN") // only admin can delete category

                            .requestMatchers(HttpMethod.PUT, apiPrefix + "/categories/**").hasRole("ADMIN") // only admin can add categories

                            .requestMatchers(HttpMethod.GET, apiPrefix + "/products**").permitAll() // everybody can get products

                            .requestMatchers(HttpMethod.GET, apiPrefix + "/products/**").permitAll() // everybody can get products

                            .requestMatchers(HttpMethod.POST, apiPrefix + "/products").hasRole("ADMIN") // only admin can add products

                            .requestMatchers(HttpMethod.DELETE, apiPrefix + "/products/**").hasRole("ADMIN") // only admin can delete product

                            .requestMatchers(HttpMethod.PUT, apiPrefix + "/products/**").hasRole("ADMIN") // only admin can add product

                            .requestMatchers(HttpMethod.PUT, apiPrefix + "/orders/**").hasRole("ADMIN") // only admin can update order

                            .requestMatchers(HttpMethod.DELETE, apiPrefix + "/orders/**").hasRole("ADMIN") // only admin can delete order

                            .requestMatchers(HttpMethod.POST, apiPrefix + "/orders/**").hasAnyRole("ADMIN", "USER") // admin and user can create order

                            .requestMatchers(HttpMethod.GET, apiPrefix + "/orders/**").hasAnyRole("ADMIN", "USER") // user or admin must be login before get order info

                            .requestMatchers(HttpMethod.GET, apiPrefix + "/orders/user/**").hasAnyRole("ADMIN", "USER") // user or admin can get all order by user_id

                            .requestMatchers(HttpMethod.GET, apiPrefix + "/order_details/**").hasAnyRole("ADMIN", "USER") // user or admin can get  orderDetail by id


                            .requestMatchers(HttpMethod.POST, apiPrefix + "/order_details").hasAnyRole("ADMIN", "USER") // user or admin can create orderDetail

                            .requestMatchers(HttpMethod.GET, apiPrefix + "/order_details/order/**").hasAnyRole("ADMIN", "USER") // user or admin can create orderDetail by order id

                            .requestMatchers(HttpMethod.PUT, apiPrefix + "/order_details/**").hasAnyRole("ADMIN", "USER") // user or admin can update orderDetail

                            .requestMatchers(HttpMethod.DELETE, apiPrefix + "/order_details/**").hasAnyRole("ADMIN", "USER") // user or admin can delete orderDetail

                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);

        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token"));
                corsConfiguration.setAllowedOrigins(List.of("*"));
                corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
                corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
                urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
                httpSecurityCorsConfigurer.configurationSource(urlBasedCorsConfigurationSource);
            }
        });

        return http.build();
    }
}
