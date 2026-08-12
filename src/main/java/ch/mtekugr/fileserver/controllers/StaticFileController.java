package ch.mtekugr.fileserver.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticFileController {
    @GetMapping(value = {"/", "/index"})
    public String forwardIndex() {
        return "forward:/index.html";
    }

    @GetMapping(value = {"/admin-ui", "/admin-ui/index"})
    public String forwardAdmin() {
        return "forward:/admin-ui/index.html";
    }
}
