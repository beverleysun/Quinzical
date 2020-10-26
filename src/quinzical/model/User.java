package quinzical.model;

import java.util.Comparator;

/**
 * Represents a past user and their score
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class User {

    private final String name;
    private final int score;

    /**
     * Creates a user
     * @param name user's name
     * @param score user's score
     */
    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Gets name of user
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * Gets user's score
     * @return the user's score
     */
    public int getScore() {
        return score;
    }

    public static class UserComparator implements Comparator<User>{
        /**
         * Implemented method so that a list of users can be sorted based on their scores
         * @param user1 first user to compare
         * @param user2 second user to compare
         * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
         */
        @Override
        public int compare(User user1, User user2) {
            if (user1.score > user2.score) {
                return -1;
            } else if (user1.score == user2.score) {
                return 0;
            }
            return 1;
        }
    }
}
