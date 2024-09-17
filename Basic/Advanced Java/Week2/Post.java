package Week2;

public class Post {
    private PostBox first, second;

    public Post(PostBox first, PostBox second) {
        this.first = first;
        this.second = second;
    }

    public void sort(Mail mail) {
        if (mail.getZip() % 2 == 0) {
            first.receive(mail);
        } else {
            second.receive(mail);
        }
    }
}
