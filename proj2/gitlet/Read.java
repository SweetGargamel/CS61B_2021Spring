package gitlet;

import java.io.File;

public class Read {
    public static void main(String[] args) {
       Branch b = Utils.readObject(
               new File("E:\\CodePlace\\CS61B_Skeleton21\\proj2\\.gitlet\\REFS\\other"),
               Branch.class);
       System.out.println(b.ref_UID);
       System.out.println(b.name);

        Commit c = null;
        try {
            c = Commit.getCommit("3eabe72e21c54533e2fbbf7496d729a0790ed328");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(c.getSnapshot());

    }
}
