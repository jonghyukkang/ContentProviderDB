package kang.recyclerdb.ETC;

import java.util.Random;

import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 7. 21..
 */
public class Cheeses {

    private static final Random RANDOM = new Random();

    public static int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.cheese_1;
            case 1:
                return R.drawable.cheese_2;
            case 2:
                return R.drawable.cheese_3;
            case 3:
                return R.drawable.cheese_4;
            case 4:
                return R.drawable.cheese_5;
        }
    }
}
