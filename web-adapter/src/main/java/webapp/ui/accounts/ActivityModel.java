package webapp.ui.accounts;

import domain.value.AccountId;
import domain.value.Activity;

import java.util.Optional;

public class ActivityModel {

    final String source;
    final String target;
    final String money;
    final String timestamp;

    private ActivityModel(String source, String target, String money, String timestamp) {
        this.source = source;
        this.target = target;
        this.money = money;
        this.timestamp = timestamp;
    }

    public static ActivityModel from(Activity activity) {
        return new ActivityModel(
            Optional.ofNullable(activity.source).map(AccountId::toString).orElse("-"),
            Optional.ofNullable(activity.target).map(AccountId::toString).orElse("-"),
            String.format("%.2f", activity.money.value()),
            activity.timestamp.toString()
        );
    }

    public static ActivityModel empty() {
        return new ActivityModel("-", "-", "0.00", "");
    }
}
