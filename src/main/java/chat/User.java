package chat;

class User {

    final String username;

    User(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username;
    }
}
