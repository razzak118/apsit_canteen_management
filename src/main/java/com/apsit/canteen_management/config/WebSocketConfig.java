package com.apsit.canteen_management.config;

import com.apsit.canteen_management.security.AuthUtil;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthUtil authUtil;

    public WebSocketConfig(AuthUtil authUtil) {
        this.authUtil = authUtil;
    }

    // method to build a web socket connection. HANDSHAKE
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:*","http://127.0.0.1:*")
                .withSockJS();
    }

    // to configure the channels like /topic/order is the broadcast channel & /app for messages set from client to server.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor= MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // authenticate when the client first connect
                assert accessor != null;
                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    String authHeader=accessor.getFirstNativeHeader("Authorization");
                    if(authHeader!=null && authHeader.startsWith("Bearer ")){
                        String jwtToken=authHeader.split("Bearer ")[1];
                        String username= authUtil.getUsernameFromToken(jwtToken);
                        if(username!=null){
                            // Set the principal so Spring knows who this WebSocket session belongs to
                            Principal principal=()->username; // short from to call getName function of the Principal class.
                            accessor.setUser(principal);
                        }
                    }
                }
                return message;
            }
        });
    }
}
