
class DefaultChannel extends UserChannel{
    DefaultChannel() {
        super(Main.defaultChannel);
    }
    @Override
    public boolean canDelete() {
        return false;
    }
}
