package gitlet;

import java.io.File;
import java.util.List;

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


}
