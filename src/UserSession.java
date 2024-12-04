public class UserSession {
    private static int userId= -1; // Store the logged-in user's ID

    public static void setUserId(int id) {
        userId = id;
    }

    public static int getUserId() {
        return userId;
    }
}
