package formation.afpa.natspecies.model;

public class ModifySpecieThread extends Thread {

    private InetworkListener in;

    public ModifySpecieThread(InetworkListener in) {
        this.in = in;
    }

    @Override
    public void run() {
        super.run();
    }
}
