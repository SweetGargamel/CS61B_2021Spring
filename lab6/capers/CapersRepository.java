package capers;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.File;
import java.io.IOException;

import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = join(CWD,".capers"); // TODO Hint: look at the `join`
                                            //      function in Utils
    static final File STORY=join(CAPERS_FOLDER,"story");
    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        CAPERS_FOLDER.mkdirs();
        Dog.DOG_FOLDER.mkdirs();
        try {
            STORY.createNewFile();
        } catch (IOException e) {
            exitWithError(e.toString());
        }

    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
//        String old_story=readContentsAsString(STORY);
//        String new_story=old_story+"\n"+text;
//        writeContents(STORY,new_story);
//        System.out.println(new_story);
        text = readContentsAsString(STORY) + text +"\n" ;
        writeContents(STORY, text);     // 覆盖文件内容
        System.out.println(text);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        Dog g=new Dog(name,breed,age);
        System.out.println(g);
        g.saveDog();
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        Dog d1 =Dog.fromFile(name);
        d1.haveBirthday();
    }
}
