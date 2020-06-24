package ru.raimbek.rakhimbekov.alphabattle.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String hi(String request) {
        return String.format("Hello, World(%s)!", request);
    }
}

/*
e.g: python3 -m http.server
client-side:

<html>
	<head>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
	</head>
	<body>
		<script type="text/javascript">
			var socket = new SockJS('http://localhost:8080/gs-guide-websocket');
		    stompClient = Stomp.over(socket);
		    stompClient.connect({}, function (frame) {
		        console.log('Connected: ' + frame);

		        stompClient.subscribe('/topic/greetings', function (greeting) {
		            console.log(greeting.body);
		        });
		    });

		    stompClient.send("/app/hello", {}, 'Hello, Test Stomp');

		</script>
	</body>
</html>
*/