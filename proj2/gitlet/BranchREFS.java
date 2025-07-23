package gitlet;


import java.util.HashMap;

public class BranchREFS implements Dumpable {
    public HashMap<String, String> branches;

    public BranchREFS() {
        branches = new HashMap<>();
        this.dump();
    }
    @Override
    public void dump() {
        Utils.writeObject(Repository.REFS_FILE, this);
    }

    public static BranchREFS getBranchREFS() {
        if (Repository.REFS_FILE.exists()) {
            return Utils.readObject(Repository.REFS_FILE, BranchREFS.class);
        }else {
            return new BranchREFS();
        }
    }
}
