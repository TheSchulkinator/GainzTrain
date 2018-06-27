package theschulk.com.gainztrain.Database;

public class test {
    public static String compress(String input){
        int stringLength = input.length();

        String stringCompressed = "";
        char stringChar = input.charAt(0);
        int count = 1;
        for(int i = 1; i < stringLength; i++) {
            if(input.charAt(i) == stringChar) count++;
            else {
                stringCompressed += Character.toString(stringChar) + Integer.toString(count);
                stringChar = input.charAt(i);
                count = 1;
            }
        }
        stringCompressed += Character.toString(stringChar) + Integer.toString(count);
        if(stringCompressed.length() < stringLength) return stringCompressed;
        return input;

}
}
