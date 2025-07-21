package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];

        Repository repo = new Repository();
        switch (firstArg) {
            case "init":
                checkArgs(args, 1);
                repo.init();
                break;
            case "add":
                checkArgs(args, 2);
                repo.add(args[1]);
                break;
            case "commit":
                checkCommitArgs(args);
                repo.commit(args[1]);
                break;
            case "rm":
                checkArgs(args, 2);
                repo.rm(args[1]);
                break;
            case "log":
                checkArgs(args, 1);
                repo.log();
                break;
            case "global-log":
                checkArgs(args, 1);
                repo.global_log();
                break;
            case "find":
                checkArgs(args, 2);
                repo.find(args[1]);
                break;
            case "status":
                checkArgs(args, 1);
                repo.status();
                break;
            case "checkout":
                int opt_type = check_checkout(args);
                switch (opt_type) {
                    case 1:
                        repo.checkout_1(args[2]);
                        break;
                    case 2:
                        repo.checkout_2(args[1], args[3]);
                        break;
                    case 3:
                        repo.checkout_3(args[1]);
                        break;
                    default:
                        System.out.println("Incorrect operands.");
                }
                break;
            case "branch":
                checkArgs(args, 2);
                repo.branch(args[1]);
                break;
            case "rm-branch":
                checkArgs(args, 2);
                repo.rm_branch(args[1]);
                break;
            case "reset":
                checkArgs(args, 2);
                repo.reset(args[1]);
                break;
            case "merge":
                checkArgs(args, 2);
                repo.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
                break;
        }
    }

    public static void checkArgs(String[] args, int num) {
        if (args.length != num) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void checkCommitArgs(String[] args) {
        if (args.length > 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else if (args.length == 1) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        } else if (args.length == 2) {
            if(args[1].equals("")) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            }
        }
    }

    public static int check_checkout(String[] args) {
        //这里返回的数字根据手册上的顺序来
//        java gitlet.Main checkout -- [file name]
//        java gitlet.Main checkout [commit id] -- [file name]
//        java gitlet.Main checkout [branch name]
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            } else {
                return 1;
            }
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            } else {
                return 2;
            }
        } else if (args.length == 2) {
            return 3; //
        } else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        return 0;
    }
}
