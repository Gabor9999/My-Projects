package Week2;

import java.util.List;
import java.util.ArrayList;

public class PostBox {
    private List<Mail> mails = new ArrayList<>();

    public void receive(Mail mail) {
        mails.add(mail);
    }

    public int getNumberOfMails() {
        return mails.size();
    }

    public Mail getMail(int index) {
        return mails.get(index);
    }
}
