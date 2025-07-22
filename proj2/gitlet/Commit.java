package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class does at a high level.
 *
 * @author TODO
 */
public class Commit implements Dumpable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The UID of this commit
     */
    private String UID;

    /**
     * The message of this Commit.
     */
    private String message;

    /**
     * The parent commit using sha1
     */
    private List<String> parents;
    /**
     * The commit datatime
     */
    private Date date;

    /**
     * The snapshot Tree. From the filename
     * to the sha-1 reference of the blob
     */
    private Map<String, String> snapshot;

    /**
     *
     */
    private boolean isMergeCommit;

    /**
     * For usual Commit
     */
    public Commit(String message, String parent, Stage stage) {
        this.message = message;
        this.parents = new ArrayList<>();
        this.parents.add(parent);
        this.snapshot = Commit.getParentCommit(parent).snapshot;

        for (String key : stage.getAddStage().keySet()) {
            this.snapshot.put(key, stage.getAddStage().get(key));
        }
        for (String key : stage.getRemoveStage()) {
            this.snapshot.remove(key);
        }
        this.date = new Date();
        this.UID = Utils.sha1(this.message, this.parents.toString(), this.snapshot.toString(), this.date.toString());
        this.dump();
    }
    /**
     *
     * for merge commit
     */
    public Commit(String message,String parent1,String parent2,Stage stage) {
        this.message = message;
        this.parents = new ArrayList<>();
        this.parents.add(parent1);
        this.parents.add(parent2);
        this.snapshot = Commit.getParentCommit(parent1).snapshot;
        for (String key : stage.getAddStage().keySet()) {
            this.snapshot.put(key, stage.getAddStage().get(key));
        }
        for (String key : stage.getRemoveStage()) {
            this.snapshot.remove(key);
        }
        this.date = new Date();
        this.UID = Utils.sha1(this.message, this.parents.toString(), this.snapshot.toString(), this.date.toString());
        this.isMergeCommit = true;
        this.dump();
    }

    /**
     * For Init commit
     */
    public Commit() {
        this.message = "initial commit";
        this.parents = new ArrayList<>();
        this.parents.add("");
        this.date = new Date(0);
        this.snapshot = new HashMap<>();
        this.isMergeCommit = false;
        this.UID = Utils.sha1(this.message, this.parents.toString(), this.snapshot.toString(), this.date.toString());
        this.dump();
    }




    public String getMessage() {
        return message;
    }

    public List<String> getParents() {
        return parents;
    }

    public Date getDate() {
        return date;
    }

    public Map<String, String> getSnapshot() {
        return snapshot;
    }

    public String getFormatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "E MMM d HH:mm:ss yyyy Z",
                Locale.ENGLISH
        );

        sdf.setTimeZone(TimeZone.getTimeZone("GMT-8:00"));

        String finalOutput = "Date: " + sdf.format(this.date);
        return finalOutput;
    }

    public String getUID() {
        return UID;
    }

    /**
     * To vaildate if the file is tracked
     *
     * @param filename
     * @return
     */
    public boolean hasFileTracked(String filename) {
        return this.snapshot.keySet().contains(filename);
    }

    /**
     * Get the UID of the Tracked File
     *
     * @param filename
     * @return
     */
    public String getTrackedFileUID(String filename) {
        return this.snapshot.get(filename);
    }

    /**
     * helper function that reads the parent
     *
     * @param ParentUID
     * @return
     */
    public static Commit getParentCommit(String ParentUID) {
        Commit parent_Commit = null;
        try {
            parent_Commit = getCommit(ParentUID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return parent_Commit;
    }

    /**
     * return the commit finding by UID.
     *
     * @param UID
     * @return
     */
    public static Commit getCommit(String UID) throws Exception {
        if (UID.length() == Utils.UID_LENGTH) {
            return getCommitWithFullSha1(UID);
        } else if (UID.length() > Utils.UID_LENGTH) {
            throw new FileNotFoundException();
        } else {
            List<String> possible_commits = new ArrayList<>();
            for (String commit_id : getAllCommits()) {
                if (commit_id.startsWith(UID)) {
                    possible_commits.add(commit_id);
                }
            }
            if (possible_commits.size() == 0 || possible_commits.size() > 1) {
                throw new FileNotFoundException();
            } else {
                return getCommitWithFullSha1(possible_commits.get(0));
            }
        }
    }

    private static Commit getCommitWithFullSha1(String UID) throws Exception {
        if (UID.length() == Utils.UID_LENGTH) {
            return Utils.readObject(Utils.join(Repository.COMMIT_DIR, UID), Commit.class);
        } else {
            throw new FileNotFoundException();
        }
    }


    public static List<String> getAllCommits() {
        return Utils.plainFilenamesIn(Repository.COMMIT_DIR);
    }

    @Override
    public void dump() {
        File curr_commit_file = Utils.join(Repository.COMMIT_DIR, this.UID);
        if (!curr_commit_file.exists()) {
            try {
                curr_commit_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Utils.writeObject(curr_commit_file, this);
    }

    public boolean isMergeCommit() {
        return isMergeCommit;
    }

    public boolean TrackedAButDiffer(String filename) {
        return !this.snapshot.get(filename).equals(Utils.getFileSha1(filename));
    }


    public static String find_split_commit(Commit commit1, Commit commit2) {
        Map<String,Integer> comm1_toInit = getPathToInit(commit1);
        Queue<String> queue = new ArrayDeque<>();
        queue.add(commit2.getUID());
        String curr_UID = commit2.getUID();
        while (!queue.isEmpty()) {
            curr_UID = queue.poll();
            if(comm1_toInit.containsKey(curr_UID)) {
                return curr_UID;
            }
            Commit curr_commit = null;
            try {
                curr_commit = Commit.getCommit(curr_UID);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for(String com : curr_commit.getParents()){
                queue.add(com);
            }
        }
        return null;
    }

    /**
     *
     * @param start_commit
     * @param
     * @return
     */
    public static Map<String, Integer> getPathToTargetCommit(Commit start_commit,Commit end_commit){
        Map<String, Integer> paths = new HashMap<>();
        Queue<String> queue = new ArrayDeque<>();
        queue.add(start_commit.getUID());
        String curr_UID = start_commit.getUID();
        paths.put(start_commit.getUID(), 0);

        while (!queue.isEmpty()) {
            curr_UID = queue.poll();
            Commit curr_commit = null;
            try {
                curr_commit = Commit.getCommit(curr_UID);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for(String com : curr_commit.getParents()){
                if(com.equals( end_commit.getUID())){
                    return paths;
                }
                if(paths.containsKey(com)){
                    continue;
                }else{
                    paths.put(com, paths.get(curr_UID) + 1);
                }
                queue.add(com);
            }

        }
        return paths;
    }

//    public String getInitCommit(){
//
//
//    }
    public static Map<String, Integer> getPathToInit(Commit commit) {
        Map<String, Integer> paths = new HashMap<>();
        Queue<String> queue = new ArrayDeque<>();
        queue.add(commit.getUID());
        String curr_UID = commit.getUID();
        paths.put(commit.getUID(), 0);

        while (!queue.isEmpty()) {
            curr_UID = queue.poll();
            Commit curr_commit = null;
            try {
                curr_commit = Commit.getCommit(curr_UID);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for(String com : curr_commit.getParents()){
                if(com == ""){
                    return paths;
                }
                if(paths.containsKey(com)){
                    continue;
                }else{
                    paths.put(com, paths.get(curr_UID) + 1);
                }
                queue.add(com);
            }
        }
        return paths;

    }
    public void printCommit(){
        System.out.println("===");
        System.out.println("commit " + this.UID);
        if (this.isMergeCommit()) {
            List<String> parent_arr = this.getParents();
            String par1 = parent_arr.get(0);
            String par2 = parent_arr.get(1);
            System.out.println(par1.substring(0, 7) + " " + par2.substring(0, 7));
        }
        System.out.println(this.getFormatedDate());
        System.out.println(this.getMessage());
        System.out.println();
    }

    public String getTrackedFileSha1(String filename) {
        return this.snapshot.getOrDefault(filename, "");
    }
}
