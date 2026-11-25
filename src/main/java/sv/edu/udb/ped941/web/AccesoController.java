package sv.edu.udb.ped941.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccesoController {

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("/inicio")
    public String inicio(
            Authentication authentication) {

        boolean administrador =
                authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(autoridad ->
                                autoridad
                                        .getAuthority()
                                        .equals(
                                                "ROLE_ADMINISTRADOR"));

        if (administrador) {

            return "redirect:/admin";
        }

        return "redirect:/";
    }
}
