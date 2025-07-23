package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Branch {
    public final String name;
    public String ref_UID;

    /**
     * 用来新建Branch的
     * @param name
     * @param ref_UID
     */
    public Branch(String name, String ref_UID) {

        for(String branch_name : getAllBranches().keySet()){
            if(branch_name.equals(name)){
                System.out.println("A branch with that name already exists.");
                System.exit(0);
            }
        }
        this.name = name;
        this.ref_UID = ref_UID;
        this.dump();
    }
    public Branch(String name, String ref_UID, boolean flag) {
        this.name = name;
        this.ref_UID = ref_UID;

    }

    public void dump(){
        BranchREFS refs = BranchREFS.getBranchREFS();
        refs.branches.put(name,ref_UID);
        refs.dump();
    }

    public static Branch get_Branch(String branch_name) {
        BranchREFS refs = BranchREFS.getBranchREFS();
        if(refs.branches.containsKey(branch_name)){
            return new Branch(branch_name,refs.branches.get(branch_name),true);

        }else {
            return null;
        }
    }
    public static Map<String,String> getAllBranches(){
        BranchREFS branch_refs=BranchREFS.getBranchREFS();
        return branch_refs.branches;
    }
    public static void removeBranch(String branch_name) {
        BranchREFS refs = BranchREFS.getBranchREFS();
        refs.branches.remove(branch_name);
        refs.dump();
    }
    public void updateUID(String uid) {
        BranchREFS refs = BranchREFS.getBranchREFS();
        this.ref_UID = uid;
        refs.branches.put(this.name,uid);
        refs.dump();
    }
    public static boolean has_branch(String branch_name) {
        for(String b_name : getAllBranches().keySet()){
            if(b_name.equals(branch_name)){
                return true;
            }
        }
        return false;
    }
}
