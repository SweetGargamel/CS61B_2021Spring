package gitlet;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Remote implements Dumpable {
    public String remote_name;
    public String remote_path;

    public Remote(String remote_name, String remote_path) {
        if (getAllRemotes() != null) {
            for (String r_name : getAllRemotes()) {
                if (r_name.equals(remote_name)) {
                    System.out.println("A remote with that name already exists.");
                    System.exit(0);
                }
            }
        }

        this.remote_name = remote_name;
        this.remote_path = remote_path;
        this.dump();
    }

    public void remove_self() {
        for (String r_name : getAllRemotes()) {
            if (r_name.equals(this.remote_name)) {
                File remote_file = Utils.join(Repository.REMOTE_DIR, this.remote_name);
                remote_file.delete();
                System.exit(0);
            }
        }
        System.out.println("A remote with that name does not exist.");
    }

    @Override
    public void dump() {
        Utils.writeObject(Utils.join(Repository.REMOTE_DIR, this.remote_name), this);
    }

    public static Remote getRemote(String remote_name) throws Exception {
        return Utils.readObject(Utils.join(Repository.REMOTE_DIR, remote_name), Remote.class);
    }

    public static List<String> getAllRemotes() {
        return Utils.plainFilenamesIn(Repository.REMOTE_DIR);
    }


    public String getRemote_path() {
        return this.remote_path;
    }

    public static void fetchFileBetweenRepos
            (Map<String, Integer> from_repo_path_to_init, File from_gitlet_dir, File to_gitlet_dir) {
        for (String commit : from_repo_path_to_init.keySet()) {
            Commit curr_commit = null;
            try {
                curr_commit = Commit.getCommit(commit);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            copySingleCommits(from_gitlet_dir, to_gitlet_dir, commit);
            for (String file_sha1 : curr_commit.getSnapshot().keySet()) {
                copyBlobFiles(from_gitlet_dir, to_gitlet_dir, curr_commit.getSnapshot().get(file_sha1));
            }
        }
    }

    private static void copySingleCommits(File from_gitlet_dir, File to_gitlet_dir, String curr_commit) {
        Utils.copyFile(
                Utils.join(from_gitlet_dir, "COMMIT", curr_commit),
                Utils.join(to_gitlet_dir, "COMMIT", curr_commit));
    }

    private static void copyBlobFiles(File local_gitlet_dir, File remote_gitlet_dir, String file_sha1) {
        File sourceFileDir = Utils.join(local_gitlet_dir, "BLOB", file_sha1.substring(0, 2));
        File destinationFileDir = Utils.join(remote_gitlet_dir, "BLOB", file_sha1.substring(0, 2));
        Utils.copyFile(Utils.join(sourceFileDir, file_sha1.substring(2)),
                Utils.join(destinationFileDir, file_sha1.substring(2)));
    }
}


