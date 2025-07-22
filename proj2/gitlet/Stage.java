package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Stage implements Dumpable {
    private Map<String, String> addStage;
    private Set<String> removeStage;

    public Stage() {
        addStage = new HashMap<String, String>();
        removeStage = new HashSet<String>();
    }

    @Override
    public void dump() {
        if (!Repository.STAGE_FILE.exists()) {
            try {
                Repository.STAGE_FILE.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Utils.writeObject(Repository.STAGE_FILE, this);

    }

    public void putAddStage(String added_filename, String HEAD) {
        // To get the lastest commit
        Commit HEAD_Commit = null;
        try {
            HEAD_Commit = Commit.getCommit(HEAD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //To validate if the file exists in the working directory
        File added_fileInstance = new File(Repository.CWD, added_filename);

        // CASE1: File does not exist.
        if (!added_fileInstance.exists() && !this.isStagedInRemove(added_filename)
        ) {
            System.out.println("File does not exist.");
            return;
        }
        //CASE0:Already in removeStage

        if (isStagedInRemove(added_filename)) {
            removeFromRemoveStage(added_filename);
            this.dump();

            return;
        }
        //calculate the sha1 of the file in working directory
        String added_file_sha1 = Utils.getFileSha1(added_fileInstance);
        //CASE2:if is staged and the added file is not changed
        if (isStagedInAdd(added_filename)
                && added_file_sha1.equals(this.addStage.get(added_filename))) {
            return;
        //CASE3:if is  the same as the latest commit
        } else if (added_file_sha1.equals(HEAD_Commit.getTrackedFileUID(added_filename))) {
            if (isStagedInAdd(added_filename)) {
                removeFromAddStage(added_filename);
            }
            return;
        } else {
            //CASE4:
            // if is not staged or is changed ,just copy the current file in working directory
            // to the blobs.Note that we just still use the name of the file Rather than the sha1 ID.
            storeFileInBlobs(added_filename, added_file_sha1);
            this.addStage.put(added_filename, added_file_sha1);
        }

    }


    /**
     * 判断当前工作区的文件是否和暂存区的不同
     * @return
     */
    public boolean StagedInAddButDiffer(String filename){
        return !(this.addStage.get(filename).equals(Utils.getFileSha1(filename)));
    }

    public void putRemoveStage(String removed_filename) {
        this.removeStage.add(removed_filename);
    }

    public void clear() {
        addStage.clear();
        removeStage.clear();

    }

    /**
     * Helper method that remove a file from the addStage or removeStage.
     *
     * @param
     */
    public void removeFromRemoveStage(String addedFilename) {
        this.removeStage.remove(addedFilename);
    }

    public void removeFromAddStage(String removed_filename) {
        this.addStage.remove(removed_filename);
    }

    /**
     * two helper methods that validate if the file is staged
     *
     * @param filename
     * @return
     */
    public boolean isStagedInAdd(String filename) {
        return addStage.containsKey(filename);
    }

    public boolean isStagedInRemove(String filename) {
        return removeStage.contains(filename);
    }


    public Map<String, String> getAddStage() {
        return addStage;
    }

    public Set<String> getRemoveStage() {
        return removeStage;
    }

    /**
     * Helper function that help store files in Blobs
     *
     * @param filename
     * @param file_sha1
     */
    private void storeFileInBlobs(String filename, String file_sha1) {
        storeFileInBlobs(new File(Repository.CWD, filename), file_sha1);
    }


    private void storeFileInBlobs(File file, String file_sha1) {
        File store_dir = new File(Repository.BLOB_DIR, file_sha1.substring(0, 2));
        if (!store_dir.exists()) {
            store_dir.mkdir();
        }
        Utils.copyFile(file, Utils.join(store_dir, file_sha1.substring(2)));
    }
}
