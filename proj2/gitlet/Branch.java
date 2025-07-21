package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Branch implements Dumpable{
    public final String name;
    public String ref_UID;
    public Branch(String name, String ref_UID) {

        for(String branch_name : getAllBranches()){
            if(branch_name.equals(name)){
                System.out.println("A branch with that name already exists.");
                System.exit(0);
            }
        }
        this.name = name;
        this.ref_UID = ref_UID;
        this.dump();
    }


    @Override
    public void dump() {
        File curr_branch_file = Utils.join(Repository.REFS_DIR,this.name);
        if(!curr_branch_file.exists()) {
            try {
                curr_branch_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Utils.writeObject(curr_branch_file, this);
    }
    public static Branch get_Branch(String branch_name) {
        File branch_path=Utils.join(Repository.REFS_DIR,branch_name);
        if(branch_path.exists()){
            return  Utils.readObject(branch_path, Branch.class);
        }
        return null;
    }
    public static List<String> getAllBranches(){
        return Utils.plainFilenamesIn(Repository.REFS_DIR);
    }
    public static void removeBranch(String branch_name) {
        File branch_path=Utils.join(Repository.REFS_DIR,branch_name);
        if(branch_path.exists()){
            branch_path.delete();
        }
    }
    public void updateUID(String uid) {
        this.ref_UID = uid;
        dump();
    }
    public static boolean has_branch(String branch_name) {
        for(String b_name : getAllBranches()){
            if(b_name.equals(branch_name)){
                return true;
            }
        }
        return false;
    }
}
