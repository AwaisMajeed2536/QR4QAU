package awais.majeed.services.core;



import java.io.IOException;

public class NoInternetException extends IOException {
    @Override
    public String getMessage() {
        return "Error! No Internet connection found...";
    }

} // NoInternetException
