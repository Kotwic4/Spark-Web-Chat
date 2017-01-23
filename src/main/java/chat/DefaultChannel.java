package chat;

class DefaultChannel extends UserChannel{

    DefaultChannel() {
        super(Chat.defaultChannel);
    }

    @Override
    public boolean canDelete() {
        return false;
    }
}
