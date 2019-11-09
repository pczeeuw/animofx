package nl.pczeeuw.animofx8.services;

import org.springframework.stereotype.Component;

@Component
public class AwesomeActionService {

    public String processName(String name) {
        if(name.equals("Anton")) {
            return "Hello Anton!";
        }
        else {
            return "Hello Unknown Stranger!";
        }
    }
}
