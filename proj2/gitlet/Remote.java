package gitlet;

import jh61b.junit.In;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Remote implements Dumpable{
    public String remote_name;
    public String remote_path;

    public Remote(String remote_name, String remote_path) {
        for(String r_name : getAllRemotes() ){
            if(r_name.equals(remote_name)){
                System.out.println("A remote with that name already exists.");
                System.exit(0);
            }
        }
        this.remote_name = remote_name;
        this.remote_path = remote_path;
        this.dump();
    }
    public void remove_self() {
        for(String r_name : getAllRemotes() ){
            if(r_name.equals(this.remote_name)){
                File remote_file = Utils.join(Repository.REMOTE_DIR,this.remote_name);
                remote_file.delete();
                System.exit(0);
            }
        }
        System.out.println("A remote with that name does not exist.");
    }
    @Override
    public void dump() {
        Utils.writeObject(Utils.join(Repository.REMOTE_DIR,this.remote_name),this);
    }
    public static Remote getRemote(String remote_name) throws Exception {
        return Utils.readObject(Utils.join(Repository.REMOTE_DIR,remote_name),Remote.class);
    }
    public static List<String> getAllRemotes()  {
        return Utils.plainFilenamesIn(Repository.REMOTE_DIR);
    }

    public static Branch getRemoteBranch(String remote_name)  {
        try{
            return Utils.readObject(Utils.join(Repository.REMOTE_DIR,remote_name),Branch.class);
        }catch(Exception e){
            return null;
        }
    }
    public static void pushFromLoaclToRemote
            (String start_sha1,String end_sha1,String local_commit_dir,String remote_commit_dir){
            try {
                Commit end = Commit.getCommit(end_sha1);
                Commit start = Commit.getCommit(start_sha1);
                Map<String, Integer> path = Commit.getPathToTargetCommit(start,end);
                Integer last_layer = path.get(end_sha1);
                for(String commit: path.keySet()){
                    if(last_layer.equals(path.get(commit)) && !commit.equals(end_sha1)){
                        continue;
                    }
                    Commit curr_commit = Commit.getCommit(commit);
                    for(String file_sha1: curr_commit.getSnapshot().keySet()){
                        Utils.
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }


}
