package visuals;

public class Printer {



    public static void print_conjugateGradient(double res, int iteration, long millis, int cores){
        int min = (int) (millis / (60 * 1000));
        int sec = (int) ((millis / 1000) % 60);
        int mil = (int) (millis % 1000);

        System.out.format("\rres: %5.3E  it: %-6d time[m,s,ms]: %02d:%02d:%03d  cores: %4d",
                res,
                iteration,
                min,
                sec,
                mil,
                cores);
    }


}
