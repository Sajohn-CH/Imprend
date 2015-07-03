package spacingAlgorithms;

import java.util.Date;

/**
 * Created by samuel on 30.06.15.
 * It is implementing the SM-2 algorithm (http://www.supermemo.com/english/ol/sm2.htm), but i modified it slightly. It has less responses
 * response:
 * 0 = black out; no idea
 * 1 = incorrect answer
 * 2 = correct answer, but with some difficulties to remember
 * 3 = correct answer, good able to remember
 * 4 = correct answer, instant response
 * It should act as an example of what a spacing-algorithm should do/implement
 */
public class VerySimpleCard {

    public static Date getNextDate(double ease, int amountRepetition, Date oldDate, Date date) {
        Date nextDate;
        long intervall;
        Date currentDate = new Date(System.currentTimeMillis());

        //get old Intervall in Days:
        //get differenc in msec
        Long oldIntervall = date.getTime() - oldDate.getTime();
        //change unit to days
        oldIntervall /= (1000*60*60*24);


        switch(amountRepetition) {
            case 1:
                intervall = 1;
                break;
            case 2:
                intervall = 6;
                break;
            default:
                intervall =  Math.round(oldIntervall*ease);
        }

        nextDate = new Date(currentDate.getTime() + intervall * (1000*60*60*24));

        return nextDate;
    }

    public static double getNewEase(double ease, int response) {
        if(ease > 1.3) {
            return  1.3;
        }
        return ease+(0.1-(4-response)*(0.08+(4-response)*0.02));
    }

}
