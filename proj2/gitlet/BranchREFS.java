package gitlet;


import java.util.Map;
import java.util.TreeMap;

public class BranchREFS implements Dumpable {
    public Map<String, String> branches;

    public BranchREFS() {
        branches = new TreeMap<>();
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
