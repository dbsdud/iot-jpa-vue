package io.dbsdud.backend.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class CommonController {
    @GetMapping("/api")
    public String index() {
        return "HI, Current Server Time is " + new Date() + "\n";
    }
}
