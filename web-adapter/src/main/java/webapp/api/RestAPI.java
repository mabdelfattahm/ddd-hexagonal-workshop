package webapp.api;

import domain.value.Money;
import org.springframework.web.bind.annotation.*;
import port.in.CreateAccount;
import port.out.StoreAccount;

@RestController
@RequestMapping("/api/v1/")
public class RestAPI {

    private final StoreAccount storage;

    public RestAPI(StoreAccount storage) {
        this.storage = storage;
    }

    @PostMapping("account")
    public void addAccount(@RequestBody String balance) {
        this.storage.store(new CreateAccount().create(Money.of(Double.parseDouble(balance))));
    }
}
