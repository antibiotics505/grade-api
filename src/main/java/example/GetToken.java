package example;

import java.io.IOException;

/**
 * Class for getting your Grade API token.
 */
public class GetToken {

    /**
     * Run to get your token after you complete the code.
     * @param args the desired practice username (first argument)
     */
    public static void main(String[] args) {
        GetExample example = new GetExample();

        String gradeAPISignUpURL = "https://csc207-20269-vm.teach.cs.toronto.edu/signUp";
        String userName = args.length == 0 ? "" : args[0];
        if (userName.isEmpty()) {
            System.out.println("please choose a non-empty user name!");
            System.exit(0);
        }
        String response = "";
        try {
            response = example.run(gradeAPISignUpURL, "username", userName);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (!response.isEmpty()) {
            org.json.JSONObject payload = new org.json.JSONObject(response);
            if (payload.getInt("status_code") != 200) {
                throw new IllegalStateException(payload.optString("message", "Signup failed"));
            }
            try {
                java.nio.file.Path path = java.nio.file.Paths.get("token.txt");
                java.nio.file.Files.writeString(path, "username=" + userName + "\ntoken=" + payload.getString("token") + "\n");
                java.nio.file.Files.setPosixFilePermissions(path,
                        java.nio.file.attribute.PosixFilePermissions.fromString("rw-------"));
                System.out.println("Practice token saved locally in ignored token.txt (not printed).");
            }
            catch (IOException e) {
                throw new IllegalStateException("Could not save the practice token locally", e);
            }
        }
    }
}
