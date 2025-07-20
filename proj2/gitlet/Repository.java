package gitlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The HEAD Path and Stage Path
     */
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    public static final File STAGE_FILE = join(GITLET_DIR, "STAGE");
    public static final File BRANCH_FILE = join(GITLET_DIR, "BRANCH");
    /**
     * The Blob path and the commit path
     */
    public static final File COMMIT_DIR = join(GITLET_DIR, "COMMIT");
    public static final File BLOB_DIR = join(GITLET_DIR, "BLOB");
    public static final File REFS_DIR = join(GITLET_DIR, "REFS");


    /**
     * The HEAD varivable stores the HEAD sha-1 ID
     */
    public String HEAD;
    /**
     * Determine whether it has been initialized.
     */
    private boolean isInitialized;

    /**
     * The reference of Stage
     */
    private Stage stage;
    private String now_branch;

    public Repository() {
        if (GITLET_DIR.exists()) {
            isInitialized = true;

            HEAD = getHEAD();
            stage = readObject(STAGE_FILE, Stage.class);
            now_branch = getBranch();
        } else {
            isInitialized = false;
            HEAD = null;
            stage = null;
            now_branch = "";
        }
    }

    private static String getHEAD() {
        return readContentsAsString(HEAD_FILE);
    }

    private static String getBranch() {
        return readContentsAsString(BRANCH_FILE);
    }


    /**
     * Creates a new Gitlet version-control system in the current directory
     * Default with a commit (initial commit)
     * with a branch `master`
     */
    public void init() {
        if (this.isInitialized) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        } else {
            GITLET_DIR.mkdirs();
            COMMIT_DIR.mkdirs();
            BLOB_DIR.mkdirs();
            REFS_DIR.mkdirs();

            Commit init_commit = new Commit();//Create the init commit .
            HEAD = init_commit.getUID();

            Branch now_branch = new Branch("master", HEAD);
            this.now_branch = "master";

            this.stage = new Stage();
            this.saveToFile();
        }
    }

    public void add(String filename) {
        this.validateIsInitialized();
        // call the method of stage to add.
        this.stage.putAddStage(filename, this.HEAD);
        saveToFile();
    }

    public void commit(String commitMessage) {
        this.validateIsInitialized();
        if (this.stage.getRemoveStage().isEmpty() && this.stage.getAddStage().isEmpty()) {
            System.out.println("No changes added to the commit. ");
            System.exit(0);
        }

        Commit new_commit = new Commit(commitMessage, HEAD, this.stage);
        new_commit.dump();//CREATE the commit and store it.
        HEAD = new_commit.getUID();
        Branch now_branch = Branch.get_Branch(this.now_branch);
        now_branch.updateUID(new_commit.getUID());
        now_branch.dump();
        this.stage.clear();
        saveToFile();
    }

    public void rm(String filename) {
        this.validateIsInitialized();
        File rmFile = new File(CWD, filename);//Create the fileInstance
        //Corner Case1: no such file
        if (!rmFile.exists()) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        //Corner Case2 : not staged or commited
        Commit HEAD_commit = null;
        try {
            HEAD_commit = Commit.getCommit(HEAD);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!this.stage.isStagedInAdd(filename) && !HEAD_commit.hasFileTracked(filename)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        //Case 1: when is Staged,the remove it from addstage
        if (this.stage.isStagedInAdd(filename)) {
            this.stage.removeFromAddStage(filename);
        }
        //Case2 : Tracked
        if (HEAD_commit.hasFileTracked(filename)) {
            this.stage.putRemoveStage(filename);
            Utils.restrictedDelete(filename);
        }
        saveToFile();
    }

    public void log() {
        String curr_UID = HEAD;
        Commit curr_commit;
        while (curr_UID != "") {
            try {
                curr_commit = Commit.getCommit(curr_UID);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println("===");
            System.out.println("commit " + curr_UID);
            if (curr_commit.isMergeCommit()) {
                String[] parent_arr = (String[]) curr_commit.getParents().toArray();
                String par1 = parent_arr[0];
                String par2 = parent_arr[1];
                System.out.println(par1.substring(0, 7) + " " + par2.substring(0, 7));
            }
            System.out.println("Date: " + curr_commit.getFormatedDate());
            System.out.println(curr_commit.getMessage());
            System.out.println();
            curr_UID = (String) curr_commit.getParents().toArray()[0];
        }
    }

    public void global_log() {
        validateIsInitialized();

        for (String commit_UID : Commit.getAllCommits()) {
            System.out.println("===");
            System.out.println("commit " + commit_UID);
            Commit curr_commit = null;
            try {
                curr_commit = Commit.getCommit(commit_UID);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (curr_commit.isMergeCommit()) {
                String[] parent_arr = (String[]) curr_commit.getParents().toArray();
                String par1 = parent_arr[0];
                String par2 = parent_arr[1];
                System.out.println(par1.substring(0, 7) + " " + par2.substring(0, 7));
            }
            System.out.println("Date: " + curr_commit.getFormatedDate());
            System.out.println(curr_commit.getMessage());
            System.out.println();
        }
    }

    public void find(String target_msg) {
        for (String commit_UID : Utils.plainFilenamesIn(COMMIT_DIR)) {
            Commit curr_commit = null;
            try {
                curr_commit = Commit.getCommit(commit_UID);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (target_msg.equals(curr_commit.getMessage())) {
                System.out.println(commit_UID);
            }
        }
    }

    public void status() {

        System.out.println("=== Branches ===");
        for (String branch_name : Branch.getAllBranches()) {
            if (branch_name.equals(this.now_branch)) {
                System.out.println("*" + branch_name);
            } else {
                System.out.println(branch_name);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (String filename : this.stage.getAddStage().keySet()) {
            System.out.println(filename);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String filename : this.stage.getRemoveStage()) {
            System.out.println(filename);
        }
        System.out.println();

        List<String> Modified_Not_Staged = new ArrayList<>();
        Commit curr_commit = null;
        try {
            curr_commit = Commit.getCommit(HEAD);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        for (String filename : curr_commit.getSnapshot().keySet()) {
            File file = new File(filename);
            if (!file.exists()) {
                Modified_Not_Staged.add(filename + " (deleted)");
            } else {
                if (!this.stage.isStagedInAdd(filename) && curr_commit.TrackedAButDiffer(filename)) {
                    Modified_Not_Staged.add(filename + " (modified)");
                }
            }
        }
        for (String filename : this.stage.getAddStage().keySet()) {
            File file = new File(filename);
            if (!file.exists()) {
                Modified_Not_Staged.add(filename + " (deleted)");
            } else {
                if (this.stage.StagedInAddButDiffer(filename)) {
                    Modified_Not_Staged.add(filename + " (modified)");
                }
            }
        }
        String[] Modified_Not_Staged_array = Modified_Not_Staged.toArray(Modified_Not_Staged.toArray(new String[0]));
        Arrays.sort(Modified_Not_Staged_array);

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String filename : Modified_Not_Staged_array) {
            System.out.println(filename);
        }
        System.out.println();


        List<String> Untracked = getUntrackedFiles(curr_commit);
        System.out.println("=== Untracked Files ===");
        for (String filename : Untracked) {
            System.out.println(filename);
        }
        System.out.println();
    }

    private List<String> getUntrackedFiles(String curr_commit) {
        try {
            return getUntrackedFiles(Commit.getCommit(curr_commit));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getUntrackedFiles(Commit curr_commit) {
        List<String> Untracked = new ArrayList<>();
        List<String> Working_File_List = plainFilenamesIn(CWD);
        for (String filename : Working_File_List) {
            if (!this.stage.isStagedInAdd(filename)
                    && !this.stage.isStagedInRemove(filename)
                    && !curr_commit.hasFileTracked(filename)) {
                Untracked.add(filename);
            }

        }
        return Untracked;
    }

    public void checkout_1(String filename) {
        checkout_2(HEAD, filename);
    }

    public void checkout_2(String UID, String filename) {

        Commit target_commit = null;
        try {
            target_commit = Commit.getCommit(UID);
        } catch (FileNotFoundException e) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (!target_commit.hasFileTracked(filename)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String file_sha1 = target_commit.getSnapshot().get(filename);
        restoreFilesToWorking(filename, file_sha1);
        if (this.stage.isStagedInAdd(filename)) {
            this.stage.removeFromAddStage(filename);
        }
        if (this.stage.isStagedInRemove(filename)) {
            this.stage.removeFromRemoveStage(filename);
        }
        saveToFile();
    }

    private void restoreFilesToWorking(String filename, String file_sha1) {
        File file_instance = new File(filename);
        if (file_instance.exists() && Utils.getFileSha1(file_instance).equals(file_sha1)) {
            //如果没有变化，就直接结束
            System.exit(0);
        }
        File store_dir = new File(Repository.BLOB_DIR, file_sha1.substring(0, 2));
        Utils.restrictedDelete(file_instance);
        Utils.copyFile(Utils.join(store_dir, file_sha1.substring(2))
                , file_instance);
    }

    public void checkout_3(String branch_name) {

        if (branch_name.equals(this.now_branch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        if (!Branch.has_branch(branch_name)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        Branch target_branch = Branch.get_Branch(branch_name);
        Commit target_commit;
        try {
            target_commit = Commit.getCommit(target_branch.ref_UID);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        checkout_to_target_Commit(target_commit);
        this.now_branch = branch_name;
        saveToFile();

    }


    private void validateHasUntrackedFilesConfilt(String commit_UID, Set<String> setToDetect) {
        for (String untracked_file : getUntrackedFiles(commit_UID)) {
            if (setToDetect.contains(untracked_file)) {
                System.out.println(
                        "There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    private void checkout_to_target_Commit(Commit target_commit) {
        validateHasUntrackedFilesConfilt(HEAD,target_commit.getSnapshot().keySet());
        Commit curr_commit = null;
        try {
            curr_commit = Commit.getCommit(HEAD);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //删除所有当前追踪但是目标里面没有追踪的
        for (String now_tracked_file : curr_commit.getSnapshot().keySet()) {
            if (!target_commit.getSnapshot().containsKey(now_tracked_file)) {
                File now_file = new File(now_tracked_file);
                Utils.restrictedDelete(now_file);
            }
        }
        //开始覆盖所有的文件
        for (Map.Entry<String, String> entry : target_commit.getSnapshot().entrySet()) {
            String target_filename = entry.getKey();    // 获取键
            String target_filesha1 = entry.getValue();  // 获取值
            restoreFilesToWorking(target_filename, target_filesha1);
        }
        this.stage.clear();
        this.HEAD = target_commit.getUID();
        this.saveToFile();
    }

    public void branch(String new_branch) {
        validateIsInitialized();
        Branch branch = new Branch(new_branch, HEAD);
        this.now_branch = new_branch;
        saveToFile();
    }


    public void rm_branch(String branch_name) {
        if (branch_name.equals(this.now_branch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        for (String b_name : Branch.getAllBranches()) {
            if (b_name.equals(branch_name)) {
                Branch.removeBranch(b_name);
                System.exit(0);
            }
        }
        System.out.println("A branch with that name does not exist.");
        saveToFile();
    }

    public void reset(String UID) {
        Commit target_commit = null;
        try {
            target_commit = Commit.getCommit(UID);
        } catch (FileNotFoundException e) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        checkout_to_target_Commit(target_commit);
        Branch curr_branch = Branch.get_Branch(this.now_branch);
        curr_branch.updateUID(this.HEAD);
        saveToFile();

    }

    public void merge(String another_branch) {
        if (!this.stage.getRemoveStage().isEmpty() && !this.stage.getAddStage().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!Branch.has_branch(another_branch)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);

        }
        if (another_branch.equals(this.now_branch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        Commit curr_commit = null;
        Commit another_commit = null;
        Branch another_baranch_instance = null;
        try {
            curr_commit = Commit.getCommit(HEAD);
            another_baranch_instance = Branch.get_Branch(another_branch);
            another_commit = Commit.getCommit(another_baranch_instance.ref_UID);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        /// 先做一些检查
        String split_commit = Commit.find_split_commit(curr_commit, another_commit);
        if (split_commit.equals(another_baranch_instance.ref_UID)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (split_commit.equals(this.HEAD)) {
            System.out.println("Current branch fast-forwarded.");
            checkout_3(another_branch);
            System.exit(0);
        }

        Commit split_commit_instance = null;
        try {
            split_commit_instance = Commit.getCommit(split_commit);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> all_related_files = new HashMap<>();
        //这里构建一个merge plan,默认所有的都是空
        for (String file : curr_commit.getSnapshot().keySet()) {
            all_related_files.put(file, "");
        }
        for (String file : another_commit.getSnapshot().keySet()) {
            all_related_files.put(file, "");
        }
        for (String file : split_commit_instance.getSnapshot().keySet()) {
            all_related_files.put(file, "");
        }


        //开始考虑所有情况
        for (String filename : all_related_files.keySet()) {
            String curr_file_sha1 = curr_commit.getTrackedFileSha1(filename);
            String another_file_sha1 = another_commit.getTrackedFileSha1(filename);
            String split_commit_file_sha1 = split_commit_instance.getTrackedFileSha1(filename);

            if (curr_file_sha1.equals(another_file_sha1)
                    && curr_file_sha1.equals(split_commit_file_sha1)) {
                all_related_files.put(filename, curr_file_sha1);
                continue; //如果三个里面的情况一样，就不动了
            }

            //case1 在given branch修改的但是再 current branch没有修改的，使用given branch
            //      同时会被暂存
            //case6 所有之前有的，current branch没动的，但是given branch 删除的，要删除

            else if (curr_file_sha1.equals(split_commit_file_sha1)) {
                all_related_files.put(filename, another_file_sha1);
            }

            //case2 current branch动了的但是given branch没动的，保留
            //case7 所有之前有的，current branch删除的，但是given branch 没动的，保持不存在

            else if (another_file_sha1.equals(split_commit_file_sha1)) {
                all_related_files.put(filename, curr_file_sha1);
            }
            //case3 在current branch和 given branch 的修改方式一样（包括删除）
            //      特别的，如果删了之后又新建了一个的话，不管他，仍然保留为Untracked
            else if (curr_file_sha1.equals(another_file_sha1)) {
                all_related_files.put(filename, "Nothing");
            }
            //case4 所有之前没有但是现在current branch里面有的保留
            else if (split_commit_file_sha1.equals("") && another_file_sha1.equals("")) {
                all_related_files.put(filename, curr_file_sha1);
            }
            //case5 所有之前没有，但是再given branch里面有的checkout 并且 stage
            else if (split_commit_file_sha1.equals("") && curr_file_sha1.equals("")) {
                all_related_files.put(filename, another_file_sha1);
            }


            //case8 合并冲突
            else {
                String new_content = "<<<<<<< HEAD\n"
                        + Utils.readContentOfBlobs(curr_commit.getUID())
                        + "=======" + Utils.readContentOfBlobs(another_commit.getUID())
                        + ">>>>>>>";
                all_related_files.put(filename, new_content);
            }
        }
        validateHasUntrackedFilesConfilt(HEAD,all_related_files.keySet());
        boolean isConfilt=false;
        for(Map.Entry<String,String> entry: all_related_files.entrySet()) {
            String value = entry.getValue();
            //confilt
            if(value.startsWith("<<<<<<<")){
                isConfilt=true;
                Utils.writeContents(Utils.join(CWD,entry.getKey()),value);
            }
        }
        if(isConfilt){
            System.out.println("Encountered a merge conflict.");
        }

        saveToFile();

    }

    /**
     * Save the current status of Repository
     */
    private void saveToFile() {
        writeContents(HEAD_FILE, HEAD);
        writeContents(BRANCH_FILE, now_branch);
        this.stage.dump();

    }

    private void validateIsInitialized() {
        if (!this.isInitialized) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

}
