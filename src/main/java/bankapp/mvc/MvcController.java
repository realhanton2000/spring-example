package bankapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MvcController {

    @RequestMapping("/customerMgt")
    public String customerMgtForm() {
        return "customerMgtForm";
    }

    @RequestMapping("/accountTransfer")
    public String accountTransfer() {
        return "accountTransferForm";
    }

    @RequestMapping("/accountMgt")
    public String accountMgtForm() {
        return "accountMgtForm";
    }
}
