package it.niko.mywatchlist.support.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String nickname, String email){
        super("Could not find the user [" + nickname + ", " + email + "] you were looking for");
    }

    public UserNotFoundException(String email) {
        super("Could not find the user [" + email + "] you were looking for");
    }
}
