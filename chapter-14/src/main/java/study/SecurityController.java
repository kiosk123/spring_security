package study;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/")
    public String index() {
        return "home";
    }
    
    @GetMapping("/denied")
    public String denied() {
        return "denied";
    }

    @GetMapping("/user")
    public String user() {
        return "user"; 
    }

    @GetMapping("/admin/pay")
    public String adminPay() {
        return "admin pay"; 
    }

    @GetMapping("/sys")
    public String sys() {
        return "sys";
    }
    
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
