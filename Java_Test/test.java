public class test {
    public static void main(String[] args) {
        String s1 = "FB";
        String s2 = "Ea";

        System.out.println("String 1: \"" + s1 + "\"");
        System.out.println("String 2: \"" + s2 + "\"");
        System.out.println();
        System.out.println("hashCode for s1: " + s1.hashCode());
        System.out.println("hashCode for s2: " + s2.hashCode());
        System.out.println("Are the hash codes equal? " + (s1.hashCode() == s2.hashCode()));
    }
}