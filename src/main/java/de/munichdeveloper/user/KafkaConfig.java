package de.munichdeveloper.user;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userCreatedTopic() {
        return TopicBuilder.name("user-created")
                .replicas(0)
                .build();
    }

    @Bean
    public NewTopic userSigninMagicLinkTopic() {
        return TopicBuilder.name("user-signin-magic-link")
                .replicas(0)
                .build();
    }

}
