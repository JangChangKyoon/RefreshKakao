package com.example.refreshtokenandkakao.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
// Bean 등록을 위한 어노테이션 or 설정파일이라 정의하는 것
@RequiredArgsConstructor
public class WebConfig {

    @Bean
    //Request나 Respons 내에는 모두 Text 형식이라고 할 수 있다. 이를 Java 자료형으로 변환해 주는 역할
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        // "Java Object" =Serialize=> "JSON", (2) "JSON" =Deserialize=> "Java Object
        //dependensies : implementation 'com.fasterxml.jackson.core:jackson-databind:2.12.3'
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

}
