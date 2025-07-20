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
        } catch (FileNotFoundException e) {
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
    public static Commit getCommit(String UID) throws FileNotFoundException {
        if (UID.length() == Utils.UID_LENGTH) {
            return getCommitWithFullSha1(UID);
        } else if (UID.length() > Utils.UID_LENGTH || UID.length() != 6) {
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

    private static Commit getCommitWithFullSha1(String UID) throws FileNotFoundException {
        if (UID.length() == Utils.UID_LENGTH) {
            return Utils.readObject(Utils.join(Repository.COMMIT_DIR, UID), Commit.class);
        }else{
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

    public static String find_split_commit(Commit commit1,Commit commit2) {
        Set<String> ancestors = new HashSet<>();
        String current_commit_sha1 = commit1.getUID();

        while(current_commit_sha1!="" ) {
            Commit curr_commit= null;
            try {
                curr_commit = Commit.getCommit(current_commit_sha1);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            ancestors.add(curr_commit.getUID());
            current_commit_sha1 = curr_commit.getParents().get(0);
        }

        current_commit_sha1 = commit2.getUID();
        while(!ancestors.contains(current_commit_sha1)) {
            Commit curr_commit= null;
            try {
                curr_commit = Commit.getCommit(current_commit_sha1);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            current_commit_sha1 = curr_commit.getParents().get(0);
        }
        return current_commit_sha1;
    }
    public String getTrackedFileSha1(String filename) {
        return this.snapshot.getOrDefault(filename,"");
    }
}
